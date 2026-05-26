import java.util.*;

public class Function {

    final List<String> inputs;
    final String output;

    private final List<Cube> function;

    /**
     * Creates an empty function with input and output names
     * @param inputs list of input variable names
     * @param output output variable name
     */
    public Function(List<String> inputs, String output) {
        this.inputs = inputs;
        this.output = output;
        this.function = new LinkedList<>();
    }

    /**
     * Creates an empty function with input and output names of a template function
     * @param template function to copy input and output names from
     */
    public Function(Function template) {
        this.inputs = template.inputs;
        this.output = template.output;
        this.function = new LinkedList<>();
    }

    /**
     * Parses a line from the BLIF representation of the function, transforms it
     * into positional cube notation and adds the cube to the function.
     * @param line line of a .names-block
     */
    public void addLine(String line) {

        if (!line.endsWith("1")) {
            System.out.println("Error: only on sets supported!");
            return;
        }

        String cubeBLIF = line.split(" ")[0];

        if (cubeBLIF.length() != inputs.size()) {
            System.out.println("Error: length mismatch of cube " + cubeBLIF + " with number of inputs!");
            return;
        }

        Cube pcn = new Cube(inputs.size());

        /***** YOUR CODE HERE *****/
        /* transform the string cubeBLIF into a Cube object that adheres to the positional cube notation (see lecture) */
        for(int i = 0; i < inputs.size(); i++){
            switch(line.charAt(i)){
                case '-':
                    pcn.set(i*2, true);
                    pcn.set(i*2 +1, true);
                    break;
                case '0':
                    pcn.set(i*2, true);
                    pcn.set(i*2 +1, false);
                    break;
                case '1':
                    pcn.set(i*2, false);
                    pcn.set(i*2 +1, true);
                    break;
                default:
                    System.out.println("Error can only pars 0, 1 and - inputs");
                    break;
            }
        }

        /***** END CODE *****/

        function.add(pcn);
    }

    /**
     * Adds a cube to the function
     * @param cube to add
     */
    public void addCube(Cube cube) {
        function.add(cube);
    }

    /**
     * @return a list of the cubes of the function
     */
    public List<Cube> getCubes() {
        return function;
    }

    /**
     * @return a string representation of the function
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        inputs.forEach(i -> sb.append(i).append(" "));
        sb.append("-> " + output);
        sb.append("\n");

        for (Cube pcn : function) {
            sb.append(pcn.toString()).append("\n");
        }

        return sb.toString();
    }
}
