package com.alphatica.genotick.population;

import com.alphatica.genotick.timepoint.TimePoint;

import java.io.Serializable;

class ResultHolder implements Serializable {

    private static final long serialVersionUID = -7391322212327425428L;
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
