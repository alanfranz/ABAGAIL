package eu.franzoni.abagail.opt.test;

import java.util.Arrays;
import java.util.List;

import eu.franzoni.abagail.dist.DiscreteDependencyTree;
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
    //private static final int N = 30;

    /**
     * The t value
     */
    //private static final int T = N / 10;
    public static void main(String[] args) {
        final List<Integer> sizes = Arrays.asList(10, 40, 100);
        final List<Integer> divisors = Arrays.asList(10, 20);

        final List<Long> seeds = Arrays.asList(13529634494442651L, 5656981922355810402L
                , -6449267216724225329L, -4411830362201525141L, 5566798682235319198L, -8758420082094094714L, 3253005473232577707L, -5286286812144096374L, -4119452930045979494L, 5770958020097602318L, 683138933608733263L, 8209964896825328468L, 7031392117777361361L, -6121210489028553847L, -6469731346477625132L, -6665466218042342981L, -5961282655162978163L, 1696770048611725543L, 8029254822252000300L, -942868935122628217L, -2724805368243433498L, 6744958667841432491L, 8992788776112062706L, -267750365740541309L, -4609461931086629707L, -6346129594062662673L, 8303094097110875302L, 8534572557185839709L, -1513711453533974150L, -1988507183983714377L,
                -4771493155960262495L, -1606160883395982541L, 2578644751320052306L, 9110929450740901209L, 717207449707688923L, 2848358714595589750L, -3302511082686917161L, 559566510816032781L, -6245497221226436475L,
                -6040228022713241920L, -5942417874383785695L, -8729688670016265721L, -4461026529614911420L, 4216892117723149175L, 7183285063220747610L, 5718428447980185135L, 3231711550978164945L, -7674679440764368227L,
                2085066947842327304L, -4899074866717547002L, -4778814035843079912L, 7783047912961912602L, -6926031667219542166L, 636470299668616838L, -6320072164692178048L, 1641938784710373347L, 443931928227223757L,
                -5177123061854887676L, 6439515748840354508L, -4635503008972482907L, 3330546935734397669L, -4971350913261785778L, 3937455567930511370L, -4709599159417791981L, -6315455503296899339L, 6917820859638531499L,
                -2472817110656597739L, 6084103684703190395L, 1403470006669503441L, -6027930348546990837L, 2857457684571945049L, 8422507991458925309L, 97320894082290686L, 787151114575790035L, 1183645201713906288L,
                -4772122828044506937L, 6370189761144798003L, -7665891922111399228L, -7330054334128328316L, -5550499909978860302L, 1534280183352406632L, -8636587672647994597L, -2468555069095201964L,
                -9207226534984257761L, -7673201591837605072L, 865598175074152134L, -7256334452322277028L, -7475208512913142686L, -6924067848650035036L, -6415543556141845702L, -640014635566371395L,
                -1981500378889374518L, -8219243506348948437L, 5146132043411957838L, 6242824757159542603L, -2527691444947296299L, -7829257710384305046L, -3979136175127034588L, -4114460505497727855L, 613711846571433146L);

        for (Integer divisor : divisors) {
            for (Integer N : sizes) {
                for (Long seed : seeds) {
                    final int T = N / divisor;
                    System.out.println("Array size: " + N + " T: " + T);
                    MyRandom.initialize(seed);
                    fourPeaks(N, T);
                    sixPeaks(N, T);
                    System.out.println("--------------");
                }
            }
        }


    }

    private static void fourPeaks(int N, int T) {
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
        System.out.println("RHC: " + ef.value(optimal));

        // SA
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainer(sa, 2000);
        final double saError = fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()));

        // GA
        CrossoverFunction cf = new SingleCrossOver();
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        fit = new FixedIterationTrainer(ga, 2000);
        final double gaError = fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));

        // MIMIC
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        MIMIC mimic = new MIMIC(200, 20, pop);
        fit = new FixedIterationTrainer(mimic, 2000);
        final double mimicError = fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
    }

    private static void sixPeaks(int N, int T) {
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
