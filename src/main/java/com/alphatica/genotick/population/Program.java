package com.alphatica.genotick.population;


import com.alphatica.genotick.genotick.Outcome;
import com.alphatica.genotick.genotick.Prediction;
import com.alphatica.genotick.genotick.WeightCalculator;
import com.alphatica.genotick.instructions.Instruction;
import com.alphatica.genotick.instructions.InstructionList;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program implements Serializable {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -32164662984L;
    private static final DecimalFormat weightFormat = new DecimalFormat("0.00");

    private ProgramName name;
    private final int maximumDataOffset;
    private final List<InstructionList> instructions;
    private int totalChildren;
    private int totalPredictions;
    private int correctPredictions;
    private double inheritedWeight;
    private int totalOutcomes;
    private long outcomesAtLastChild;
    private int bias;

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

    public ProgramName getName() {
        return name;
    }

    public void setInheritedWeight(double inheritedWeight) {
        this.inheritedWeight = inheritedWeight;
    }

    private Program(int maximumDataOffset) {
        this.maximumDataOffset = maximumDataOffset;
        instructions = new ArrayList<>();
    }

    public void recordPrediction(Prediction prediction) {
        switch (prediction) {
            case UP: bias++; break;
            case DOWN: bias--;
        }
    }

    public void recordOutcomes(List<Outcome> outcomes) {
        for(Outcome outcome: outcomes) {
            totalOutcomes++;
            if(outcome == Outcome.OUT) {
                continue;
            }
            if(outcome == Outcome.CORRECT)
                correctPredictions++;
            totalPredictions++;
        }
    }

    public int getTotalChildren() {
        return totalChildren;
    }

    public double getWeight() {
        double earnedWeight = WeightCalculator.calculateWeight(this);
        return inheritedWeight + earnedWeight;
    }

    public long getOutcomesAtLastChild() {
        return outcomesAtLastChild;
    }



    @Override
    public String toString() {
        int length = getLength();
        return "Name: " + this.name.toString()
                + " Outcomes: " + String.valueOf(totalOutcomes)
                + " Weight: " + weightFormat.format(getWeight())
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
        return bias;
    }

    public void setName(ProgramName name) {
        this.name = name;
    }

    public String showProgram() throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        addFields(sb);
        addFunctions(sb);
        return sb.toString();
    }

    private void addFunctions(StringBuilder sb) throws IllegalAccessException {
        for(InstructionList instructionList: instructions) {
            addInstructionList(instructionList,sb);
        }
    }

    private void addInstructionList(InstructionList instructionList, StringBuilder sb) throws IllegalAccessException {
        sb.append("MainFunction:").append("\n");
        sb.append("VariableCount: ").append(instructionList.getVariablesCount()).append("\n");
        for(int i = 0; i < instructionList.getSize(); i++) {
            Instruction instruction = instructionList.getInstruction(i);
            sb.append(instruction.instructionString()).append("\n");
        }
    }

    private void addFields(StringBuilder sb) throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field: fields) {
            if(field.getName().equals("mainFunction"))
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
}
