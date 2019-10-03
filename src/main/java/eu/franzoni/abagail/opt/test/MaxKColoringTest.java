package eu.franzoni.abagail.opt.test;

import java.util.Arrays;
import java.util.Random;

import eu.franzoni.abagail.opt.ga.MaxKColorFitnessFunction;
import eu.franzoni.abagail.opt.ga.Vertex;

import eu.franzoni.abagail.dist.DiscreteDependencyTree;
import eu.franzoni.abagail.dist.DiscretePermutationDistribution;
import eu.franzoni.abagail.dist.DiscreteUniformDistribution;
import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.opt.DiscreteChangeOneNeighbor;
import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.opt.SwapNeighbor;
import eu.franzoni.abagail.opt.GenericHillClimbingProblem;
import eu.franzoni.abagail.opt.HillClimbingProblem;
import eu.franzoni.abagail.opt.NeighborFunction;
import eu.franzoni.abagail.opt.RandomizedHillClimbing;
import eu.franzoni.abagail.opt.SimulatedAnnealing;
import eu.franzoni.abagail.opt.ga.CrossoverFunction;
import eu.franzoni.abagail.opt.ga.DiscreteChangeOneMutation;
import eu.franzoni.abagail.opt.ga.SingleCrossOver;
import eu.franzoni.abagail.opt.ga.SwapMutation;
import eu.franzoni.abagail.opt.ga.GenericGeneticAlgorithmProblem;
import eu.franzoni.abagail.opt.ga.GeneticAlgorithmProblem;
import eu.franzoni.abagail.opt.ga.MutationFunction;
import eu.franzoni.abagail.opt.ga.StandardGeneticAlgorithm;
import eu.franzoni.abagail.opt.ga.UniformCrossOver;
import eu.franzoni.abagail.opt.prob.GenericProbabilisticOptimizationProblem;
import eu.franzoni.abagail.opt.prob.MIMIC;
import eu.franzoni.abagail.opt.prob.ProbabilisticOptimizationProblem;
import eu.franzoni.abagail.shared.FixedIterationTrainer;

/**
 * 
 * @author kmandal
 * @version 1.0
 */
public class MaxKColoringTest {
    /** The n value */
    private static final int N = 50; // number of vertices
    private static final int L =4; // L adjacent nodes per vertex
    private static final int K = 8; // K possible colors
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Random random = new Random(N*L);
        // create the random velocity
        Vertex[] vertices = new Vertex[N];
        for (int i = 0; i < N; i++) {
            Vertex vertex = new Vertex();
            vertices[i] = vertex;	
            vertex.setAdjMatrixSize(L);
            for(int j = 0; j <L; j++ ){
            	 vertex.getAadjacencyColorMatrix().add(random.nextInt(N*L));
            }
        }
        /*for (int i = 0; i < N; i++) {
            Vertex vertex = vertices[i];
            System.out.println(Arrays.toString(vertex.getAadjacencyColorMatrix().toArray()));
        }*/
        // for rhc, sa, and ga we use a permutation based encoding
        MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
        Distribution odd = new DiscretePermutationDistribution(K);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new SingleCrossOver();
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        
        Distribution df = new DiscreteDependencyTree(.1); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        long starttime = System.currentTimeMillis();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 20000);
        fit.train();
        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .1, hcp);
        fit = new FixedIterationTrainer(sa, 20000);
        fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 10, 60, gap);
        fit = new FixedIterationTrainer(ga, 50);
        fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainer(mimic, 5);
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));  
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
    }
}
