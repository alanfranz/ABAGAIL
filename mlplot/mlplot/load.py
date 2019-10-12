import numpy as np
import pandas as pd
import os
from pyarrow import ArrowIOError
import warnings

RANDOMIZED_ALGORITHMS_OUT_FN = os.path.join(os.path.dirname(os.path.realpath(__file__)), os.pardir, os.pardir,
                                            "experiments_output", "randomized_algorithms",
                                            "randomized_algorithms_experiment_output.csv.gz")

NN_OUT_FN = os.path.join(os.path.dirname(os.path.realpath(__file__)), os.pardir, os.pardir,
                                            "experiments_output", "neural",
                                            "neural.csv")

NN_TRAINING_OUT_FN = os.path.join(os.path.dirname(os.path.realpath(__file__)), os.pardir, os.pardir,
                                            "experiments_output", "neural",
                                            "neural-alltraining.csv")


# columns:
"group",
"bitstringSize",
"algorithm",
"evaluationFunction",
"maximumIterations",
"actualIterations",
"maximumTheoreticalValue",
"actualValue",
"executionTimeMillis",
"learningCurve"


def load_randomized_algos():
    try:
        return pd.read_feather("randomized_out.feather")
    except IOError:
        warnings.warn("Could not find optimized feather file, using csv")
        df = pd.read_csv(RANDOMIZED_ALGORITHMS_OUT_FN, sep=",", header=0)
        #df.to_feather("randomized_out.feather")
        return df

def load_neural_networks():
    return pd.read_csv(NN_OUT_FN, sep=",", header=0)

def load_neural_networks_training():
    return pd.read_csv(NN_TRAINING_OUT_FN, sep=",", header=0)


if __name__ == "__main__":
    df = load_neural_networks_training()

    pass
