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

        // implement espresso
        Espresso esp = new Espresso(functions);

        List<int[]> minimizedCover = esp.getMinimizedCover();

        // functions is always a single entry
        functions.get(0).ListToPCN(minimizedCover);

        blif.writeBLIF();
    }
}
