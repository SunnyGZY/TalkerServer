import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class test5 {

    @Test
    public void test() {

        int num = 12;

        for (int i = num - 1; i > 1; i--) {
            if (num % i == 0) {
                System.out.println(String.valueOf(num) + "不是素数。");
                return;
            }
        }

        System.out.println(String.valueOf(num) + "是素数。");
    }
}
