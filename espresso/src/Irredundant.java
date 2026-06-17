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
        // boolean[] marked = new boolean[cover.size()];
        // Arrays.fill(marked, false);
        List<int[]> irredundantCover = new LinkedList<>(cover);

        for (int i = 0; i < irredundantCover.size(); i++) {
            int[] cube = irredundantCover.get(i);
            List<int[]> newCover = new LinkedList<>();

            // omit cube[indexToOmit]
            for (int j = 0; j < irredundantCover.size(); j++) {
                if (i == j)
                    continue;
                newCover.add(irredundantCover.get(j));
            }
            List<int[]> surroundingCoverComplement = coverComplement(newCover);

            if (checkIntersect(cube, surroundingCoverComplement) == false) {
                // no essential minterms => redundant
                // mark for deletion
                irredundantCover.remove(i);
                i--;
                // marked[i] = true;
            }
        }
        // List<int[]> irreduantCover = new LinkedList<>();
        // for (int i = 0; i < marked.length; i++) {
        // if (!marked[i]) {
        // irreduantCover.add(cover.get(i));
        // }
        // }

        return irredundantCover;
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
