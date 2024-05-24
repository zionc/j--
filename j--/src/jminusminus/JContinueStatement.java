// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.GOTO;

/**
 * An AST node for a continue-statement.
 */
public class JContinueStatement extends JStatement {

    private JStatement enclosingStatement;
    /**
     * Constructs an AST node for a continue-statement.
     *
     * @param line line in which the continue-statement occurs in the source file.
     */
    public JContinueStatement(int line) {
        super(line);
    }

    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        this.enclosingStatement = JMember.enclosingStatement.peek();

        if(enclosingStatement instanceof JDoStatement) {
            ((JDoStatement)enclosingStatement).setHasContinue(true);
        } else if (enclosingStatement instanceof JForStatement) {
            ((JForStatement)enclosingStatement).setHasContinue(true);
        } else if (enclosingStatement instanceof JWhileStatement) {
            ((JWhileStatement)enclosingStatement).setHasContinue(true);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        // Get exit label
        String continueLabel = "";
        //TODO - clean up, introduce another interface for polymorphism?
        if(enclosingStatement instanceof JDoStatement) {
            continueLabel = ((JDoStatement)enclosingStatement).getContinueLabel();
        } else if (enclosingStatement instanceof JForStatement) {
            continueLabel = ((JForStatement)enclosingStatement).getContinueLabel();
        } else if (enclosingStatement instanceof JWhileStatement) {
            continueLabel = ((JWhileStatement)enclosingStatement).getContinueLabel();
        }

        // Unconditional jump to loop header
        output.addBranchInstruction(GOTO,continueLabel);
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JContinueStatement:" + line, e);
    }
}
