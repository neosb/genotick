package com.alphatica.genotick.genotick;

import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.population.Program;
import com.alphatica.genotick.population.Result;

import java.util.List;
import java.util.Map;

public class WeightCalculatorStatic {


    public static double calculateWeight(Program program) {
        return calculateSquareOfDifference2(program);
        //return calculateSquareOfDifference(program);
        //return calculateCorrectVsIncorrectPredictions(program);
    }

    private static double calculateSquareOfDifference2(Program program) {
        Totals totals = countTotals(program);
        int positives = totals.positive;
        int negatives = totals.negative;
        boolean positive = positives > negatives;
        double weightAbs = Math.pow(positives - negatives,2);
        return positive ? weightAbs : -weightAbs;
    }

    private static Totals countTotals(Program program) {
        Totals totals = new Totals();
        Map<DataSetName, List<Result>> map = program.getResultsMap();
        for(Map.Entry<DataSetName, List<Result>> entry: map.entrySet()) {
            for(Result result: entry.getValue()) {
                if(result.getProfit() > 0) {
                    totals.positive++;
                }
                if(result.getProfit() < 0) {
                    totals.negative++;
                }
            }
        }
        return totals;
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


    private static class Totals {
        public int positive = 0;
        public int negative = 0;
    }
}
