// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a do-statement.
 */
public class JDoStatement extends JStatement {
    // Body.
    private JStatement body;

    // Test expression.
    private JExpression condition;

    // Determine if Statement has a break or continue
    private boolean hasBreak, hasContinue;

    // Label for break and continue
    private String breakLabel, continueLabel;

    /**
     * Constructs an AST node for a do-statement.
     *
     * @param line      line in which the do-statement occurs in the source file.
     * @param body      the body.
     * @param condition test expression.
     */
    public JDoStatement(int line, JStatement body, JExpression condition) {
        super(line);
        this.body = body;
        this.condition = condition;
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
        condition.type().mustMatchExpected(line(),Type.BOOLEAN);
        body = (JStatement)body.analyze(context);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        String doLabel = output.createLabel();
        output.addLabel(doLabel);

        // Assign continue label to loop header
        if(this.hasContinue) {
            this.continueLabel = doLabel;
        }

        // Create break label if a break statement has been analyzed
        if(this.hasBreak) {
            this.breakLabel = output.createLabel();
        }

        // Generate code for body
        body.codegen(output);

        // Jump back to do label on true
        condition.codegen(output,doLabel,true);

        // Add label to end loop if break statement has been analyzed
        if(this.breakLabel != null) {
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
        json.addChild("JDoStatement:" + line, e);
        JSONElement e1 = new JSONElement();
        e.addChild("Body", e1);
        body.toJSON(e1);
        JSONElement e2 = new JSONElement();
        e.addChild("Condition", e2);
        condition.toJSON(e2);
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
