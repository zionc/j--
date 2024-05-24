import java.lang.Integer;
import java.lang.System;
import java.lang.Thread;
public class Operators2 {
    public static void ghettoAssert(boolean expected, boolean given) {
        String result = (expected == given) ? "Test Passed!" : "Test Failed!";
        System.out.println(result);
    }
    public static void main(String[] args) {
//        ghettoAssert(true,4L < 6L);
//        ghettoAssert(false, 10L < 4L);
//        ghettoAssert(true,2.0 < 4.0);
//
//        ghettoAssert(false,6.0 < 5.0);
//        ghettoAssert(true, 6L <= 6L);
//        ghettoAssert(true,4L <= 6L);
//
//        ghettoAssert(false,10L < 6L);
//        ghettoAssert(true, 6.0 <= 6.0);
//        ghettoAssert(true,4.0 <= 6.0);
//
//        ghettoAssert(false,10.0 <= 6.0);
//        ghettoAssert(false, 4L > 6L);
//        ghettoAssert(true,10L > 4L);
//
//        ghettoAssert(false, 2.0 > 4.0);
//        ghettoAssert(true,6.0 > 5.0);

        double a;
        double b = 3.0;
        System.out.println(a ^= b);
    }
}
