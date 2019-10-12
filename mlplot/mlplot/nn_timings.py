import sys

from mlplot.load import load_neural_networks

if __name__ == "__main__":
    global_df = load_neural_networks()
    algos = global_df["algorithmWithParams"].unique()
    #global_df = global_df[global_df["maximumIterations"] == 500]
    algos.sort()

    #global_df["avgTimePerIteration"] = global_df["executionTimeMillis"] / global_df["maximumIterations"]
    for algo in algos:
        time_sum = global_df[global_df["algorithmWithParams"] == algo]["executionTimeMillis"].sum()
        iter_sum = global_df[global_df["algorithmWithParams"] == algo]["maximumIterations"].sum()
        sys.stdout.write(algo.replace("StandardGeneticAlgorithm", "GA").replace("SimulatedAnnealing", "SA"
                                                                                ).replace("RandomizedHillClimbing",
                                                                                          "RHC").replace(
            "BackPropagation", "BP"
            ).replace("populationSize", "pop").replace("cooling", "cool").replace("{", " ").replace("}", " "
                                                                                                    ).replace("toMate",
                                                                                                              "mate").replace(
            "toMutate", "mut") + f" & {time_sum/iter_sum:.2f}\n")




