import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// Expand: tries to raise each literal in each cube to Don't Care (3)
// without hitting the complement cover, making cubes as large as possible.
public class Expand {
    private final int numInputs;

    public Expand(int numInputs) {
        this.numInputs = numInputs;
    }

    public List<int[]> expandFunction(final List<int[]> cover, final List<int[]> complementCover) {
        List<int[]> myCover = new LinkedList<>();

        for (int[] cube : cover) {
            int[] newCube = cube.clone();
            myCover.add(newCube);
        }

        int[] order = getOrder(myCover);

        return expandCover(myCover, order, complementCover);
    }

    private List<int[]> expandCover(List<int[]> cover, final int[] order, final List<int[]> complementCover) {

        for (int i : order) {
            int[] cube = cover.get(i);

            for (int j = 0; j < this.numInputs; j++) {
                if (cube[j] == 3)
                    continue;
                int temp = cube[j];
                cube[j] = 3; // try expanding this literal to DC
                if (checkIntersect(cube, complementCover) == true) {
                    cube[j] = temp; // expansion would cover 0-minterms, revert
                }
            }
        }
        return cover;
    }

    private boolean checkIntersect(final int[] cube, final List<int[]> complementCover) {
        // check if cube intersect with any cube of complement cover
        for (int[] compCube : complementCover) {
            boolean intersects = true;
            for (int i = 0; i < this.numInputs; i++) {
                if ((cube[i] & compCube[i]) == 0) {
                    intersects = false;
                    break;
                }
            }
            if (intersects)
                return true;
        }
        return false;
    }

    // Order cubes by ascending weight so the most-constrained cubes expand first.
    // column vector = (in binary format) sum up all the TRUEs
    // row vector = binary representation of cube
    // Weight = dot product (column vector, row vector)
    private int[] getOrder(final List<int[]> cover) {
        int[] result = new int[cover.size()];
        int[] columnVector = new int[2 * (this.numInputs)];
        int[] weights = new int[cover.size()];
        ArrayList<Integer> order = new ArrayList<>();

        Arrays.fill(columnVector, 0);
        Arrays.fill(weights, 0);

        // Get the column vector as sum of columns of PCN
        for (int[] cube : cover) {
            for (int i = 0; i < this.numInputs; i++) {
                columnVector[2 * i] += ((cube[i] & 2) >> 1); // neat
                columnVector[2 * i + 1] += (cube[i] & 1);
            }
        }

        // calculate weights
        for (int i = 0; i < cover.size(); i++) {
            for (int j = 0; j < this.numInputs; j++) {
                // column = {313131}
                // cube[i] = {2 3 1} = {10 11 01} = {10 -> 1 * 3, 0 * 1}
                weights[i] += (((cover.get(i)[j] >> 1) & 1) * (columnVector[2 * j]));
                weights[i] += ((cover.get(i)[j] & 1) * (columnVector[2 * j + 1]));
            }
            order.add(i);
        }

        order.sort((i, j) -> weights[i] - weights[j]);
        int index = 0;
        for (Integer integer : order) {
            result[index++] = integer;
        }

        return result;

    }
}
