// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.util.ArrayList;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a try-catch-finally statement.
 */
class JTryStatement extends JStatement {
    // The try block.
    private JBlock tryBlock;

    // The catch parameters.
    private ArrayList<JFormalParameter> parameters;

    // The catch blocks.
    private ArrayList<JBlock> catchBlocks;

    // The finally block.
    private JBlock finallyBlock;

    // Offset for the context for optional finally block
    private int contextFinalOffset;

    /**
     * Constructs an AST node for a try-statement.
     *
     * @param line         line in which the while-statement occurs in the source file.
     * @param tryBlock     the try block.
     * @param parameters   the catch parameters.
     * @param catchBlocks  the catch blocks.
     * @param finallyBlock the finally block.
     */
    public JTryStatement(int line, JBlock tryBlock, ArrayList<JFormalParameter> parameters,
                         ArrayList<JBlock> catchBlocks, JBlock finallyBlock) {
        super(line);
        this.tryBlock = tryBlock;
        this.parameters = parameters;
        this.catchBlocks = catchBlocks;
        this.finallyBlock = finallyBlock;
        this.contextFinalOffset = 0;
    }

    /**
     * {@inheritDoc}
     */
    public JTryStatement analyze(Context context) {
        this.tryBlock = (JBlock) tryBlock.analyze(context);

        for(int i = 0; i < catchBlocks.size(); i++) {
            // Declare new local context
            LocalContext newlocalContext = new LocalContext(context);

            // Get catch block and parameter
            JBlock catchBlock = catchBlocks.get(i);
            JFormalParameter parameter = parameters.get(i);

            // Resolve parameter type
            parameter.setType(parameter.type().resolve(context));

            // Declare parameters in new local context
            LocalVariableDefn defn = new LocalVariableDefn(parameter.type(), newlocalContext.nextOffset());
            defn.initialize();
            newlocalContext.addEntry(parameter.line(),parameter.name(),defn);

            if(parameter.type() == Type.LONG || parameter.type() == Type.DOUBLE) {
                newlocalContext.nextOffset();
            }

            // Analyze catch block in new local context
            catchBlock = (JBlock)catchBlock.analyze(newlocalContext);

        }

        if(finallyBlock != null) {
            LocalContext localContext = new LocalContext(context); // Create local context for finally block
            finallyBlock = (JBlock)finallyBlock.analyze(localContext);  // Analyze optional finally block in new context
            this.contextFinalOffset = localContext.offset();
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        // Labels
        String startTry = output.createLabel();
        String endTry   = output.createLabel();
        String endFinally   = output.createLabel();
        String startFinally = output.createLabel();
        ArrayList<ArrayList<String>> catchLabels = new ArrayList<>();

        // Add start try label
        output.addLabel(startTry);

        // generate code for try block
        this.tryBlock.codegen(output);

        // generate code for optional finally block
        if(finallyBlock != null) {
            finallyBlock.codegen(output);
        }
        output.addBranchInstruction(GOTO,endFinally);


        // end try label
        output.addLabel(endTry);

        for(int i = 0; i < catchBlocks.size(); i++) {
            JBlock catchBlock = catchBlocks.get(i);
            ArrayList<String> labels = new ArrayList<>();

            // Add start catch label
            String startCatchLabel = output.createLabel();
            output.addLabel(startCatchLabel);

            // Get catch variable
            JFormalParameter param = this.parameters.get(i);
            String paramJVM = param.type().jvmName();

            // Store reference to variable
            output.addNoArgInstruction(ASTORE_1);

            // Generate code for catch block
            catchBlock.codegen(output);

            // Add end catch label
            String endCatchLabel = output.createLabel();
            output.addLabel(endCatchLabel);

            // Add exception handler
            output.addExceptionHandler(startTry,endTry,startCatchLabel,paramJVM);

            // Generate code for optional finally block
            if(finallyBlock!= null) {
                finallyBlock.codegen(output);
            }

            // add labels
            labels.add(startCatchLabel);
            labels.add(endCatchLabel);
            catchLabels.add(labels);

            output.addBranchInstruction(GOTO,endFinally);

        }

        if(finallyBlock != null) {
            output.addLabel(startFinally);
            output.addOneArgInstruction(ASTORE,this.contextFinalOffset);

            String fPlusOne = output.createLabel();
            output.addLabel(fPlusOne);

            finallyBlock.codegen(output);
            output.addOneArgInstruction(ALOAD,this.contextFinalOffset);
            output.addNoArgInstruction(ATHROW);

            // Add exception handlers
            output.addExceptionHandler(startTry,endTry,startFinally,null);
            for(int i = 0; i < catchBlocks.size(); i++) {
                output.addExceptionHandler(catchLabels.get(i).get(0), // start catch
                        catchLabels.get(i).get(1), // end catch
                        startFinally,
                        null);
            }
            output.addExceptionHandler(startFinally,fPlusOne,startFinally,null);
        }

        output.addLabel(endFinally);

    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JTryStatement:" + line, e);
        JSONElement e1 = new JSONElement();
        e.addChild("TryBlock", e1);
        tryBlock.toJSON(e1);
        if (catchBlocks != null) {
            for (int i = 0; i < catchBlocks.size(); i++) {
                JFormalParameter param = parameters.get(i);
                JBlock catchBlock = catchBlocks.get(i);
                JSONElement e2 = new JSONElement();
                e.addChild("CatchBlock", e2);
                String s = String.format("[\"%s\", \"%s\"]", param.name(), param.type() == null ?
                        "" : param.type().toString());
                e2.addAttribute("parameter", s);
                catchBlock.toJSON(e2);
            }
        }
        if (finallyBlock != null) {
            JSONElement e2 = new JSONElement();
            e.addChild("FinallyBlock", e2);
            finallyBlock.toJSON(e2);
        }
    }
}
