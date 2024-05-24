import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Math;
import java.lang.NumberFormatException;
import java.lang.System;

public class Test {

        public static void main(String[] args) {
//            try {
//                int x = Integer.parseInt(args[0]);
//                int result = sqrt(x);
//                System.out.println(result);
//            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("x not specified");
//            } catch (NumberFormatException e) {
//                System.out.println("x must be a double");
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage());
////            System.out.println("Test");
//            }
//            finally {
//                System.out.println("Done!");
//            }
            sqrt(5.0);
        }

        private static double sqrt(double x) throws IllegalArgumentException {
            if (x < 0.0) {
                throw new IllegalArgumentException("x must be positve");
            }
            return 3.0;
        }


}