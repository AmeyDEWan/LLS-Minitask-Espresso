import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Reduce {
    private final int numInputs;

    public Reduce(int numInputs) {
        this.numInputs = numInputs;
    }

    public List<int[]> reduceFunction(final List<int[]> cover) {
        List<int[]> myCover = new LinkedList<>();

        for (int[] cube : cover) {
            int[] newCube = cube.clone();
            myCover.add(newCube);
        }

        int[] order = getOrder(myCover);

        return reduce(myCover, order);
    }

    private int[] getOrder(final List<int[]> cover) {
        int[] result = new int[cover.size()];
        int[] columnVector = new int[2 * (this.numInputs)];
        int[] weight = new int[cover.size()];
        ArrayList<Integer> order = new ArrayList<>();

        Arrays.fill(columnVector, 0);
        Arrays.fill(weight, 0);

        // Get the column vector as sum of columns of PCN
        for (int[] cube : cover) {
            for (int i = 0; i < this.numInputs; i++) {
                columnVector[2 * i] += ((cube[i] & 2) >> 1); // neat
                columnVector[2 * i + 1] += (cube[i] & 1);
            }
        }

        // calculate weight
        for (int i = 0; i < cover.size(); i++) {
            for (int j = 0; j < this.numInputs; j++) {
                // column = {313131}
                // cube[i] = {2 3 1} = {10 11 01} = {10 -> 1 * 3, 0 * 1}
                weight[i] += (((cover.get(i)[j] >> 1) & 1) * (columnVector[2 * j]));
                weight[i] += ((cover.get(i)[j] & 1) * (columnVector[2 * j + 1]));
            }
            order.add(i);
        }

        // sort in descending order
        order.sort((i, j) -> weight[j] - weight[i]);
        int idx = 0;
        for (Integer integer : order) {
            result[idx++] = integer;
        }

        return result;

    }

    private List<int[]> reduce(List<int[]> cover, final int[] order) {
        return cover;
    }
}
