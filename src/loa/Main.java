package loa;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;


import loa.core.Game;

public class Main {

    public static final String VERSION = "69";

    public static void main(String... args) {


        Game game = new Game();
        game.play();
    }


    /** Report an error and exit program with EXIT as the
     *  exit code if _strict is false; otherwise exit with code 2.
     *  FORMAT is the message format (as for printf), and ARGS any
     *  additional arguments. */
    public static void error(int exit, String format, Object... args) {
        error(format, args);
        System.exit(exit);
    }

    /** Report an error.  If _strict, then exit (code 2).  Otherwise,
     *  simply return. FORMAT is the message format (as for printf),
     *  and ARGS any additional arguments. */
    public static void error(String format, Object... args) {
        System.err.print("Error: ");
        System.err.printf(format, args);
    }

}
