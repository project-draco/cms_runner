package br.unb.cic.cms.runner.algorithm;

import br.unirio.lns.hdesign.model.Project;
import br.unirio.lns.hdesign.multiobjective.CouplingProblem;
import jmetal.base.Algorithm;

public class RandomSearchFactory implements AlgorithmFactory {
    public int maxEvaluations_ = 25000;
    @Override
    public Algorithm instance(Project instance) throws Exception {
        CouplingProblem problem = new CouplingProblem(instance);
        Algorithm algorithm = new jmetal.metaheuristics.randomSearch.RandomSearch(problem);

        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
        return algorithm;
    }
}
