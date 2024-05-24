import java.lang.Integer;
import java.lang.System;

public class BreakStatement {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int count = 0;
        for (int i = 2; ; ) {
            if (i > n) {
                break;
            }
            int j = 2;
            while (j <= i / j) {
                if (i % j == 0) {
                    break;
                }
                j++;
            }
            count += j > i / j ? 1 : 0;
            i++;
        }
        System.out.println(count);


//
//        int count = 0;
//        do {
//            if(count >= n) {
//                break;
//            } else  {
//                count++;
//            }
//        } while(true);
//        System.out.println(count);
//
//        count = 0;
//        do {
//            if(count >= n/2) {
//                break;
//            } else  {
//                count++;
//            }
//        } while(true);
//        System.out.println(count);
//
//        count = 0;
//        for(int i = 0; i < n; i++) {
//            count = i;
//            if(i == n/2) {
//                break;
//            }
//        }
//        System.out.println(count);

    }
}
