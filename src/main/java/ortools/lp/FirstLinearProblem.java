package ortools.lp;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

/** Simple linear programming example. */
public final class FirstLinearProblem {

    public static void main(String[] args) {
        Loader.loadNativeLibraries();
        // MpSolver只是一个包含各种三方线性求解器的包装类.
        // 这里着重强调是包装类，内部还包含了混合整数优化引擎，混合整数优化不是此处的重点.
        MPSolver solver = MPSolver.createSolver("GLOP");
        // 定义变量.
        double infinity = Double.POSITIVE_INFINITY;
        // x and y are continuous non-negative variables.
        MPVariable x = solver.makeNumVar(0.0, infinity, "x");
        MPVariable y = solver.makeNumVar(0.0, infinity, "y");
        System.out.println("Number of variables = " + solver.numVariables());
        // 定义约束.
        // x + 2*y <= 14.
        MPConstraint c0 = solver.makeConstraint(-infinity, 14.0, "c0");
        c0.setCoefficient(x, 1);
        c0.setCoefficient(y, 2);

        // 3*x - y >= 0.
        MPConstraint c1 = solver.makeConstraint(0.0, infinity, "c1");
        c1.setCoefficient(x, 3);
        c1.setCoefficient(y, -1);

        // x - y <= 2.
        MPConstraint c2 = solver.makeConstraint(-infinity, 2.0, "c2");
        c2.setCoefficient(x, 1);
        c2.setCoefficient(y, -1);
        System.out.println("Number of constraints = " + solver.numConstraints());

        // 定义目标函数。原题目是最大化优化目标.
        // Maximize 3 * x + 4 * y.
        MPObjective objective = solver.objective();
        objective.setCoefficient(x, 3);
        objective.setCoefficient(y, 4);
        objective.setMaximization();
        // 调用solver进行求解.
        final MPSolver.ResultStatus resultStatus = solver.solve();
        // 打印结果.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution:");
            System.out.println("Objective value = " + objective.value());
            System.out.println("x = " + x.solutionValue());
            System.out.println("y = " + y.solutionValue());
        } else {
            System.err.println("The problem does not have an optimal solution!");
        }

        System.out.println("\nAdvanced usage:");
        System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
        System.out.println("Problem solved in " + solver.iterations() + " iterations");
    }

}