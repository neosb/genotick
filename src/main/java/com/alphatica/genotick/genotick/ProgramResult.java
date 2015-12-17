package com.alphatica.genotick.genotick;

import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.population.Program;
import com.alphatica.genotick.population.ProgramName;

public class ProgramResult {

    private final Prediction prediction;
    private final ProgramName name;
    private final Double weight;
    private final Double actualChange;
    private final DataSetName setName;

    public ProgramResult(Program program, DataSetName setName, Prediction prediction, Double actualChange) {

        this.prediction = prediction;
        this.name = program.getName();
        this.weight = program.getWeight();
        this.setName = setName;
        this.actualChange = actualChange;
    }

    @Override
    public String toString() {
        return "Name: " + name.toString() + " Prediction: " + prediction.toString() + " Weight: " + String.valueOf(weight);
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
