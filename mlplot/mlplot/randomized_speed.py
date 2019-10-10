from mlplot.load import load_randomized_algos
from itertools import product
import pandas as pd
from matplotlib.pyplot import show, savefig

def calc_avg_times(func):
    df = load_randomized_algos()
    values = df[(df["maximumIterations"] == 50000) & (df["evaluationFunction"] == func)
                & (df["bitstringSize"] == 60)]

    if not values["maximumTheoreticalValue"].max() == values["maximumTheoreticalValue"].min():
        raise ValueError("something stinks")

    maxValue = values["maximumTheoreticalValue"].max()

    #maximized_values = values[values["actualValue"] == maxValue]
    maximized_values = values
    return maximized_values


    # algos = ['RHC', 'SA', 'GA', 'MIMIC']
    # # let's see the learning curves
    #
    # all_curves = {}
    #
    # for algo in algos:
    #     algoOnly = values[values["algorithm"] == algo]
    #
    #     curves = pd.DataFrame(((float(v) for v in c.split(";")) for c in algoOnly["learningCurve"]))
    #     all_curves[algo] = curves.mean(axis=0)
    #
    # all_curves["theoreticalMax"] = maxValue
    #
    # return pd.DataFrame(all_curves)

if __name__ == "__main__":
    mxv = calc_avg_times("SixPeaksEvaluationFunction")