// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.GOTO;

/**
 * An AST node for a break-statement.
 */
public class JBreakStatement extends JStatement {

    // Surrounding control flow statement (do, while, switch, for)
    private JStatement enclosingStatement;
    /**
     * Constructs an AST node for a break-statement.
     *
     * @param line line in which the break-statement occurs in the source file.
     */
    public JBreakStatement(int line) {
        super(line);
    }

    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        this.enclosingStatement = JMember.enclosingStatement.peek();

        //TODO - clean up, introduce another interface for polymorphism?
        if(enclosingStatement instanceof JDoStatement) {
            ((JDoStatement)enclosingStatement).setHasBreak(true);
        } else if (enclosingStatement instanceof JForStatement) {
            ((JForStatement)enclosingStatement).setHasBreak(true);
        } else if (enclosingStatement instanceof JWhileStatement) {
            ((JWhileStatement)enclosingStatement).setHasBreak(true);
        } else if (enclosingStatement instanceof JSwitchStatement) {
            ((JSwitchStatement)enclosingStatement).setHasBreak(true);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        // Get exit label
        String exitLabel = "";
        //TODO - clean up, introduce another interface for polymorphism?
        if(enclosingStatement instanceof JDoStatement) {
            exitLabel = ((JDoStatement)enclosingStatement).getBreakLabel();
        } else if (enclosingStatement instanceof JForStatement) {
            exitLabel = ((JForStatement)enclosingStatement).getBreakLabel();
        } else if (enclosingStatement instanceof JWhileStatement) {
            exitLabel = ((JWhileStatement)enclosingStatement).getBreakLabel();
        } else if (enclosingStatement instanceof JSwitchStatement) {
            exitLabel = ((JSwitchStatement) enclosingStatement).getBreakLabel();
        }


        // Unconditional jump to control flow statement's exit
        output.addBranchInstruction(GOTO, exitLabel);
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JBreakStatement:" + line, e);
    }
}
