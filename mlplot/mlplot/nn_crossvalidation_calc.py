import sys

from mlplot.load import load_neural_networks
import pandas as pd
from matplotlib.pyplot import show, clf

"""
['group', 'description', 'algorithmWithParams', 'maximumIterations',
       'actualIterations', 'trainingTruePositives', 'trainingTrueNegatives',
       'trainingFalsePositives', 'trainingFalseNegatives',
       'validationTruePositives', 'validationTrueNegatives',
       'validationFalsePositives', 'validationFalseNegatives',
       'globalTestTruePositives', 'globalTestTrueNegatives',
       'globalTestFalsePositives', 'globalTestFalseNegatives',
       'executionTimeMillis', 'errorCurve']
"""
def accuracy(tp: int, tn: int, fp: int, fn: int) -> float:
    return (tp+tn) / (tp+tn+fp+fn)

def precision(tp, fp):
    return tp/(tp+fp)

def recall(tp, fn):
    return tp/(tp+fn)

def f1(tp, tn, fp, fn):
    p = precision(tp, fp)
    r = recall(tp, fn)
    return 2*p*r/(p+r)

def calc_crossval_score(itnum):
    global_df = load_neural_networks()
    algos = global_df["algorithmWithParams"].unique()
    algos.sort()
    #maxIters = global_df["maximumIterations"].unique()
    maxIters = [itnum]
    maxIters.sort()
    accuracies = pd.DataFrame(index=maxIters, columns=algos)
    precisions = pd.DataFrame(index=maxIters, columns=algos)
    recalls = pd.DataFrame(index=maxIters, columns=algos)
    f1s = pd.DataFrame(index=maxIters, columns=algos)

    for algo in algos:
        sys.stdout.write(algo.replace("StandardGeneticAlgorithm", "GA").replace("SimulatedAnnealing", "SA"
            ).replace("RandomizedHillClimbing", "RHC").replace("BackPropagation", "BP"
            ).replace("populationSize", "pop").replace("cooling", "cool").replace("{", " ").replace("}", " "
         ).replace("toMate", "mate").replace("toMutate", "mut") + " & ")
        df = global_df[global_df["algorithmWithParams"] == algo]

        for iterations in maxIters:
            #print(iterations)
            cv_accuracy_sum = 0.0
            cv_precision_sum = 0.0
            cv_recall_sum = 0.0
            cv_f1_sum = 0.0

            for foldn in (1, 2, 3, 4, 5):
                frame = df[(df["description"] == f"fold{foldn}") & (df["maximumIterations"] == iterations)]
                tp = frame["validationTruePositives"].iloc[0]
                fp = frame["validationFalsePositives"].iloc[0]
                tn = frame["validationTrueNegatives"].iloc[0]
                fn = frame["validationFalseNegatives"].iloc[0]
                cv_accuracy_sum += accuracy(tp, tn, fp, fn)
                cv_precision_sum += precision(tp, fp)
                cv_recall_sum += recall(tp, fn)
                cv_f1_sum += f1(tp, tn, fp, fn)

            accuracies.loc[iterations, algo] = cv_accuracy_sum / 5.0
            precisions.loc[iterations, algo] = cv_precision_sum / 5.0
            recalls.loc[iterations, algo] = cv_recall_sum / 5.0
            f1s.loc[iterations, algo] = cv_f1_sum / 5.0

            print(f"{cv_accuracy_sum/5.0:.2f} & {cv_precision_sum/5.0:.2f} & {cv_recall_sum/5.0:.2f} & {cv_f1_sum/5.0:.2f} \\\\")

        print("\n")

    return accuracies, precisions, recalls, f1s

if __name__ == "__main__":
    #calc_crossval_score(10000)
    calc_crossval_score(500)
    #print(df)
