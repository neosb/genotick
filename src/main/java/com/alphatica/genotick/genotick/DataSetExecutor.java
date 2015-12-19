package com.alphatica.genotick.genotick;

import com.alphatica.genotick.population.*;
import com.alphatica.genotick.timepoint.TimePoint;

import java.util.List;

public interface DataSetExecutor {

    List<ProgramResult> execute(TimePoint timePoint, List<ProgramData> programDataList, Program program, ProgramExecutor programExecutor);

}
