import java.util.LinkedList;
import java.util.List;

public class Espresso {

    /* private member functions */
    /* ENCODING - 1 = 01 = TRUE, 2 = 10 = FALSE, 3 = 11 = Don't Care */
    private final List<int[]> cover; // a 2D array, cover[i][j] = {1, 2, 3} means jth literal in ith cube has state =
                                     // x
    private final List<int[]> complementCover;
    private final List<int[]> minimizedCover;
    private final int numInputs;

    /**
     * Create an espresso object
     * 
     * @param functions
     */
    public Espresso(List<Function> functions) {

        /* Conversion of bitset PCN into List of 1D arrays for easier manipulation */
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

        /* Object init for all the required steps of Espresso */
        Complement comp = new Complement(this.numInputs);
        Expand expand = new Expand(this.numInputs);
        Irredundant irrd = new Irredundant(this.numInputs);
        Reduce reduce = new Reduce(this.numInputs);

        /* Start of Espresso algorithm */
        // (0) Get the complement
        this.complementCover = comp.getComplement(this.cover);
        // (1) pre-processing
        List<int[]> expandedCover = expand.expandFunction(this.cover, this.complementCover);
        List<int[]> irredundantCover = irrd.getIrredundant(expandedCover);
        // (2) espresso loop

        int prevSize; // cost variable
        do {
            prevSize = irredundantCover.size();
            List<int[]> reducedCover = reduce.reduceFunction(irredundantCover);
            expandedCover = expand.expandFunction(reducedCover, this.complementCover);
            irredundantCover = irrd.getIrredundant(expandedCover);
        } while (irredundantCover.size() < prevSize);
        this.minimizedCover = new LinkedList<>(irredundantCover);

        /* End of Espresso Algorithm */
    }

    public List<int[]> getMinimizedCover() {
        return new LinkedList<>(this.minimizedCover);
    }
}
