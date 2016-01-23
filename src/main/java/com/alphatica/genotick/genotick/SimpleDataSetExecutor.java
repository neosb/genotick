package com.alphatica.genotick.genotick;

import com.alphatica.genotick.population.Program;
import com.alphatica.genotick.population.ProgramExecutor;
import com.alphatica.genotick.timepoint.TimePoint;

import java.util.ArrayList;
import java.util.List;

public class  SimpleDataSetExecutor implements DataSetExecutor {


    @Override
    public List<ProgramResult> execute(TimePoint timePoint, List<ProgramData> programDataList, Program program, ProgramExecutor programExecutor) {
        List<ProgramResult> programResultList = new ArrayList<>(programDataList.size());
        for(ProgramData programData: programDataList) {
            double weight = program.getInheritedWeight() + program.getTotalWeight();
            Prediction prediction = programExecutor.executeProgram(programData,program);
            ProgramResult result = new ProgramResult(timePoint, programData.getName(), prediction, programData.getActualChange(), weight);
            programResultList.add(result);
        }
        return programResultList;
    }

}
