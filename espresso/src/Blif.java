import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Reads and writes BLIF (Berkeley Logic Interchange Format) files.
// Each .names block becomes a Function; cube lines are parsed into PCN.
public class Blif {

    private final String fileName;
    private String preamble;
    private List<Function> functions;

    /**
     * Parses a simple BLIF file into a list of functions
     * 
     * @param fileName the path to the BLIF file
     * @return a list of functions in order of their appearance
     */
    public Blif(String fileName) {
        this.fileName = fileName;
        BufferedReader reader;
        functions = new ArrayList<>();
        StringBuilder preambleStringBuilder = new StringBuilder();

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            Function currentFunction = null;

            while (line != null) {

                /* .names marks the start of a new function */
                if (line.startsWith(".names")) {

                    /* terminate previous function */
                    if (currentFunction != null) {
                        functions.add(currentFunction);
                        currentFunction = null;
                    }

                    /* parse names of inputs and output */
                    List<String> namesLine = new ArrayList<>(Arrays.asList(line.split(" ")));
                    currentFunction = new Function(namesLine.subList(1, namesLine.size() - 1),
                            namesLine.get(namesLine.size() - 1));

                    /* .end or a blank line marks the end of a function */
                } else if (line.startsWith(".end") || line.isBlank()) {
                    functions.add(currentFunction);
                    currentFunction = null;

                    /* inside a function, lines represent cubes */
                } else if (currentFunction != null) {
                    currentFunction.addCube(parseCube(line, currentFunction.inputs.size()));
                } else if (!line.startsWith("#")) {
                    preambleStringBuilder.append(line).append("\n");
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.preamble = preambleStringBuilder.toString();
    }

    private static Cube parseCube(String line, int numInputs) {

        if (!line.endsWith("1")) {
            System.out.println("Error: only on sets supported!");
            return null;
        }

        String cubeBLIF = line.split(" ")[0];

        if (cubeBLIF.length() != numInputs) {
            System.out.println("Error: length mismatch of cube " + cubeBLIF + " with number of inputs!");
            return null;
        }

        Cube pcn = new Cube(numInputs);

        /*
         * transform the string cubeBLIF into a Cube object that adheres to the
         * positional cube notation (see lecture)
         */
        for (int i = 0; i < numInputs; i++) {
            switch (line.charAt(i)) {
                case '-':
                    pcn.set(i * 2, true);
                    pcn.set(i * 2 + 1, true);
                    break;
                case '0':
                    pcn.set(i * 2, true);
                    pcn.set(i * 2 + 1, false);
                    break;
                case '1':
                    pcn.set(i * 2, false);
                    pcn.set(i * 2 + 1, true);
                    break;
                default:
                    System.out.println("Error can only pars 0, 1 and - inputs");
                    break;
            }
        }

        return pcn;
    }

    public void writeBLIF() {
        BufferedWriter writer;
        try {
            Path inputFile = Paths.get(fileName);
            Path parentFolder = inputFile.getParent();
            if (parentFolder == null) {
                parentFolder = Paths.get(".");
            }
            Path outputDir = parentFolder.resolve("output");
            Files.createDirectories(outputDir);
            writer = new BufferedWriter(new FileWriter(outputDir.resolve(inputFile.getFileName()).toString()));

            writer.write("# minimized by reverse engineered ESPRESSO\n");
            writer.write(preamble);

            for (Function function : getFunctions()) {
                writer.write(".names " + String.join(" ", function.inputs) + " " + function.output + "\n");

                for (Cube cube : function.getCubes()) {
                    writer.write(writeCube(cube));
                }
            }
            writer.write(".end\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Converts a PCN cube back to BLIF string: 11->"-", 10->"0", 01->"1"
    private static String writeCube(Cube cube) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cube.getNumInputs(); i++) {
            if (cube.get(2 * i) && cube.get(2 * i + 1)) {
                sb.append("-");
            } else if (cube.get(2 * i) && !cube.get(2 * i + 1)) {
                sb.append("0");
            } else if (!cube.get(2 * i) && cube.get(2 * i + 1)) {
                sb.append("1");
            } else {
                System.out.println("Error: can't write illegal cubes");
            }
        }
        sb.append(" 1\n");
        return sb.toString();
    }

    public List<Function> getFunctions() {
        return functions;
    }

}
