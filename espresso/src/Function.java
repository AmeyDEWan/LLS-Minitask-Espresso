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
