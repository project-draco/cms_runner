package br.unb.cic.cms.runner.algorithm;

import br.unirio.lns.hdesign.model.Project;
import br.unirio.lns.hdesign.multiobjective.CouplingProblem;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.operator.crossover.UniformCrossover;
import jmetal.base.operator.mutation.IntUniformMutation;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.metaheuristics.nsgaII.NSGAII;

public class NSGAIIFactory implements  AlgorithmFactory {

    private int populationFactor;
    private int evaluationsFactor;

    public NSGAIIFactory() {
        this(2, 50);
    }
    public NSGAIIFactory(int populationFactor, int evaluationsFactor) {
        System.out.println("NSGA-II Running in the " + populationFactor + " x " + evaluationsFactor + " mode.");
        this.populationFactor = populationFactor;
        this.evaluationsFactor = evaluationsFactor;
    }
    @Override
    public Algorithm instance(Project project) throws Exception {
        CouplingProblem problem = new CouplingProblem(project);

        Operator crossover = new UniformCrossover();
        crossover.setParameter("probability", 1.0);

        Operator mutation = new IntUniformMutation();
        mutation.setParameter("probability", 1.0 / problem.getNumberOfVariables());

        Operator selection = new BinaryTournament();

//        int population = 10 * project.getPackageCount();
//        int evaluations = 200 * project.getPackageCount() * population;

        int population = this.populationFactor * project.getClassCount();
        int evaluations = this.evaluationsFactor * project.getClassCount() * population;

        NSGAII algorithm = new NSGAII(problem);
        algorithm.setInputParameter("populationSize", population);
        algorithm.setInputParameter("maxEvaluations", evaluations);
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);
        return algorithm;
    }
}
