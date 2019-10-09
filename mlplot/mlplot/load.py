import numpy as np
import pandas as pd
import os
from pyarrow import ArrowIOError
import warnings

RANDOMIZED_ALGORITHMS_OUT_FN = os.path.join(os.path.dirname(os.path.realpath(__file__)), os.pardir, os.pardir,
                                            "experiments_output", "randomized_algorithms",
                                            "randomized_algorithms_experiment_output.csv.gz")


def load():
    try:
        return pd.read_feather("randomized_out.feather")
    except IOError:
        warnings.warn("Could not find optimized feather file, using csv")
        return pd.read_csv(RANDOMIZED_ALGORITHMS_OUT_FN, sep=",", header=0)

if __name__ == "__main__":
    x = load()
    pass
