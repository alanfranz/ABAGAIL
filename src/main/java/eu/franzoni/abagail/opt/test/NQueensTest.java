package eu.franzoni.abagail.opt.test;

import java.util.Arrays;
import java.util.Random;
import eu.franzoni.abagail.opt.ga.NQueensFitnessFunction;
import eu.franzoni.abagail.dist.DiscreteDependencyTree;
import eu.franzoni.abagail.dist.DiscretePermutationDistribution;
import eu.franzoni.abagail.dist.DiscreteUniformDistribution;
import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.opt.DiscreteChangeOneNeighbor;
import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.opt.GenericHillClimbingProblem;
import eu.franzoni.abagail.opt.HillClimbingProblem;
import eu.franzoni.abagail.opt.NeighborFunction;
import eu.franzoni.abagail.opt.RandomizedHillClimbing;
import eu.franzoni.abagail.opt.SimulatedAnnealing;
import eu.franzoni.abagail.opt.SwapNeighbor;
import eu.franzoni.abagail.opt.example.*;
import eu.franzoni.abagail.opt.ga.CrossoverFunction;
import eu.franzoni.abagail.opt.ga.DiscreteChangeOneMutation;
import eu.franzoni.abagail.opt.ga.SingleCrossOver;
import eu.franzoni.abagail.opt.ga.GenericGeneticAlgorithmProblem;
import eu.franzoni.abagail.opt.ga.GeneticAlgorithmProblem;
import eu.franzoni.abagail.opt.ga.MutationFunction;
import eu.franzoni.abagail.opt.ga.StandardGeneticAlgorithm;
import eu.franzoni.abagail.opt.ga.SwapMutation;
import eu.franzoni.abagail.opt.prob.GenericProbabilisticOptimizationProblem;
import eu.franzoni.abagail.opt.prob.MIMIC;
import eu.franzoni.abagail.opt.prob.ProbabilisticOptimizationProblem;
import eu.franzoni.abagail.shared.FixedIterationTrainer;

/**
 * @author kmanda1
 * @version 1.0
 */
public class NQueensTest {
    /** The n value */
    private static final int N = 10;
    /** The t value */
    
    public static void main(String[] args) {
        int[] ranges = new int[N];
        Random random = new Random(N);
        for (int i = 0; i < N; i++) {
        	ranges[i] = random.nextInt();
        }
        NQueensFitnessFunction ef = new NQueensFitnessFunction();
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 100);
        fit.train();
        long starttime = System.currentTimeMillis();
        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        System.out.println("RHC: Board Position: ");
       // System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        SimulatedAnnealing sa = new SimulatedAnnealing(1E1, .1, hcp);
        fit = new FixedIterationTrainer(sa, 100);
        fit.train();
        
        starttime = System.currentTimeMillis();
        System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println("SA: Board Position: ");
       // System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 0, 10, gap);
        fit = new FixedIterationTrainer(ga, 100);
        fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println("GA: Board Position: ");
        //System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        MIMIC mimic = new MIMIC(200, 10, pop);
        fit = new FixedIterationTrainer(mimic, 5);
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        System.out.println("MIMIC: Board Position: ");
        //System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
    }
}
