import java.lang.Integer;
import java.lang.System;

public class Operators {
    public static void ghettoAssert(boolean expected, boolean given) {
        if(expected == given) {
            System.out.println("Test passed!");
        } else {
            System.out.println("Test failed!");
        }
    }
    public static void main(String[] args) {
//        int a = Integer.parseInt(args[0]);
//        int b = Integer.parseInt(args[1]);
//        System.out.println(a != b);
//        System.out.println(a /= b);
//        System.out.println(a -= b);
//        System.out.println(a *= b);
//        System.out.println(a %= b);
//        System.out.println(a >>= b);
//        System.out.println(a >>>= b);
//        System.out.println(a >= b);
//        System.out.println(a <<= b);
//        System.out.println(a < b);
//        System.out.println(a ^= b);
//        System.out.println(a |= b);
//        System.out.println(a == b || b == a);
//        System.out.println(a &= b);
//        System.out.println(a++);
//        System.out.println(--b);
//        System.out.println(a / b);
//        System.out.println(a % b);
//        System.out.println(a << b);
//        System.out.println(a >> b);
//        System.out.println(a >>> b);
//        System.out.println(~a);
//        System.out.println(a | b);
//        System.out.println(a ^ b);
//        System.out.println(a & b);
//        System.out.println(+a);

        ghettoAssert(true,4L < 6L);
        ghettoAssert(false, 10L < 4L);
        ghettoAssert(true,2.0 < 4.0);

        ghettoAssert(false,6.0 < 5.0);
        ghettoAssert(true, 6L <= 6L);
        ghettoAssert(true,4L <= 6L);

        ghettoAssert(false,10L < 6L);
        ghettoAssert(true, 6.0 <= 6.0);
        ghettoAssert(true,4.0 <= 6.0);

        ghettoAssert(false,10.0 <= 6.0);
        ghettoAssert(false, 4L > 6L);
        ghettoAssert(true,10L > 4L);

        ghettoAssert(false, 2.0 > 4.0);
        ghettoAssert(true,6.0 > 5.0);




    }
}
