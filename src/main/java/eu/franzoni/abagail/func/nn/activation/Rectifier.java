package eu.franzoni.abagail.func.nn.activation;

public class Rectifier extends DifferentiableActivationFunction {
    @Override
    public double derivative(double value) {
        return (value > 0.0) ? 1.0 : 0.0;
    }

    @Override
    public double value(double value) {
        return Math.max(value, 0.0);
    }
}
