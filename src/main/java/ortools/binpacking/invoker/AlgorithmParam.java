package ortools.binpacking.invoker;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * base algorithm param.
 *
 * @author 老钱开山大弟子的小师弟
 */
@Data
@AllArgsConstructor
public class AlgorithmParam {

    private long timeLimitMilliseconds;
    private int numThreads;

}
