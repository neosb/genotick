package com.alphatica.genotick.population;

import com.alphatica.genotick.timepoint.TimePoint;

class ResultHolder {
    private final TimePoint timePoint;
    private final double profit;

    public ResultHolder(TimePoint timePoint, double profit) {
        this.timePoint = timePoint;
        this.profit = profit;
    }

    public TimePoint getTimePoint() {
        return timePoint;
    }

    public double getProfit() {
        return profit;
    }
}
