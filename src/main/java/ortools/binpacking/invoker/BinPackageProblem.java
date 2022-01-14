package ortools.binpacking.invoker;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * bin package problem.
 *
 * @author 老钱开山大弟子的小师弟
 */
@AllArgsConstructor
@Getter
public class BinPackageProblem {

    private long[] weights;
    private long[] volumes;
    private long[] counts;
    private long binWeight;
    private long binVolume;
    private long binCount;
    private int numItems;
    private int numBins;

    public BinPackageProblem(long[] weights, long[] volumes, long[] counts,
                             long binWeight, long binVolume, long binCount) {
        this.weights = weights;
        this.volumes = volumes;
        this.counts = counts;
        this.binWeight = binWeight;
        this.binVolume = binVolume;
        this.binCount = binCount;
        if (weights.length != volumes.length || weights.length != counts.length || volumes.length != counts.length) {
            throw new IllegalArgumentException();
        }
        int arrayLength = weights.length;
        this.numItems = arrayLength;
        this.numBins = arrayLength;
    }

}