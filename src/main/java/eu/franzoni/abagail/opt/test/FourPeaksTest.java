package eu.franzoni.abagail.opt.test;

import java.util.Arrays;

import eu.franzoni.abagail.dist.DiscreteDependencyTree;
import eu.franzoni.abagail.dist.DiscreteUniformDistribution;
import eu.franzoni.abagail.dist.Distribution;

import eu.franzoni.abagail.opt.DiscreteChangeOneNeighbor;
import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.opt.GenericHillClimbingProblem;
import eu.franzoni.abagail.opt.HillClimbingProblem;
import eu.franzoni.abagail.opt.NeighborFunction;
import eu.franzoni.abagail.opt.RandomizedHillClimbing;
import eu.franzoni.abagail.opt.SimulatedAnnealing;
import eu.franzoni.abagail.opt.example.*;
import eu.franzoni.abagail.opt.ga.CrossoverFunction;
import eu.franzoni.abagail.opt.ga.DiscreteChangeOneMutation;
import eu.franzoni.abagail.opt.ga.SingleCrossOver;
import eu.franzoni.abagail.opt.ga.GenericGeneticAlgorithmProblem;
import eu.franzoni.abagail.opt.ga.GeneticAlgorithmProblem;
import eu.franzoni.abagail.opt.ga.MutationFunction;
import eu.franzoni.abagail.opt.ga.StandardGeneticAlgorithm;
import eu.franzoni.abagail.opt.prob.GenericProbabilisticOptimizationProblem;
import eu.franzoni.abagail.opt.prob.MIMIC;
import eu.franzoni.abagail.opt.prob.ProbabilisticOptimizationProblem;
import eu.franzoni.abagail.shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
    /** The n value */
    private static final int N = 200;
    /** The t value */
    private static final int T = N / 5;
    
    public static void main(String[] args) {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200000);
        fit.train();
        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainer(sa, 200000);
        fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()));
        
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        fit = new FixedIterationTrainer(ga, 1000);
        fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));
        
        MIMIC mimic = new MIMIC(200, 20, pop);
        fit = new FixedIterationTrainer(mimic, 1000);
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
    }
}
