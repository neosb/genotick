package com.alphatica.genotick.genotick;

import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.timepoint.TimePoint;

public class ProgramResult {

    private final TimePoint timePoint;
    private final DataSetName setName;
    private final Prediction prediction;
    private final Double actualChange;
    private final Double weight;


    public ProgramResult(TimePoint timePoint, DataSetName setName, Prediction prediction, Double actualChange, double weight) {
        this.timePoint = timePoint;
        this.setName = setName;
        this.actualChange = actualChange;
        this.prediction = prediction;
        this.weight = weight;
    }

    public TimePoint getTimePoint() {
        return timePoint;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public Double getWeight() {
        return weight;
    }

    public DataSetName getSetName() {
        return setName;
    }

    public Double getActualChange() {
        return actualChange;
    }
}
