package eu.franzoni.abagail.shared;

import java.util.Random;

public class MyRandom {
    private static Random myRandom;

    public static Random provideRandom() {
        return myRandom;
    }

    public static Random initialize(long seed) {
        myRandom = new Random(seed);
        return myRandom;
    }

}
