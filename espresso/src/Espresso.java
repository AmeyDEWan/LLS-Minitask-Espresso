import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Espresso {

    /* private member functions */
    private final List<int[]> cover;
    private final List<int[]> complementCover;
    // private final List<int[]> expandedCover;
    private final int numInputs;

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
        Complement comp = new Complement(this.numInputs);
        Expand expand = new Expand(this.numInputs);
        Irredundant irrd = new Irredundant(this.numInputs);
        Reduce reduce = new Reduce(this.numInputs);

        // pre-processing
        this.complementCover = comp.getComplement(this.cover);
        List<int[]> expandedCover = expand.expandFunction(this.cover, this.complementCover);
        System.out.println("Expanded Cover (in proprocessing): ");
        for (int[] cube : expandedCover) {
            System.out.println(Arrays.toString(cube));
        }

        List<int[]> irredundantCover = irrd.getIrredundant(expandedCover);

        // return irredundant cover in BLIF format; lots of plumbing
        System.out.println("Before loop Cover: ");
        for (int[] cube : irredundantCover) {
            System.out.println(Arrays.toString(cube));
        }
        System.out.println();

        // espresso loop
        int prevSize;
        do {
            prevSize = irredundantCover.size();
            List<int[]> reducedCover = reduce.reduceFunction(irredundantCover);
            expandedCover = expand.expandFunction(reducedCover, this.complementCover);
            irredundantCover = irrd.getIrredundant(expandedCover);
        } while (irredundantCover.size() < prevSize);

        // return irredundant cover in BLIF format; lots of plumbing
        System.out.println("Final Cover: ");
        for (int[] cube : irredundantCover) {
            System.out.println(Arrays.toString(cube));
        }
        System.out.println();
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

    // public void printExpandedCover() {
    // System.out.println("Expanded Cover : ");
    // for (int[] cube : this.expandedCover) {
    // System.out.println(Arrays.toString(cube));
    // }
    // }

}
