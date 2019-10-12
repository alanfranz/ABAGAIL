import warnings

from mlplot.load import load_randomized_algos, load_neural_networks_training
from itertools import product
import pandas as pd
from matplotlib.pyplot import show, savefig, clf, figure
import os

# POSSIBLE IMPROVEMENT: PLOT STANDARD DEVIATION AS WELL


def savefigure(ax, filename):
    homedir = os.getenv("HOME")
    dropbox_out = os.path.join(homedir, "Dropbox", "gatech", "ml", "assignment2", "img", filename)
    ax.get_figure().savefig(os.path.join("img", filename), dpi=300)
    try:
        ax.get_figure().savefig(dropbox_out, dpi=300)
    except:
        warnings.warn("didn't save in dropbox home")

def plot_learning_curve():
    df = load_neural_networks_training()
    values = df[(df["maximumIterations"] == 10000)]

    #all_curves = {}
    algos = values["algorithmWithParams"].unique()
    algos.sort()
    print(algos)
    all_curves = {}

    for algo in algos:
        if algo == "BackPropagation":
            continue
        algoOnly = values[values["algorithmWithParams"] == algo]
        learningCurve = [ 1/float(v) for v in algoOnly.iloc[0]["errorCurve"].split(";")]
        all_curves[algo.replace("StandardGeneticAlgorithm", "GA").replace("SimulatedAnnealing", "SA"
                                                                                ).replace("RandomizedHillClimbing",
                                                                                          "RHC").replace(
            "BackPropagation", "BP"
            ).replace("populationSize", "pop").replace("cooling", "cool").replace("{", " ").replace("}", " "
                                                                                                    ).replace("toMate",
                                                                                                              "mate").replace(
            "toMutate", "mut")] = learningCurve


        #items = [float(v) for v in c.split(";")] for c in algoOnly["errorCurve"].iloc[0]]
        #curve = pd.Series(items)
        #all_curves[algo] = curve

    return all_curves



if __name__ == "__main__":
    all_curves = plot_learning_curve()
    figure(num=None, figsize=(10, 6))
    x = pd.DataFrame(all_curves)
    ax = x.plot(title="NN training, fitness vs iterations")
    ax.set_xlabel("iterations")
    ax.set_ylabel("fitness")
    ax.get_legend().prop.set_size("x-small")
    savefigure(ax, "nn-learning-curves.png")


    # for func in ["FourPeaksEvaluationFunction", "SixPeaksEvaluationFunction",
    #              "FlipFlopEvaluationFunction"]:
    #     all_curves, max_value = plot_learning_curve(func)
    #
    #     for algo in ALGOS:
    #         funcname = func.rsplit('EvaluationFunction')[0]
    #         ax = all_curves[algo].plot(
    #             title=f"{algo} searching {funcname}, fitness vs iterations.\n"
    #                   f"60-bit input, max fitness: {max_value}")
    #         ax.get_legend().remove()
    #         ax.set_xlabel("iterations")
    #         ax.set_ylabel("fitness")
    #         ax.set_ylim(0, max_value+5)
    #         savefigure(ax, f"{algo}_{funcname}_learning_curves.png".lower())
    #
    #         clf()


    # all_curves = plot_learning_curve("FourPeaksEvaluationFunction")
    # ax = all_curves.plot(title="FourPeaks avg fitness vs iterations, 60-bit")
    # ax.set_xlabel("iterations")
    # ax.set_ylabel("avg fitness")
    # ax.get_figure().savefig("fourpeaks-fitness-average.png")
    #
    # all_curves = plot_learning_curve("SixPeaksEvaluationFunction")
    # ax = all_curves.plot(title="SixPeaks avg fitness vs iterations, 60-bit")
    # ax.set_xlabel("iterations")
    # ax.set_ylabel("avg fitness")
    # ax.get_figure().savefig("sixpeaks-fitness-average.png")
    #
    # all_curves = plot_learning_curve("FlipFlopEvaluationFunction")
    # ax = all_curves.plot(title="FlipFlop avg fitness vs iterations, 60-bit")
    # ax.set_xlabel("iterations")
    # ax.set_ylabel("avg fitness")
    # ax.get_figure().savefig("flipflop-fitness-average.png")


