// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a switch-statement.
 */
public class JSwitchStatement extends JStatement {
    // Test expression.
    private JExpression condition;

    // List of switch-statement groups.
    private ArrayList<SwitchStatementGroup> stmtGroup;

    // Highest case value, lowest case value, total real case labels, respectively.
    private int hi,lo,nLabels;

    // Variable to switch on
    //private int switchOnOffset;

    private TreeMap<Integer, String> map;

    // Determine if Statement has a break
    private boolean hasBreak;

    // Label for break
    private String breakLabel;

    /**
     * Constructs an AST node for a switch-statement.
     *
     * @param line      line in which the switch-statement occurs in the source file.
     * @param condition test expression.
     * @param stmtGroup list of statement groups.
     */
    public JSwitchStatement(int line, JExpression condition,
                            ArrayList<SwitchStatementGroup> stmtGroup) {
        super(line);
        this.condition = condition;
        this.stmtGroup = stmtGroup;
        this.nLabels = 0;
        this.hi = Integer.MIN_VALUE;
        this.lo = Integer.MAX_VALUE;
        this.hasBreak = false;
        this.breakLabel = null;

    }

    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        // Push reference to self on entry
        JMember.enclosingStatement.push(this);

        condition = (JExpression)condition.analyze(context);
        condition.type().mustMatchExpected(line(),Type.INT);

        // Deprecated please ignore
//        if(condition instanceof JVariable) {
//            JVariable variable = (JVariable)condition;
//            LocalVariableDefn variableDefn = (LocalVariableDefn)variable.iDefn();
//            this.switchOnOffset = variableDefn.offset();
//        }

        for (SwitchStatementGroup group : stmtGroup) {
            for (int j = 0; j < group.getSwitchLabels().size(); j++) {
                JExpression switchCase = group.getSwitchLabels().get(j);

                // analyze real switch cases
                if (switchCase != null) {
                    switchCase = (JExpression) switchCase.analyze(context);
                    switchCase.type().mustMatchExpected(line(), Type.INT);
                    JLiteralInt literalInt = (JLiteralInt)switchCase;

                    // increment real switch cases counter and update high/low case value
                    nLabels++;
                    hi = Integer.max(literalInt.toInt(),hi);
                    lo = Integer.min(literalInt.toInt(),lo);
                }

            }

            // Analyze statements in a block in new local context
            for (int j = 0; j < group.getSwitchBlock().size(); j++) {
                JStatement switchStatement = group.getSwitchBlock().get(j);
                LocalContext localContext = new LocalContext(context);
                switchStatement = (JStatement) switchStatement.analyze(localContext);
            }

        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {

        // Heuristic to determine using tableswitch or lookupswitch
        long tableSpaceCost = 5 + hi - lo ;
        long tableTimeCost = 3;
        long lookupSpaceCost = 3 + 2L * nLabels ;
        long lookupTimeCost = nLabels ;
        int opcode = nLabels > 0 && ( tableSpaceCost + 3 * tableTimeCost <= lookupSpaceCost + 3 * lookupTimeCost ) ?
                TABLESWITCH : LOOKUPSWITCH ;

        // Load the variable we want to switch on, onto the stack (Deprecated please ignore)
        //output.addOneArgInstruction(ILOAD, this.switchOnOffset);

        // Generate code for condition
        this.condition.codegen(output);

        String defaultLabel = output.createLabel();
        String endSwitchLabel = output.createLabel();
        if(hasBreak) {
            this.breakLabel = endSwitchLabel;
        }
        if(opcode == LOOKUPSWITCH) {
            // Initialize map
            map = new TreeMap<>();

            // Load map
            for(SwitchStatementGroup group: stmtGroup) {
                for(JExpression caseLabel: group.getSwitchLabels()) {
                    if(caseLabel != null) {
                        JLiteralInt literalInt = (JLiteralInt)caseLabel;
                        map.put(literalInt.toInt(), output.createLabel());
                    }
                }
            }

            output.addLOOKUPSWITCHInstruction(defaultLabel,nLabels,map);

            // Create labels
            int caseIndex = 0;
            for(SwitchStatementGroup group: stmtGroup) {
                if(caseIndex < nLabels) {
                    output.addLabel(map.get(map.keySet().toArray()[caseIndex]));
                } else {
                    output.addLabel(defaultLabel);
                }
                for(JStatement statement: group.getSwitchBlock()) {
                    statement.codegen(output);
                }
                caseIndex++;
            }
        } else {
            ArrayList<String> labels = new ArrayList<>();

            // Load table
            for(int i = 0; i < nLabels; i++) {
                labels.add(output.createLabel());
            }
            output.addTABLESWITCHInstruction(defaultLabel,this.lo,this.hi,labels);

            // Create labels
            int labelIndex = 0;
            for(SwitchStatementGroup group: stmtGroup) {
                if(labelIndex < nLabels) {
                    output.addLabel(labels.get(labelIndex));
                } else {
                    output.addLabel(defaultLabel);
                }
                for(JStatement statement: group.getSwitchBlock()) {
                    statement.codegen(output);
                }
                labelIndex++;
            }
        }
        // Push a default label that does nothing if no default case was defined
        if(stmtGroup.size() == nLabels) {
            output.addLabel(defaultLabel);
        }
        if(hasBreak) {
            output.addLabel(this.breakLabel);
        }
        // Pop self from stack on exit
        JMember.enclosingStatement.pop();
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JSwitchStatement:" + line, e);
        JSONElement e1 = new JSONElement();
        e.addChild("Condition", e1);
        condition.toJSON(e1);
        for (SwitchStatementGroup group : stmtGroup) {
            group.toJSON(e);
        }
    }

    /**
     * Return boolean if statement has a break
     * @return boolean if statement has a break
     */
    public boolean isHasBreak() {
        return hasBreak;
    }

    /**
     * Set boolean to hasBreak
     * @param hasBreak boolean value for hasBreak
     */
    public void setHasBreak(boolean hasBreak) {
        this.hasBreak = hasBreak;
    }

    /**
     * Get break label for statement
     * @return break label for statement
     */
    public String getBreakLabel() {
        return breakLabel;
    }

    /**
     * Set breakLabel
     * @param breakLabel - label value for break label
     */
    public void setBreakLabel(String breakLabel) {
        this.breakLabel = breakLabel;
    }
}

/**
 * A switch statement group consists of case labels and a block of statements.
 */
class SwitchStatementGroup {
    // Case labels.
    private ArrayList<JExpression> switchLabels;

    // Block of statements.
    private ArrayList<JStatement> block;

    /**
     * Constructs a switch-statement group.
     *
     * @param switchLabels case labels.
     * @param block        block of statements.
     */
    public SwitchStatementGroup(ArrayList<JExpression> switchLabels, ArrayList<JStatement> block) {
        this.switchLabels = switchLabels;
        this.block = block;
    }

    /**
     * Return the case labels for switch statement group
     * @return Return the case labels for switch statement group
     */
    public ArrayList<JExpression> getSwitchLabels() {return switchLabels;}

    /**
     * Return the block for switch statement group
     * @return Return the block for switch statement group
     */
    public ArrayList<JStatement> getSwitchBlock() {return block;}

    /**
     * Stores information about this switch statement group in JSON format.
     *
     * @param json the JSON emitter.
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("SwitchStatementGroup", e);
        for (JExpression label : switchLabels) {
            JSONElement e1 = new JSONElement();
            if (label != null) {
                e.addChild("Case", e1);
                label.toJSON(e1);
            } else {
                e.addChild("Default", e1);
            }
        }
        if (block != null) {
            for (JStatement stmt : block) {
                stmt.toJSON(e);
            }
        }
    }
}
