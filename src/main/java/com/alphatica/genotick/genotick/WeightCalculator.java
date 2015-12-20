package com.alphatica.genotick.genotick;

import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.population.Program;
import com.alphatica.genotick.population.Result;

import java.util.List;
import java.util.Map;

public class WeightCalculator {


    public static double calculateWeight(Program program) {
        return calculateSquareOfDifference2(program);
        //return calculateSquareOfDifference(program);
        //return calculateCorrectVsIncorrectPredictions(program);
    }

    private static double calculateSquareOfDifference2(Program program) {
        int positiveCount = getPositiveCount(program);
        int negativeCount = getNegativeCount(program);
        boolean positive = positiveCount > negativeCount;
        double weightAbs = Math.pow(positiveCount - negativeCount,2);
        return positive ? weightAbs : -weightAbs;
    }

    private static int getPositiveCount(Program program) {
        Map<DataSetName, List<Result>> map = program.getResultsMap();
        int total = 0;
        for(Map.Entry<DataSetName, List<Result>> entry: map.entrySet()) {
            total += getPositives(entry.getValue());
        }
        return total;
    }

    private static int getPositives(List<Result> results) {
        int total = 0;
        for(Result result : results) {
            if(result.getProfit() > 0)
                total++;
        }
        return total;
    }

    private static int getNegativeCount(Program program) {
        Map<DataSetName, List<Result>> map = program.getResultsMap();
        int total = 0;
        for(Map.Entry<DataSetName, List<Result>> entry: map.entrySet()) {
            total += getNegatives(entry.getValue());
        }
        return total;
    }

    private static int getNegatives(List<Result> results) {
        int total = 0;
        for(Result result : results) {
            if(result.getProfit() < 0)
                total++;
        }
        return total;
    }

    @SuppressWarnings("unused")
    private static double calculateCorrectVsIncorrectPredictions(Program program) {
        int totalPrediction = program.getTotalPredictions();
        if(totalPrediction == 0)
            return 0;
        int correct = program.getCorrectPredictions();
        int incorrect = program.getTotalPredictions() - correct;
        return correct - incorrect;
    }

    private static double calculateSquareOfDifference(Program program) {
        int correct = program.getCorrectPredictions();
        int incorrect = program.getTotalPredictions() - correct;
        boolean positive = correct > incorrect;
        double weightAbs = Math.pow(correct - incorrect,2);
        return positive ? weightAbs : -weightAbs;
    }



}
