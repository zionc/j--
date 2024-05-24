import java.util.ArrayList;

import jminusminus.CLEmitter;

import static jminusminus.CLConstants.*;

/**
 * This class programmatically generates the class file for the following Java application:
 * 
 * <pre>
 * public class IsPrime {
 *     // Entry point.
 *     public static void main(String[] args) {
 *         int n = Integer.parseInt(args[0]);
 *         boolean result = isPrime(n);
 *         if (result) {
 *             System.out.println(n + " is a prime number");
 *         } else {
 *             System.out.println(n + " is not a prime number");
 *         }
 *     }
 *
 *     // Returns true if n is prime, and false otherwise.
 *     private static boolean isPrime(int n) {
 *         if (n < 2) {
 *             return false;
 *         }
 *         for (int i = 2; i <= n / i; i++) {
 *             if (n % i == 0) {
 *                 return false;
 *             }
 *         }
 *         return true;
 *     }
 * }
 * </pre>
 */
public class GenIsPrime {
    public static void main(String[] args) {
        CLEmitter e = new CLEmitter(true);
        ArrayList<String> modifiers = new ArrayList<String>();

        // Load the class
        // -------------------------------------------------------------------------------------

        // public class IsPrime {
        modifiers.add("public");
        e.addClass(modifiers,"IsPrime","java/lang/Object",null,true);
        modifiers.clear();

        // Load main method
        // -------------------------------------------------------------------------------------

        // public static void main(String[] args) {
        modifiers.add("public");
        modifiers.add("static");
        e.addMethod(modifiers,"main","([Ljava/lang/String;)V",null,true);
        modifiers.clear();


        // int n = Integer.parseInt(args[0]);
        e.addNoArgInstruction(ALOAD_0);
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(AALOAD);
        e.addMemberAccessInstruction(INVOKESTATIC,"java/lang/Integer","parseInt","(Ljava/lang/String;)I");
        e.addNoArgInstruction(ISTORE_1);        // 1: n

        // boolean result = isPrime(n);
        // boolean isPrime(int n);
        e.addNoArgInstruction(ILOAD_1);
        e.addMemberAccessInstruction(INVOKESTATIC,"IsPrime","isPrime","(I)Z");
        e.addNoArgInstruction(ISTORE_2);        // 2: result
        e.addNoArgInstruction(ILOAD_2);         // Push result to top of stack

        // if(!result) -> branch to notprime
        e.addBranchInstruction(IFEQ,"notprime");

        // if(result)
        // System.out
        e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // sb = new StringBuffer();
        e.addReferenceInstruction(NEW, "java/lang/StringBuffer");
        e.addNoArgInstruction(DUP);
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/StringBuffer", "<init>", "()V");

        // sb.append(n);
        e.addNoArgInstruction(ILOAD_1);
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append",
                "(I)Ljava/lang/StringBuffer;");

        // sb.append(" is a prime number");
        e.addLDCInstruction(" is a prime number");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuffer;");

        // System.out.println(sb.toString());
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                "toString", "()Ljava/lang/String;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V");

        e.addNoArgInstruction(RETURN);
        // end branch ----------------------------------------------------------------


        e.addLabel("notprime");
        // System.out
        e.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // sb = new StringBuffer();
        e.addReferenceInstruction(NEW, "java/lang/StringBuffer");
        e.addNoArgInstruction(DUP);
        e.addMemberAccessInstruction(INVOKESPECIAL, "java/lang/StringBuffer", "<init>", "()V");

        // sb.append(n);
        e.addNoArgInstruction(ILOAD_1);
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append",
                "(I)Ljava/lang/StringBuffer;");

        // sb.append(" is not a prime number");
        e.addLDCInstruction(" is not a prime number");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuffer;");

        // System.out.println(sb.toString());
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/lang/StringBuffer",
                "toString", "()Ljava/lang/String;");
        e.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V");
        e.addNoArgInstruction(RETURN);
        // end branch ----------------------------------------------------------------


        // private static boolean isPrime(int n) {
        modifiers.add("private");
        modifiers.add("static");
        e.addMethod(modifiers,"isPrime","(I)Z",null,true);
        modifiers.clear();

        // load value of n then 2 on the stack
        e.addNoArgInstruction(ILOAD_0);
        e.addNoArgInstruction(ICONST_2);

        // if n >= 2 goto A:
        e.addBranchInstruction(IF_ICMPGE,"A");

        // base case: if (n < 2) return false;
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(IRETURN);

        // A: int i = 2
        e.addLabel("A");
        e.addNoArgInstruction(ICONST_2);
        e.addNoArgInstruction(ISTORE_1);

        // D: if n / i < i goto B :
        e.addLabel("D");
        e.addNoArgInstruction(ILOAD_0);
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(IDIV);
        e.addNoArgInstruction(ILOAD_1);
        e.addBranchInstruction(IF_ICMPLT,"B");

        // if n % i != 0 goto C:
        e.addNoArgInstruction(ILOAD_0);
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(IREM);
        e.addBranchInstruction(IFNE,"C");

        // return false;
        e.addNoArgInstruction(ICONST_0);
        e.addNoArgInstruction(IRETURN);
        
        // C: increment i by 1, goto D
        e.addLabel("C");
        e.addNoArgInstruction(ICONST_1);
        e.addNoArgInstruction(ILOAD_1);
        e.addNoArgInstruction(IADD);
        e.addNoArgInstruction(ISTORE_1);
        e.addBranchInstruction(GOTO,"D");

        // B: return true;
        e.addLabel("B");
        e.addNoArgInstruction(ICONST_1);
        e.addNoArgInstruction(IRETURN);

        // write IsPrime.class
        e.write();
    }
}
