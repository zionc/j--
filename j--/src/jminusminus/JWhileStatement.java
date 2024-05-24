// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a while-statement.
 */
class JWhileStatement extends JStatement {
    // Test expression.
    private JExpression condition;

    // Body.
    private JStatement body;

    // Determine if Statement has a break or continue
    private boolean hasBreak, hasContinue;

    // Label for break and continue
    private String breakLabel, continueLabel;


    /**
     * Constructs an AST node for a while-statement.
     *
     * @param line      line in which the while-statement occurs in the source file.
     * @param condition test expression.
     * @param body      the body.
     */
    public JWhileStatement(int line, JExpression condition, JStatement body) {
        super(line);
        this.condition = condition;
        this.body = body;
        this.hasBreak = false;
        this.breakLabel = null;
    }

    /**
     * {@inheritDoc}
     */
    public JWhileStatement analyze(Context context) {
        // Push reference to self on entry
        JMember.enclosingStatement.push(this);
        condition = condition.analyze(context);
        condition.type().mustMatchExpected(line(), Type.BOOLEAN);
        body = (JStatement) body.analyze(context);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        String test = output.createLabel();
        String out = output.createLabel();
        // Assign break label to exit loop
        if(this.hasBreak) {
            this.breakLabel = out;
        }
        // Assign continue label to loop header
        if(this.hasContinue) {
            this.continueLabel = test;
        }
        output.addLabel(test);
        condition.codegen(output, out, false);
        body.codegen(output);
        output.addBranchInstruction(GOTO, test);
        output.addLabel(out);
        // Pop self from stack on exit
        JMember.enclosingStatement.pop();
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JWhileStatement:" + line, e);
        JSONElement e1 = new JSONElement();
        e.addChild("Condition", e1);
        condition.toJSON(e1);
        JSONElement e2 = new JSONElement();
        e.addChild("Body", e2);
        body.toJSON(e2);
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
