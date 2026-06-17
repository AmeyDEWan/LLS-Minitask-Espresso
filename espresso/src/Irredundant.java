import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Irredundant {
    private final int numInputs;

    public Irredundant(int numInputs) {
        this.numInputs = numInputs;
    }

    public List<int[]> getIrredundant(final List<int[]> cover) {
        List<int[]> myCover = new LinkedList<>();

        for (int[] cube : cover) {
            myCover.add(cube);
        }

        return irredundant(myCover);
    }

    private List<int[]> irredundant(List<int[]> cover) {
        boolean[] marked = new boolean[cover.size()];
        Arrays.fill(marked, false);

        for (int i = 0; i < cover.size(); i++) {
            int[] cube = cover.get(i);
            List<int[]> newCover = new LinkedList<>();

            // omit cube[indexToOmit]
            for (int j = 0; j < cover.size(); j++) {
                if (i == j)
                    continue;
                newCover.add(cover.get(j));
            }
            List<int[]> surroundingCoverComplement = coverComplement(newCover);

            if (!checkIntersect(cube, surroundingCoverComplement)) {
                // no essential minterms => redundant
                // mark for deletion
                marked[i] = true;
            }
        }
        List<int[]> irreduantCover = new LinkedList<>();
        for (int i = 0; i < marked.length; i++) {
            if (!marked[i]) {
                irreduantCover.add(cover.get(i));
            }
        }

        return irreduantCover;
    }

    private List<int[]> coverComplement(final List<int[]> cover) {

        Complement comp = new Complement(this.numInputs);
        List<int[]> compNewCover = comp.getComplement(cover);

        return compNewCover;
    }

    private boolean checkIntersect(final int[] cube, final List<int[]> cover) {
        for (int[] cube_F : cover) {
            boolean intersect = true;
            for (int i = 0; i < this.numInputs; i++) {
                if ((cube[i] & cube_F[i]) == 0) {
                    intersect = false;
                    break;
                }
            }
            if (intersect) {
                return true;
            }
        }
        return false;
    }

}
