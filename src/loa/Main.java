package loa;


import loa.core.Game;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Main {


    public static void main(String... args) {
/*        Game game = new Game();

        game.play();*/

        Result result = JUnitCore.runClasses(UnitTest.class);

        for(Failure failure: result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

        Set<Point> set = new HashSet<>();

        Point pointA = new Point(1, 2);
        Point pointB = new Point(0 , 1) ;
        Point pointC = new Point(1, 2);
        set.add(pointA);
        set.add(pointB);
        set.add(pointC);
        System.out.println(set.size());
    }

}
