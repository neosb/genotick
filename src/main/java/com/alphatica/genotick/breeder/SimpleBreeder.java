package com.alphatica.genotick.breeder;

import com.alphatica.genotick.genotick.Debug;
import com.alphatica.genotick.instructions.Instruction;
import com.alphatica.genotick.instructions.InstructionList;
import com.alphatica.genotick.mutator.Mutator;
import com.alphatica.genotick.population.Population;
import com.alphatica.genotick.population.Program;
import com.alphatica.genotick.population.ProgramInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimpleBreeder implements ProgramBreeder {
    private BreederSettings settings;
    private Mutator mutator;

    public static ProgramBreeder getInstance() {
        return new SimpleBreeder();
    }

    @Override
    public void breedPopulation(Population population, List<ProgramInfo> programInfos) {
        Debug.d("Breeding population");
        Debug.d("Current population size", population.getSize());
        addRequiredRandomPrograms(population);
        if(population.haveSpaceToBreed()) {
            breedPopulationFromParents(population,programInfos);
            addOptionalRandomPrograms(population);
        }
        Debug.d("Breeder exiting");
        Debug.d("Current population size", population.getSize());
    }

    private void addOptionalRandomPrograms(Population population) {
        int count = population.getDesiredSize() - population.getSize();
        if(count > 0) {
            fillWithPrograms(count, population);
        }
    }

    private void addRequiredRandomPrograms(Population population) {
        if(settings.randomPrograms > 0) {
            int count = (int)Math.round(settings.randomPrograms * population.getDesiredSize());
            fillWithPrograms(count,population);
        }
    }

    private void fillWithPrograms(int count, Population population) {
        for(int i = 0; i < count; i++) {
            createNewProgram(population);
        }
    }

    private void createNewProgram(Population population) {
        int instructionsCount = mutator.getNextInt() % 1024;
        InstructionList instructionList = InstructionList.createInstructionList();
        while(instructionsCount-- > 0) {
            addInstructionToMain(instructionList);
        }
        Program program = Program.createEmptyProgram(settings.dataMaximumOffset);
        program.addInstructionList(instructionList);
        population.saveProgram(program);
    }

    private void addInstructionToMain(InstructionList main) {
        Instruction instruction = mutator.getRandomInstruction();
        instruction.mutate(mutator);
        main.addInstruction(instruction);
    }
    private void breedPopulationFromParents(Population population, List<ProgramInfo> originalList) {
        List<ProgramInfo> programInfoList = new ArrayList<>(originalList);
        removeNotAllowedPrograms(programInfoList);
        Collections.sort(programInfoList, ProgramInfo.comparatorByAbsoluteWeight);
        breedPopulationFromList(population, programInfoList);
    }

    private void removeNotAllowedPrograms(List<ProgramInfo> programInfoList) {
        Iterator<ProgramInfo> iterator = programInfoList.iterator();
        while(iterator.hasNext()) {
            ProgramInfo programInfo = iterator.next();
            if(!programInfo.canBeParent(settings.minimumOutcomesToAllowBreeding, settings.outcomesBetweenBreeding))
                iterator.remove();
        }
    }

    private void breedPopulationFromList(Population population, List<ProgramInfo> list) {
        while(population.haveSpaceToBreed()) {
            Program parent1 = getPossibleParent(population, list);
            Program parent2 = getPossibleParent(population,list);
            if(parent1 == null || parent2 == null)
                break;
            Program child = Program.createEmptyProgram(settings.dataMaximumOffset);
            makeChild(parent1, parent2, child);
            population.saveProgram(child);
            parent1.increaseChildren();
            population.saveProgram(parent1);
            parent2.increaseChildren();
            population.saveProgram(parent2);
        }
    }

    private void makeChild(Program parent1, Program parent2, Program child) {
        double weight = getParentsWeight(parent1, parent2);
        child.setInheritedWeight(weight);
        mixParentsToChild(parent1,parent2,child);
    }

    private void mixParentsToChild(Program parent1, Program parent2, Program child) {
        copyInstructionsFromParent(parent1,child);
        copyInstructionsFromParent(parent2,child);
        if(child.getInstructionLists().size() == 0) {
            InstructionList list = copyInstructionList(parent1.getInstructionLists().get(0));
            child.addInstructionList(list);
        }
        Debug.d("Child instruction sets:",child.getInstructionLists().size());
    }

    private void copyInstructionsFromParent(Program parent, Program child) {
        for(InstructionList instructionList: parent.getInstructionLists()) {
            if(mutator.getNextDouble() > 0) {
                InstructionList newList = copyInstructionList(instructionList);
                child.addInstructionList(newList);
            }
        }
    }

    private InstructionList copyInstructionList(InstructionList instructionList) {
        InstructionList newInstructions = InstructionList.createInstructionList();
        for(int i = 0; i < instructionList.getSize(); i++) {
            Instruction instruction = instructionList.getInstruction(i);
            addInstructionToInstructionList(instruction,newInstructions);
        }
        return newInstructions;
    }

    private double getParentsWeight(Program parent1, Program parent2) {
        return settings.inheritedWeightPercent * (parent1.getWeight() + parent2.getWeight()) / 2;
    }

    private void addInstructionToInstructionList(Instruction instruction, InstructionList instructionList) {
        if(!mutator.skipNextInstruction()) {
            if (mutator.getAllowNewInstruction()) {
                instruction = mutator.getRandomInstruction();
                instructionList.addInstruction(instruction);
            }
            if (mutator.getAllowInstructionMutation()) {
                instruction.mutate(mutator);
            }
            instructionList.addInstruction(instruction);
        }
    }

    private Program getPossibleParent(Population population, List<ProgramInfo> list) {
        double totalWeight = sumTotalWeight(list);
        double target = totalWeight * mutator.getNextDouble();
        double weightSoFar = 0;
        Iterator<ProgramInfo> iterator = list.iterator();
        while(iterator.hasNext()) {
            ProgramInfo programInfo = iterator.next();
            weightSoFar += Math.abs(programInfo.getWeight());
            if(weightSoFar >= target) {
                iterator.remove();
                return population.getProgram(programInfo.getName());
            }
        }
        return null;
    }

    private double sumTotalWeight(List<ProgramInfo> list) {
        double weight = 0;
        for(ProgramInfo programInfo: list) {
            weight += Math.abs(programInfo.getWeight());
        }
        return weight;
    }

    @Override
    public void setSettings(BreederSettings breederSettings, Mutator mutator) {
        this.settings = breederSettings;
        this.mutator = mutator;
    }
}
