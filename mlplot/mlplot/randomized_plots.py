import warnings

from mlplot.load import load_randomized_algos
from itertools import product
import pandas as pd
from matplotlib.pyplot import show, savefig, clf
import os

# POSSIBLE IMPROVEMENT: PLOT STANDARD DEVIATION AS WELL

ALGOS = ('RHC', 'SA', 'GA', 'MIMIC')

def savefigure(ax, filename):
    homedir = os.getenv("HOME")
    dropbox_out = os.path.join(homedir, "Dropbox", "gatech", "ml", "assignment2", "img", filename)
    ax.get_figure().savefig(filename)
    try:
        ax.get_figure().savefig(dropbox_out)
    except:
        warnings.warn("didn't save in dropbox home")

def plot_learning_curve(func):
    df = load_randomized_algos()
    values = df[(df["maximumIterations"] == 50000) & (df["evaluationFunction"] == func) & (df["bitstringSize"] == 60)]

    if not values["maximumTheoreticalValue"].max() == values["maximumTheoreticalValue"].min():
        raise ValueError("something stinks")

    maxValue = values["maximumTheoreticalValue"].max()

    # let's see the learning curves

    all_curves = {}

    for algo in ALGOS:
        algoOnly = values[values["algorithm"] == algo]

        curves = pd.DataFrame(((float(v) for v in c.split(";")) for c in algoOnly["learningCurve"]))
        all_curves[algo] = curves.T

    return all_curves, maxValue






if __name__ == "__main__":
    for func in ["FourPeaksEvaluationFunction", "SixPeaksEvaluationFunction",
                 "FlipFlopEvaluationFunction"]:
        all_curves, max_value = plot_learning_curve("FourPeaksEvaluationFunction")

        for algo in ALGOS:
            funcname = func.rsplit('EvaluationFunction')[0]
            ax = all_curves[algo].plot(
                title=f"{algo} searching {funcname}, fitness vs iterations.\n"
                      f"60-bit input, max fitness: {max_value}")
            ax.get_legend().remove()
            ax.set_xlabel("iterations")
            ax.set_ylabel("fitness")
            ax.set_ylim(0, max_value+5)
            savefigure(ax, f"{algo}_{funcname}_learning_curves.png".lower())

            clf()


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


