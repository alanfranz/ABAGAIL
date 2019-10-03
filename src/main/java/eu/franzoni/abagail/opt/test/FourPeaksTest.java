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
import eu.franzoni.abagail.shared.*;
import eu.franzoni.abagail.shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 *
 * @version 1.0
 */
public class FourPeaksTest {
    /**
     * The n value
     */
    private static final int N = 100;
    /**
     * The t value
     */
    private static final int T = N / 10;

    public static void main(String[] args) {
        //fourPeaks();
        sixPeaks();

    }

    private static void fourPeaks() {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        FourPeaksEvaluationFunction ef = new FourPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);

        // RANDOM HILL CLIMBING
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        Trainer fit = new FixedIterationTrainer(rhc, 2000);
        final double rhcError = fit.train();
        Instance optimal = rhc.getOptimal();
        System.out.println("Four Peaks Theoretical Maximum: " + ef.findTheoreticalMaximum(N));
        System.out.println("RHC: " + ef.value(optimal) + " ERROR: " + rhcError);

        // SA
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainer(sa, 2000);
        final double saError = fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()) + " ERROR: " + saError);

        // GA
        CrossoverFunction cf = new SingleCrossOver();
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        fit = new FixedIterationTrainer(ga, 2000);
        final double gaError = fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()) + " ERROR: " + gaError);

        // MIMIC
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        MIMIC mimic = new MIMIC(200, 20, pop);
        fit = new FixedIterationTrainer(mimic, 2000);
        final double mimicError = fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()) + " ERROR: " + mimicError);
    }

    private static void sixPeaks() {
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        SixPeaksEvaluationFunction ef = new SixPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);

        // RANDOM HILL CLIMBING
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        Trainer fit = new FixedIterationTrainer(rhc, 200000);
        final double rhcError = fit.train();
        Instance optimal = rhc.getOptimal();
        System.out.println("Six Peaks Theoretical Maximum: " + ef.findTheoreticalMaximum(N));
        System.out.println("RHC: " + ef.value(optimal) + " ERROR: " + rhcError);

        // SA
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainer(sa, 200000);
        final double saError = fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()) + " ERROR: " + saError);

        // GA
        CrossoverFunction cf = new SingleCrossOver();
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        fit = new MaximumAwareTrainer(ga, ef, ef.findTheoreticalMaximum(N), 200000);
        final double gaError = fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()) + " ERROR: " + gaError);

        // MIMIC
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        MIMIC mimic = new MIMIC(200, 20, pop);
        fit = new MaximumAwareTrainer(mimic, ef, ef.findTheoreticalMaximum(N), 200000);
        final double mimicError = fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()) + " ERROR: " + mimicError);
    }
}
