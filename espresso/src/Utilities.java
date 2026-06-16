import java.util.ArrayList;
import java.util.List;

public class Utilities {
    private final int numInputs;
    private final List<int[]> cover;

    public Utilities(int numInputs, final List<int[]> cover) {
        this.numInputs = numInputs;
        this.cover = cover; // here just referenced
    }

    public List<int[]> getComplement() {
        Complement comp = new Complement(this.numInputs);

        return comp.getComplement(this.cover);
    }

    public int[] getOrderSplitVar() {

    }

}
