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
        int[] pos = new int[numInputs]; // count of TRUE (1) appearances per variable
        int[] neg = new int[numInputs]; // count of FALSE (2) appearances per variable

        for (int[] cube : cover) {
            for (int i = 0; i < numInputs; i++) {
                if (cube[i] == 1)
                    pos[i]++;
                else if (cube[i] == 2)
                    neg[i]++;
            }
        }

    }

}
