package com.alphatica.genotick.timepoint;

import com.alphatica.genotick.genotick.DataSetExecutor;
import com.alphatica.genotick.genotick.ProgramData;
import com.alphatica.genotick.population.Population;
import com.alphatica.genotick.population.ProgramInfo;
import com.alphatica.genotick.processor.ProgramExecutorFactory;

import java.util.List;

public interface TimePointExecutor {
    List<ProgramInfo> getProgramInfos();

    TimePointResult execute(TimePoint timePoint,
                            List<ProgramData> programDataList,
                            Population population,
                            boolean updatePrograms);

    void setSettings(DataSetExecutor dataSetExecutor, ProgramExecutorFactory programExecutorFactory);
}
