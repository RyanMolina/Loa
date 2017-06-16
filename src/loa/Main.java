package loa;


import loa.core.Game;
import loa.test.UnitTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Main {


    public static void main(String... args) {

        Game game = new Game();
        game.play();


        /**
         * Testing
         */
        Result result = JUnitCore.runClasses(UnitTest.class);

        for(Failure failure: result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }


}
