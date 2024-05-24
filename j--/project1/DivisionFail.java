import java.lang.System;

public class DivisionFail {
    public static void main(String[] args) {
        System.out.println(42 / 5);
        System.out.println("42" / 5);
        System.out.println(42 / "5");
        System.out.println(60 / 3);
        System.out.println("42" / "5");
    }
}
