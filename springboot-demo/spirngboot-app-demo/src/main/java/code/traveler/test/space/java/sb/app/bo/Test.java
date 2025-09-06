package code.traveler.test.space.java.sb.app.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {

    public static void main(String[] args){
        Random random = new Random();
//        for (int i = 0; i < 20; i++) {
//
//            System.out.println(random.nextInt(5));
//        }
        List a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        System.out.println(a.get(3));
    }
}
