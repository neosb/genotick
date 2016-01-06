package com.alphatica.genotick.ui;


import com.alphatica.genotick.timepoint.TimePoint;

class ConsoleOutput implements UserOutput {
    @Override
    public void errorMessage(String message) {
        System.out.println("Error: " + message);
    }

    @Override
    public void warningMessage(String message) {
        System.out.println("Warning: " + message);
    }

    @Override
    public void reportProfitForTimePoint(TimePoint timePoint, double cumulativeProfit, double timePointProfit) {
        System.out.println("Profit for " + timePoint.toString() + ": "
            + "Cumulative profit: " + cumulativeProfit + " "
            + "TimePoint's profit: " + timePointProfit);
    }
}
