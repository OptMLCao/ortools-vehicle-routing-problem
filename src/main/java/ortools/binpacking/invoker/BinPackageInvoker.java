package ortools.binpacking.invoker;

import java.util.Arrays;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import lombok.extern.slf4j.Slf4j;

/**
 * bin package
 *
 * @author 老钱开山大弟子的小师弟
 */
@Slf4j
public class BinPackageInvoker implements AlgorithmInvoker{

    @Override
    public int invoker(BinPackageProblem problem, AlgorithmParam param) {
        Loader.loadNativeLibraries();
        // Create the linear solver with the SCIP backend.
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            log.error("Could not create solver SCIP");
        }
        solver.setTimeLimit(param.getTimeLimitMilliseconds());
        solver.setNumThreads(param.getNumThreads());
        int numItems = problem.getNumItems();
        int numBins = problem.getNumBins();
        long[] weights = problem.getWeights();
        long[] volumes = problem.getVolumes();
        long[] counts = problem.getCounts();
        long binWeight = problem.getBinWeight();
        long binVolume = problem.getBinVolume();
        long binCount = problem.getBinCount();
        /* setting variable */
        MPVariable[][] x = new MPVariable[numItems][numBins];
        for (int i = 0; i < numItems; ++i) {
            for (int j = 0; j < numBins; ++j) {
                x[i][j] = solver.makeIntVar(0, 1, "");
            }
        }
        MPVariable[] y = new MPVariable[numBins];
        for (int j = 0; j < numBins; ++j) {
            y[j] = solver.makeIntVar(0, 1, "");
        }

        double infinity = Double.POSITIVE_INFINITY;
        for (int i = 0; i < numItems; ++i) {
            MPConstraint constraint = solver.makeConstraint(1, 1, "");
            for (int j = 0; j < numBins; ++j) {
                constraint.setCoefficient(x[i][j], 1);
            }
        }
        /* capacity constraint */
        boolean isWeightConstraint = false;
        if (Arrays.stream(weights).sum() > 0.) {
            isWeightConstraint = true;
            log.info("add weight constraint");
        }
        boolean isVolumeConstraint = false;
        if (Arrays.stream(volumes).sum() > 0.) {
            isVolumeConstraint = true;
            log.info("add volume constraint");
        }
        boolean isCountConstraint = false;
        if (Arrays.stream(counts).sum() > 0.) {
            isCountConstraint = true;
            log.info("add count constraint");
        }
        /* add constraint */
        for (int j = 0; j < numBins; ++j) {
            if (isWeightConstraint) {
                MPConstraint weightConstraint = solver.makeConstraint(0, infinity, "weight");
                weightConstraint.setCoefficient(y[j], binWeight);
                for (int i = 0; i < numItems; ++i) {
                    weightConstraint.setCoefficient(x[i][j], -weights[i]);
                }
            }
            if (isVolumeConstraint) {
                MPConstraint volumeConstraint = solver.makeConstraint(0, infinity, "volume");
                volumeConstraint.setCoefficient(y[j], binVolume);
                for (int i = 0; i < numItems; ++i) {
                    volumeConstraint.setCoefficient(x[i][j], -volumes[i]);
                }
            }
            if (isCountConstraint) {
                MPConstraint volumeConstraint = solver.makeConstraint(0, infinity, "count");
                volumeConstraint.setCoefficient(y[j], binCount);
                for (int i = 0; i < numItems; ++i) {
                    volumeConstraint.setCoefficient(x[i][j], -counts[i]);
                }
            }
        }
        /* set objective */
        MPObjective objective = solver.objective();
        for (int j = 0; j < numBins; ++j) {
            objective.setCoefficient(y[j], 1);
        }
        objective.setMinimization();
        /* solve problem */
        final MPSolver.ResultStatus resultStatus = solver.solve();
        /* print solution && Check that the problem has an optimal solution. */
        int assignCount = 0;
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            assignCount = (int) objective.value();
            log.info("Number of bins used: {}", assignCount);
            double totalWeight = 0;
            double totalVolume = 0.;
            double totalCount = 0.;
            for (int j = 0; j < numBins; ++j) {
                if (y[j].solutionValue() == 1) {
                    log.info("Bin " + j);
                    double localBinWeight = 0;
                    double localBinVolume = 0.;
                    double localBinCount = 0.;
                    for (int i = 0; i < numItems; ++i) {
                        if (x[i][j].solutionValue() == 1) {
                            log.info("Item {} weight:{}, volume:{}, count:{} ", i, weights[i], volumes[i], counts[i]);
                            localBinWeight += weights[i];
                            localBinVolume += volumes[i];
                            localBinCount += counts[i];
                        }
                    }
                    log.info("Packed bin weight{}, volume:{}, count{}", localBinWeight, localBinVolume, localBinCount);
                    totalWeight += localBinWeight;
                    totalVolume += localBinVolume;
                    totalCount += localBinCount;
                }
            }
            log.info("Total packed weight:{}, volume{}, count{}", totalWeight, totalVolume, totalCount);
        } else {
            log.error("The problem does not have an optimal solution.");
        }
        return assignCount;
    }

}
