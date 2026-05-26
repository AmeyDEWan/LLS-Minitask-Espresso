import java.util.BitSet;
import java.util.stream.IntStream;

public class Cube extends BitSet {

    private final int numInputs;

    /**
     * Creates a cube with numInputs variables based on the Java BitSet class
     * @param numInputs the number of variables of the cube
     */
    public Cube(int numInputs) {
        super(numInputs * 2);
        this.numInputs = numInputs;
    }

    /**
     * Intersects the cube with another cube
     * @param other the cube to intersect with
     * @return the intersection of the cubes
     */
    public Cube intersect(Cube other) {
        Cube intersection = (Cube) this.clone();
        intersection.and(other);
        return intersection;
    }

    /**
     * Checks if a cube is legal, i.e., does not contain the 00-symbol
     * @return true, if the cube is legal
     */
    public boolean isLegal() {
        for (int i = 0; i < numInputs; i++) {
            if (!get(2 * i) && !get((2 * i) + 1))
                return false;
        }
        return true;
    }

    /**
     * @return the number of inputs of the cube
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * @return the string representation of the cube
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(numInputs * 2);
        IntStream.range(0, numInputs * 2).mapToObj(i -> get(i) ? '1' : '0').forEach(buffer::append);
        return buffer.toString();
    }
}
