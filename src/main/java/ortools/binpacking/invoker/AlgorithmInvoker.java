package ortools.binpacking.invoker;

/**
 * common algorithm invoker.
 *
 * @author 老钱开山大弟子的小师弟
 */
public interface AlgorithmInvoker {

    int invoker(BinPackageProblem problem, AlgorithmParam param);

}
