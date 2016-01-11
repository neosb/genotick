package com.alphatica.genotick.population;


import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.genotick.Outcome;
import com.alphatica.genotick.genotick.ProgramResult;
import com.alphatica.genotick.instructions.Instruction;
import com.alphatica.genotick.instructions.InstructionList;
import com.alphatica.genotick.timepoint.TimePoint;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Program implements Serializable {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -32164662984L;

    private ProgramName name;
    private final int maximumDataOffset;
    private final List<InstructionList> instructions;
    private int totalChildren;
    private int totalPredictions;
    private int correctPredictions;
    private double inheritedWeight;
    private int totalOutcomes;
    private long outcomesAtLastChild;
    private int predictionsUp;
    private int predictionsDown;
    private final Map<DataSetName,List<Result>> resultsMap;
    private double profitSum;
    private int profitCount;
    private double lossSum;
    private int lossCount;

    public static Program createEmptyProgram(int maximumDataOffset) {
        return new Program(maximumDataOffset);
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

    private Program(int maximumDataOffset) {
        this.maximumDataOffset = maximumDataOffset;
        instructions = new ArrayList<>();
        resultsMap = new HashMap<>();
    }

    public void recordOutcomes(List<Outcome> outcomes) {
        for(Outcome outcome: outcomes) {
            totalOutcomes++;
            if(outcome.getProfit() == 0) {
                continue;
            }
            if(outcome.getProfit() > 0)
                correctPredictions++;
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
    public int getCorrectPredictions() {
        return correctPredictions;
    }

    public int getBias() {
        return predictionsUp - predictionsDown;
    }

    public void setName(ProgramName name) {
        this.name = name;
    }

    public String showProgram() throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        addFields(sb);
        addResults(sb);
        addFunctions(sb);
        return sb.toString();
    }

    public double getProfitSum() {
        return profitSum;
    }

    public int getProfitCount() {
        return profitCount;
    }

    public double getLossSum() {
        return lossSum;
    }

    public int getLossCount() {
        return lossCount;
    }

    private void addFunctions(StringBuilder sb) throws IllegalAccessException {
        for(InstructionList instructionList: instructions) {
            addInstructionList(instructionList,sb);
        }
    }
    private void addResults(StringBuilder sb) {
        for(Map.Entry<DataSetName,List<Result>> entry: resultsMap.entrySet()) {
            showResultList(sb,entry.getKey(),entry.getValue());
        }
    }

    private void showResultList(StringBuilder sb, DataSetName dataSetName, List<Result> results) {
        sb.append("Results for ")
                .append(dataSetName.toString())
                .append("\n");
        for(Result result : results) {
            sb.append(result.getTimePoint().toString())
                    .append(" ")
                    .append(result.getProfit())
                    .append("\n");
        }
    }


    private void addInstructionList(InstructionList instructionList, StringBuilder sb) throws IllegalAccessException {
        sb.append("Instruction list:").append("\n");
        sb.append("Variable count: ").append(instructionList.getVariablesCount()).append("\n");
        for(int i = 0; i < instructionList.getSize(); i++) {
            Instruction instruction = instructionList.getInstruction(i);
            sb.append(instruction.instructionString()).append("\n");
        }
    }

    private void addFields(StringBuilder sb) throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field: fields) {
            if(field.getName().equals("instructions"))
                continue;
            if(field.getName().equals("resultsMap"))
                continue;
            field.setAccessible(true);
            if(!Modifier.isStatic(field.getModifiers())) {
                sb.append(field.getName()).append(" ").
                        append(field.get(this).toString()).append("\n");
            }
        }
    }

    public void addInstructionList(InstructionList instructionList) {
        instructions.add(instructionList);
    }

    public List<InstructionList> getInstructionLists() {
        return Collections.unmodifiableList(instructions);
    }

    public void recordResult(ProgramResult result) {
        recordBias(result);
        Double profit = result.getActualChange() * result.getPrediction().getValue();
        if(!profit.isNaN())
            recordProfit(result.getTimePoint(), result.getSetName(),profit);
    }

    private void recordProfit(TimePoint timePoint, DataSetName setName, double profit) {
        List<Result> results = getResultFor(setName);
        results.add(new Result(timePoint,profit));
        if(profit > 0) {
            profitSum += profit;
            profitCount++;
        } else if(profit < 0) {
            lossSum -= profit;
            lossCount++;
        }
    }

    private List<Result> getResultFor(DataSetName setName) {
        List<Result> list = resultsMap.get(setName);
        if(list == null) {
            list = new ArrayList<>();
            resultsMap.put(setName,list);
        }
        return list;
    }

    private void recordBias(ProgramResult result) {
        switch (result.getPrediction()) {
            case UP: predictionsUp++; break;
            case DOWN: predictionsDown++; break;
        }
    }

    public Map<DataSetName, List<Result>> getResultsMap() {
        return Collections.unmodifiableMap(resultsMap);
    }
}

