
import java.lang.Integer;
import java.lang.System;

public class BooleanTest{

    public static void main(String[] args) {
        int input = Integer.parseInt(args[0]);
        boolean b = isZero(input);
        if(b) {
            System.out.println("Is Zero!");
        }
        else {
            System.out.println("Not Zero!");
        }
    }
    public static boolean isZero(int x) {
        return x==0;
    }
}