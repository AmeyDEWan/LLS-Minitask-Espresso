import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /* handle BLIF file argument */
        if (args[0] == null || args[0].isEmpty())
            System.out.println("No BLIF file given.");

        System.out.println("Processing " + args[0]);

        /* parse BLIF */
        Blif blif = new Blif(args[0]);

        List<Function> functions = blif.getFunctions();

        functions.forEach(System.out::println);

        // implement espresso

        blif.writeBLIF();
    }
}
