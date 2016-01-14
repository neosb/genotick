package com.alphatica.genotick.weight;

import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.genotick.Prediction;
import com.alphatica.genotick.genotick.ProgramResult;

import java.util.Collections;
import java.util.List;

public class PositiveVsNegativesSquared implements WeightCalculator {

    private static final long serialVersionUID = -4809772601910874293L;
    private double weight;
    private int totalProfits;
    private int totalLosses;

    @Override
    public double getWeight(DataSetName name) {
        return weight;
    }

    @Override
    public List<Double> getWeights() {
        return Collections.singletonList(weight);
    }

    @Override
    public void recordResult(ProgramResult result) {
        if(result.getPrediction() == Prediction.OUT || result.getActualChange().isNaN()) {
            return;
        }
        double profit = result.getActualChange() * result.getPrediction().getValue();
        if(profit > 0) {
            totalProfits++;
        } else if (profit < 0) {
            totalLosses++;
        }
        weight = recalculate();
    }

    @Override
    public String getDescription() {
        weight = recalculate();
        return "Weight: " + String.valueOf(weight);
    }

    private double recalculate() {
        boolean positive = totalProfits > totalLosses;
        double w = Math.pow(totalProfits - totalLosses, 2);
        return positive ? w : -w;
    }
}
