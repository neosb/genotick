package com.alphatica.genotick.weight;

import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.genotick.ProgramResult;

import java.io.Serializable;
import java.util.List;

public interface WeightCalculator extends Serializable{

    double getWeight(DataSetName name);

    List<Double> getWeights();

    void recordResult(ProgramResult result);

    String getDescription();
}
