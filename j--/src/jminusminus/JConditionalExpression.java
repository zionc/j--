// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a conditional expression.
 */
class JConditionalExpression extends JExpression {
    // Test expression.
    private JExpression condition;

    // Then part.
    private JExpression thenPart;

    // Else part.
    private JExpression elsePart;

    /**
     * Constructs an AST node for a conditional expression.
     *
     * @param line      line in which the conditional expression occurs in the source file.
     * @param condition test expression.
     * @param thenPart  then part.
     * @param elsePart  else part.
     */
    public JConditionalExpression(int line, JExpression condition, JExpression thenPart,
                                  JExpression elsePart) {
        super(line);
        this.condition = condition;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
    }

    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        condition = (JExpression)condition.analyze(context);
        condition.type().mustMatchExpected(line(),Type.BOOLEAN);
        thenPart = (JExpression)thenPart.analyze(context);
        elsePart = (JExpression)elsePart.analyze(context);
        thenPart.type().mustMatchExpected(line(),elsePart.type());
        type =  thenPart.type();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {

        // Create labels
        String elseLabel = output.createLabel();
        String endLabel = output.createLabel();

        // Jump to elseLabel on false
        condition.codegen(output, elseLabel, false);

        // Do then part if condition is true then jump to end label
        thenPart.codegen(output);
        output.addBranchInstruction(GOTO, endLabel);

        // Do else part if condition is false
        output.addLabel(elseLabel);
        elsePart.codegen(output);
        // End conditional expression
        output.addLabel(endLabel);
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JConditionalExpression:" + line, e);
        JSONElement e1 = new JSONElement();
        e.addChild("Condition", e1);
        condition.toJSON(e1);
        JSONElement e2 = new JSONElement();
        e.addChild("ThenPart", e2);
        thenPart.toJSON(e2);
        JSONElement e3 = new JSONElement();
        e.addChild("ElsePart", e3);
        elsePart.toJSON(e3);
    }
}
