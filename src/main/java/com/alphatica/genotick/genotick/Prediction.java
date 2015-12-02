package com.alphatica.genotick.genotick;

public enum Prediction {
    UP(1),
    DOWN(-1),
    OUT(0);
    private final double value;

    Prediction(double value) {
        this.value = value;
    }
    public static Prediction getPrediction(double forecast) {
        if(forecast > 0)
            return Prediction.UP;
        if(forecast < 0)
            return Prediction.DOWN;
        return Prediction.OUT;
    }

    public boolean isCorrect(Double actualFutureChange) {
        return actualFutureChange * value > 0;
    }

    public double getValue() {
        return value;
    }
}
