from mlplot.load import load_randomized_algos
from itertools import product
import pandas as pd
from matplotlib.pyplot import show, savefig

# POSSIBLE IMPROVEMENT: PLOT STANDARD DEVIATION AS WELL

def plot_learning_curve(func):
    df = load_randomized_algos()
    values = df[(df["maximumIterations"] == 50000) & (df["evaluationFunction"] == func) & (df["bitstringSize"] == 60)]

    if not values["maximumTheoreticalValue"].max() == values["maximumTheoreticalValue"].min():
        raise ValueError("something stinks")

    maxValue = values["maximumTheoreticalValue"].max()


    algos = ['RHC', 'SA', 'GA', 'MIMIC']
    # let's see the learning curves

    all_curves = {}

    for algo in algos:
        algoOnly = values[values["algorithm"] == algo]

        curves = pd.DataFrame(((float(v) for v in c.split(";")) for c in algoOnly["learningCurve"]))
        all_curves[algo] = curves.mean(axis=0)

    all_curves["theoreticalMax"] = maxValue

    return pd.DataFrame(all_curves)





if __name__ == "__main__":
    all_curves = plot_learning_curve("FourPeaksEvaluationFunction")
    ax = all_curves.plot(title="FourPeaks avg fitness vs iterations, 60-bit")
    ax.set_xlabel("iterations")
    ax.set_ylabel("avg fitness")
    ax.get_figure().savefig("fourpeaks-fitness-average.png")

    all_curves = plot_learning_curve("SixPeaksEvaluationFunction")
    ax = all_curves.plot(title="SixPeaks avg fitness vs iterations, 60-bit")
    ax.set_xlabel("iterations")
    ax.set_ylabel("avg fitness")
    ax.get_figure().savefig("sixpeaks-fitness-average.png")

    all_curves = plot_learning_curve("FlipFlopEvaluationFunction")
    ax = all_curves.plot(title="FlipFlop avg fitness vs iterations, 60-bit")
    ax.set_xlabel("iterations")
    ax.set_ylabel("avg fitness")
    ax.get_figure().savefig("flipflop-fitness-average.png")


