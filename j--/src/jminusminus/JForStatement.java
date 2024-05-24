// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.util.ArrayList;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a for-statement.
 */
class JForStatement extends JStatement {
    // Initialization.
    private ArrayList<JStatement> init;

    // Test expression
    private JExpression condition;

    // Update.
    private ArrayList<JStatement> update;

    // The body.
    private JStatement body;

    // Determine if Statement has a break or continue
    private boolean hasBreak, hasContinue;

    // Label for break and continue
    private String breakLabel, continueLabel;

    /**
     * Constructs an AST node for a for-statement.
     *
     * @param line      line in which the for-statement occurs in the source file.
     * @param init      the initialization.
     * @param condition the test expression.
     * @param update    the update.
     * @param body      the body.
     */
    public JForStatement(int line, ArrayList<JStatement> init, JExpression condition,
                         ArrayList<JStatement> update, JStatement body) {
        super(line);
        this.init = init;
        this.condition = condition;
        this.update = update;
        this.body = body;
        this.hasBreak = false;
        this.breakLabel = null;
    }

    /**
     * {@inheritDoc}
     */
    public JForStatement analyze(Context context) {
        // Push reference to self on entry
        JMember.enclosingStatement.push(this);
        LocalContext localContext = new LocalContext(context);

        if(this.init != null) {
            for(JStatement initStatement: init) {
                initStatement = (JStatement) initStatement.analyze(localContext);
            }
        }

        if(this.condition != null) {
            this.condition = (JExpression)condition.analyze(localContext);
            this.condition.type().mustMatchExpected(line(),Type.BOOLEAN);
        }

        if(this.update != null) {
            for(JStatement updateStatement: update) {
                updateStatement = (JStatement)updateStatement.analyze(localContext);
            }
        }

        this.body = (JStatement)body.analyze(localContext);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        String loopLabel = output.createLabel();
        String endLoopLabel = output.createLabel();
        String updateLabel = output.createLabel();
        // Create break label if a break statement has been analyzed
        if(this.hasBreak) {
            this.breakLabel = endLoopLabel;
        }

        // Assign continue label to update label
        if(this.hasContinue) {
            this.continueLabel = updateLabel;
        }

        // Initialize
        if(this.init != null) {
            for(JStatement initStatement: init) {
                initStatement.codegen(output);
            }
        }

        // Loop start
        output.addLabel(loopLabel);

        // Evaluate condition
        if(this.condition != null) {
            this.condition.codegen(output,endLoopLabel,false);
        }

        // Do body on true condition
        this.body.codegen(output);

        // Label for update in case of continue statement
        output.addLabel(updateLabel);
        // Update
        if(this.update != null) {
            for(JStatement updateStatement: update) {
                updateStatement.codegen(output);
            }
        }

        output.addBranchInstruction(GOTO,loopLabel);

        // Loop end on false condition
        output.addLabel(endLoopLabel);
        // Pop self from stack on exit
        JMember.enclosingStatement.pop();
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JForStatement:" + line, e);
        if (init != null) {
            JSONElement e1 = new JSONElement();
            e.addChild("Init", e1);
            for (JStatement stmt : init) {
                stmt.toJSON(e1);
            }
        }
        if (condition != null) {
            JSONElement e1 = new JSONElement();
            e.addChild("Condition", e1);
            condition.toJSON(e1);
        }
        if (update != null) {
            JSONElement e1 = new JSONElement();
            e.addChild("Update", e1);
            for (JStatement stmt : update) {
                stmt.toJSON(e1);
            }
        }
        if (body != null) {
            JSONElement e1 = new JSONElement();
            e.addChild("Body", e1);
            body.toJSON(e1);
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
     * Return whether statement has a continue label or not
     * @return whether statement has a continue label or not
     */
    public boolean isHasContinue() {
        return hasContinue;
    }

    /**
     * Set hasContinue to value
     * @param hasContinue - value for hasContinue
     */
    public void setHasContinue(boolean hasContinue) {
        this.hasContinue = hasContinue;
    }

    /**
     * Get label for continue
     * @return label for continue
     */
    public String getContinueLabel() {
        return continueLabel;
    }
}
