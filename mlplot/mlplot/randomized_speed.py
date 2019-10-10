import pandas as pd
from matplotlib.pyplot import show, savefig
import sys
from mlplot.load import load_randomized_algos
from itertools import product


def calc_avg_times(func):
    df = load_randomized_algos()

    values = df[(df["maximumIterations"] == 50000) & (df["evaluationFunction"] == func)
                & (df["bitstringSize"] == 60)]

    if not values["maximumTheoreticalValue"].max() == values["maximumTheoreticalValue"].min():
        raise ValueError("something stinks")

    maxValue = values["maximumTheoreticalValue"].max()

    maximized_values = values[values["actualValue"] == maxValue]
    nonmaximized_values = values[values["actualValue"] < maxValue]
    return maximized_values, nonmaximized_values


if __name__ == "__main__":
    out = sys.stdout
    LATEX_HEADER = r"\begin{longtable}{l c c c c}" + "\n"
    LATEX_FOOTER = r"\end{longtable}" + "\n"

    for func in ["FourPeaksEvaluationFunction", "SixPeaksEvaluationFunction", "FlipFlopEvaluationFunction"]:
        out.write(LATEX_HEADER)
        maxim, nonmaxim = calc_avg_times(func)
        funcname = func.rsplit("EvaluationFunction")[0]
        out.write(f"\\caption{{{funcname} timings (ms avg/std)}}\\\\\n")
        for algo in ("RHC", "SA", "GA", "MIMIC"):
            out.write(f"& {algo} ")
        out.write("\\\\\n")

        out.write(f"Optimum ")
        for algo in ("RHC", "SA", "GA", "MIMIC"):
            max_avg = maxim[maxim["algorithm"] == algo]["executionTimeMillis"].mean()
            max_std = maxim[maxim["algorithm"] == algo]["executionTimeMillis"].std()
            out.write(f"& {max_avg:.2f} / {max_std:.2f} ")
        out.write("\\\\\n")

        out.write("Non-optimum ")
        for algo in ("RHC", "SA", "GA", "MIMIC"):
            nonmax_avg = nonmaxim[nonmaxim["algorithm"] == algo]["executionTimeMillis"].mean()
            nonmax_std = nonmaxim[nonmaxim["algorithm"] == algo]["executionTimeMillis"].std()

            out.write(f"& {nonmax_avg:.2f} / {nonmax_std:.2f} ")
        out.write("\\\\\n")

        out.write("\n")

        out.write(LATEX_FOOTER)





