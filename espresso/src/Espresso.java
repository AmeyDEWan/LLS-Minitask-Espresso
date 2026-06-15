import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Espresso {

    /* private member functions */
    private final List<int[]> cover;
    private final List<int[]> complementCover;
    private final int numInputs;

    int EMPTY = 0;
    int TRUE = 1;
    int FALSE = 2;
    int DC = 3;

    public Espresso(List<Function> functions) {
        this.cover = new LinkedList<>();
        this.numInputs = functions.get(0).getNumInputs();

        for (Function function : functions) {
            for (Cube cube : function.getCubes()) {
                int[] pcnCube = new int[this.numInputs];
                for (int i = 0; i < numInputs; i++) {
                    pcnCube[i] = ((cube.get(2 * i) ? 1 : 0) << 1) | (cube.get(2 * i + 1) ? 1 : 0);
                }
                this.cover.add(pcnCube);
            }
        }
        Complement comp = new Complement(this.cover, this.numInputs);
        int[] order = getOrder();
        this.complementCover = comp.getComplement(this.cover, order, 0);
    }

    public void printCover() {
        System.out.println("Normal Cover : ");
        for (int[] cube : this.cover) {
            System.out.println(Arrays.toString(cube));
        }
    }

    public void printComplementCover() {
        System.out.println("Complemented Cover : ");
        for (int[] cube : this.complementCover) {
            System.out.println(Arrays.toString(cube));
        }
    }

    private int[] getOrder() {
        int[] order = new int[this.numInputs];
        int[][] counts = new int[numInputs][2];
        boolean[] isBinate = new boolean[numInputs];
        ArrayList<Integer> binate = new ArrayList<>();

        for (int[] cube : this.cover) {
            for (int i = 0; i < numInputs; i++) {
                if (cube[i] == TRUE)
                    counts[i][0]++;
                else if (cube[i] == FALSE)
                    counts[i][1]++;
            }
        }

        for (int i = 0; i < numInputs; i++) {
            if (counts[i][0] > 0 && counts[i][1] > 0) {
                isBinate[i] = true;
                binate.add(i);
            }
        }

        // split at most stable (where TRUE count, FALSE count are closest => better
        // balanced tree)
        // sort in descending order. whichever with highest min, goes first
        binate.sort((i, j) -> Math.min(counts[i][0], counts[i][1]) - Math.min(counts[j][0], counts[j][1]));

        int idx = 0;
        // fill binate first
        for (int v : binate) {
            order[idx++] = v;
        }
        for (int i = 0; i < numInputs; i++) {
            if (!isBinate[i])
                order[idx++] = i;
        }

        return order;
    }

}
