package com.alphatica.genotick.population;


import com.alphatica.genotick.genotick.Outcome;
import com.alphatica.genotick.genotick.ProgramResult;
import com.alphatica.genotick.instructions.InstructionList;
import com.alphatica.genotick.weight.WeightCalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program implements Serializable {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -32164662984L;

    private ProgramName name;
    private final int maximumDataOffset;
    private final List<InstructionList> instructions;
    private int totalChildren;
    private int totalPredictions;
    private double inheritedWeight;
    private int totalOutcomes;
    private long outcomesAtLastChild;
    private int predictionsUp;
    private int predictionsDown;
    @SuppressWarnings("CanBeFinal") // I don't want this final as I hope to be able to change it later via external tools.
    private WeightCalculator weightCalculator;

    public static Program createEmptyProgram(int maximumDataOffset, WeightCalculator weightCalculator) {
        return new Program(maximumDataOffset,weightCalculator);
    }

    public int getLength() {
        int length = 0;
        for(InstructionList instructionList: instructions) {
            length += instructionList.getSize();
        }
        return length;
    }

    public double getInheritedWeight() {
        return inheritedWeight;
    }

    public ProgramName getName() {
        return name;
    }

    public void setInheritedWeight(double inheritedWeight) {
        this.inheritedWeight = inheritedWeight;
    }

    private Program(int maximumDataOffset, WeightCalculator weightCalculator) {
        this.maximumDataOffset = maximumDataOffset;
        instructions = new ArrayList<>();
        this.weightCalculator = weightCalculator;
    }

    public void recordOutcomes(List<Outcome> outcomes) {
        for(Outcome outcome: outcomes) {
            totalOutcomes++;
            if(outcome.getProfit() == 0) {
                continue;
            }
            totalPredictions++;
        }
    }

    public int getTotalChildren() {
        return totalChildren;
    }

    public long getOutcomesAtLastChild() {
        return outcomesAtLastChild;
    }

    @Override
    public String toString() {
        int length = getLength();
        return "Name: " + this.name.toString()
                + " Outcomes: " + String.valueOf(totalOutcomes)
                + " Length: " + String.valueOf(length)
                + " Children: " + String.valueOf(totalChildren);
    }

    public void increaseChildren() {
        totalChildren++;
        outcomesAtLastChild = totalOutcomes;
    }

    public int getMaximumDataOffset() {
        return maximumDataOffset;
    }

    public int getTotalPredictions() {
        return totalPredictions;
    }

    public int getTotalOutcomes() {
        return totalOutcomes;
    }

    public int getBias() {
        return predictionsUp - predictionsDown;
    }

    public void setName(ProgramName name) {
        this.name = name;
    }

    public void addInstructionList(InstructionList instructionList) {
        instructions.add(instructionList);
    }

    public List<InstructionList> getInstructionLists() {
        return Collections.unmodifiableList(instructions);
    }

    public void recordResult(ProgramResult result) {
        weightCalculator.recordResult(result);
        recordBias(result);
    }

    private void recordBias(ProgramResult result) {
        switch (result.getPrediction()) {
            case UP: predictionsUp++; break;
            case DOWN: predictionsDown++; break;
        }
    }

    public WeightCalculator getWeightCalculator() {
        return weightCalculator;
    }

    public double getTotalWeight() {
        List<Double> weights = weightCalculator.getWeights();
        double sum = 0;
        for(Double weight: weights) {
            sum += weight;
        }
        return sum;
    }
}

