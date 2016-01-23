package com.alphatica.genotick.weight;

public class WeightCalculatorFactory {

    public static WeightCalculator getDefaultWeightCalculator() {
        return new PositiveVsNegativesSquared();
    }
}
