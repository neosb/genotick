package com.alphatica.genotick.population;

import com.alphatica.genotick.instructions.Instruction;
import com.alphatica.genotick.instructions.InstructionList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ProgramPrinter {
    public static String showProgram(Program program) throws IllegalAccessException {
            StringBuilder sb = new StringBuilder();
            addFields(program,sb);
            addWeight(program,sb);
            addFunctions(program,sb);
            return sb.toString();
    }

    private static void addWeight(Program program, StringBuilder sb) {
        sb.append("Weight:").append(System.lineSeparator());
        sb.append(program.getWeightCalculator().getDescription()).append(System.lineSeparator());
    }
    private static void addFunctions(Program program, StringBuilder sb) throws IllegalAccessException {
        for(InstructionList instructionList: program.getInstructionLists()) {
            addInstructionList(instructionList,sb);
        }
    }

    private static void addInstructionList(InstructionList instructionList, StringBuilder sb) throws IllegalAccessException {
        sb.append("Instruction list:").append("\n");
        sb.append("Variable count: ").append(instructionList.getVariablesCount()).append(System.lineSeparator());
        for(int i = 0; i < instructionList.getSize(); i++) {
            Instruction instruction = instructionList.getInstruction(i);
            sb.append(instruction.instructionString()).append(System.lineSeparator());
        }
    }

    private static void addFields(Program program, StringBuilder sb) throws IllegalAccessException {
        Field[] fields = program.getClass().getDeclaredFields();
        for(Field field: fields) {
            if(field.getName().equals("instructions"))
                continue;
            if(field.getName().equals("resultsMap"))
                continue;
            field.setAccessible(true);
            if(!Modifier.isStatic(field.getModifiers())) {
                sb.append(field.getName()).append(" ").
                        append(field.get(program).toString()).append(System.lineSeparator());
            }
        }
    }

}
