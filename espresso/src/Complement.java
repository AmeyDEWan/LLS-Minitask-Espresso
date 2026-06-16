import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Complement {
    private final List<int[]> cover;
    private final int numInputs;

    public Complement(List<int[]> cover, int numInputs) {
        this.cover = cover;
        this.numInputs = numInputs;
    }

    public List<int[]> getComplement(List<int[]> cover) {
        int[] order = getOrder();
        return getComplement(cover, order, 0);
    }

    private List<int[]> getComplement(List<int[]> cover, int[] order, int index) {

        // base conditions

        if (cover.isEmpty()) {
            List<int[]> universalCubeCover = new LinkedList<>();
            int[] pcnCube = new int[numInputs];
            Arrays.fill(pcnCube, 3);
            universalCubeCover.add(pcnCube);
            return universalCubeCover;
        }

        if (isUniversalCube(cover) == 1) {
            List<int[]> emptyList = new LinkedList<>();
            return emptyList;
        }

        if (cover.size() == 1) {
            return getDeMorgan(cover.get(0));
        }

        int splitVar = order[index];

        List<int[]> positiveCofactor = getComplement(getPositiveCofactor(cover, splitVar), order, index + 1);
        List<int[]> negativeCofactor = getComplement(getNegativeCofactor(cover, splitVar), order, index + 1);

        for (int[] cube : positiveCofactor) {
            cube[splitVar] = 1;
        }

        for (int[] cube : negativeCofactor) {
            cube[splitVar] = 2;
        }

        List<int[]> complementedCover = mergeList(positiveCofactor, negativeCofactor);

        return complementedCover;
    }

    private int isUniversalCube(List<int[]> cover) {
        for (int[] cube : cover) {
            int isUniversal = 1;
            for (int i : cube) {
                if (i != 3) {
                    isUniversal = 0;
                    break;
                }
            }
            if (isUniversal == 1) {
                return 1;
            }
        }
        return 0;
    }

    private List<int[]> getDeMorgan(int[] cube) {
        List<int[]> complemenCover = new LinkedList<>();

        for (int i = 0; i < numInputs; i++) {
            if (cube[i] == 1 || cube[i] == 2) {
                int[] newCube = new int[numInputs];
                Arrays.fill(newCube, 3);

                newCube[i] = (cube[i] == 1 ? 2 : 1);
                complemenCover.add(newCube);
            }
        }

        return complemenCover;
    }

    private List<int[]> getPositiveCofactor(List<int[]> cover, int supportVar) {
        List<int[]> returnList = new LinkedList<>();

        for (int[] cube : cover) {
            if (cube[supportVar] == 2) {
                continue;
            }

            int[] newCube = cube.clone(); // deep copy and not reference
            if (cube[supportVar] == 1) {
                newCube[supportVar] = 3;
            }
            returnList.add(newCube);
        }
        return returnList;
    }

    private List<int[]> getNegativeCofactor(List<int[]> cover, int supportVar) {
        List<int[]> returnList = new LinkedList<>();

        for (int[] cube : cover) {
            if (cube[supportVar] == 1) {
                continue;
            }

            int[] newCube = cube.clone(); // deep copy and not reference
            if (cube[supportVar] == 2) {
                newCube[supportVar] = 3;
            }
            returnList.add(newCube);
        }
        return returnList;
    }

    private int[] getOrder() {
        int[] order = new int[this.numInputs];
        int[][] counts = new int[numInputs][2];
        boolean[] isBinate = new boolean[numInputs];
        ArrayList<Integer> binate = new ArrayList<>();

        for (int[] cube : this.cover) {
            for (int i = 0; i < numInputs; i++) {
                if (cube[i] == 1) counts[i][0]++;
                else if (cube[i] == 2) counts[i][1]++;
            }
        }

        for (int i = 0; i < numInputs; i++) {
            if (counts[i][0] > 0 && counts[i][1] > 0) {
                isBinate[i] = true;
                binate.add(i);
            }
        }

        binate.sort((i, j) -> Math.min(counts[j][0], counts[j][1]) - Math.min(counts[i][0], counts[i][1]));

        int idx = 0;
        for (int v : binate) order[idx++] = v;
        for (int i = 0; i < numInputs; i++) {
            if (!isBinate[i]) order[idx++] = i;
        }

        return order;
    }

    private List<int[]> mergeList(List<int[]> positiveCofactor, List<int[]> negativeCofactor) {
        // if (positiveCofactor.isEmpty()) {
        // return negativeCofactor;
        // }
        // if (negativeCofactor.isEmpty()) {
        // return positiveCofactor;
        // }

        List<int[]> mergedList = new LinkedList<>(positiveCofactor);
        mergedList.addAll(negativeCofactor);

        return mergedList;
    }

}
