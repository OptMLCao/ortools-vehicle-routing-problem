package ortools.binpacking.invoker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * test sample.
 *
 * @author 老钱开山大弟子的小师弟
 */
@Slf4j
public class BinPackageInvokerTest {

    private BinPackageProblem problem;

    @Before
    public void runBefore() {
        final long[] weights = {48, 30, 19, 36, 36, 27, 42, 42, 36, 24, 30};
        final long[] volumes = {48, 30, 19, 36, 36, 27, 42, 42, 36, 24, 30};
        final long[] counts = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        problem = new BinPackageProblem(weights, volumes, counts, 100, 100, 100);
    }

    @Test
    public void testInvoker() {
        BinPackageInvoker binPackageInvoker = new BinPackageInvoker();
        AlgorithmParam param = new AlgorithmParam(2000, 1);
        int result = binPackageInvoker.invoker(problem, param);
        log.info("need box size is {}", result);
        Assert.assertEquals(4, result);
    }

}
