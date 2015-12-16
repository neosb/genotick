package com.alphatica.genotick.genotick;

import com.alphatica.genotick.data.DataSetName;

public class Outcome {

    final private Prediction prediction;
    final private double profit;
    final private DataSetName dataSetName;

    public static Outcome getOutcome(Prediction prediction, double actualChange, DataSetName dataSetName) {
        return new Outcome(prediction,prediction.getValue() * actualChange, dataSetName);
    }

    private Outcome(Prediction prediction, double profit, DataSetName dataSetName) {
        this.prediction = prediction;
        this.profit = profit;
        this.dataSetName = dataSetName;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public double getProfit() {
        return profit;
    }

    public DataSetName getDataSetName() {
        return dataSetName;
    }
}
