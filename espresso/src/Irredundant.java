import java.util.LinkedList;
import java.util.List;

// Removes redundant cubes from a cover.
// A cube is redundant if all its minterms are already covered by the other cubes.
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
        List<int[]> irredundantCover = new LinkedList<>(cover);

        for (int i = 0; i < irredundantCover.size(); i++) {
            int[] cube = irredundantCover.get(i);
            List<int[]> newCover = new LinkedList<>();

            // Build cover without the current cube
            for (int j = 0; j < irredundantCover.size(); j++) {
                if (i == j)
                    continue;
                newCover.add(irredundantCover.get(j));
            }
            // If cube doesn't intersect the complement of the rest, it adds no unique
            // minterms
            List<int[]> surroundingCoverComplement = coverComplement(newCover);

            if (checkIntersect(cube, surroundingCoverComplement) == false) {
                // cube is redundant — remove it
                irredundantCover.remove(i);
                i--;
            }
        }
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
