import sys

from mlplot.load import load_randomized_algos
from itertools import product


def dothings():
    df = load_randomized_algos()

    algos = ['RHC', 'SA', 'GA', 'MIMIC']
    maxIterations = [1000, 10000, 50000]
    ef = ['FourPeaksEvaluationFunction', 'SixPeaksEvaluationFunction',
          'CountOnesEvaluationFunction', 'FlipFlopEvaluationFunction']
    sizes = [10, 30, 60, 100]

    # each cell: reaches optimum%/within 5% of optimum

    out = sys.stdout

    LATEX_HEADER = r"\begin{longtable}{l|c|c|c}" + "\n"
    LATEX_FOOTER = r"\end{longtable}" + "\n"

    for func in ef:
        out.write(LATEX_HEADER)
        funcname = func.rsplit("EvaluationFunction")[0]
        out.write(f"\\caption{{{funcname}}} results\\\\\n")


        out.write(f"Algorithm-InputSize & 1,000 iter (opt/5pc) & 10,000 iter (opt/5pc) & 50,000 iter (opt/5pc) \\\\\n")
        out.write("\\hline\n")

        for algo, size in product(algos, sizes):
            current_df = df[
                (df["algorithm"] == algo) & (df["bitstringSize"] == size) & (df["evaluationFunction"] == func)]

            out.write(f"{algo}-{size}")

            for maxIter in maxIterations:
                iter_df = current_df[current_df["maximumIterations"] == maxIter]
                reaches_optimum = iter_df[iter_df["actualValue"] == iter_df["maximumTheoreticalValue"]].shape[0] / \
                                  iter_df.shape[0]
                within_5p_optimum = \
                iter_df[iter_df["actualValue"] >= (iter_df["maximumTheoreticalValue"] * 0.95)].shape[0] / iter_df.shape[
                    0]
                out.write(f" & {reaches_optimum:.2f}/{within_5p_optimum:.2f} ")

            out.write("\\\\\n")

        out.write(LATEX_FOOTER)


if __name__ == "__main__":
    dothings()
