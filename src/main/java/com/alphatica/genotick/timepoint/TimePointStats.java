package com.alphatica.genotick.timepoint;

import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.genotick.Prediction;

import java.util.HashMap;
import java.util.Map;

public class TimePointStats {
    private final HashMap<DataSetName, SetStats> stats;
    private final TimePoint timePoint;

    private class SetStats {
        private double totalPercentPredicted;
        private double totalPercentMispredicted;

        public SetStats() {
        }

        public void update(Double actualFutureChange, Prediction prediction) {
            double profit = actualFutureChange * prediction.getValue();
            if(profit > 0) {
                totalPercentPredicted += Math.abs(actualFutureChange);
            }
            if(profit < 0) {
                totalPercentMispredicted += Math.abs(actualFutureChange);
            }
        }

        @Override
        public String toString() {
            return "Predicted %: " + String.valueOf(totalPercentPredicted - totalPercentMispredicted);
        }

        public double getTotalPercent() {
            return totalPercentPredicted - totalPercentMispredicted;
        }
    }

    public double getPercentEarned() {
        if(stats.isEmpty())
            return 0;
        double percent = 0;
        for(SetStats setStats: stats.values()) {
            percent += setStats.getTotalPercent();
        }
        return percent / stats.size();
    }

    public static TimePointStats getNewStats(TimePoint timePoint) {
        return new TimePointStats(timePoint);
    }

    private TimePointStats(TimePoint timePoint) {
        stats = new HashMap<>();
        this.timePoint = new TimePoint(timePoint);
    }

    public void update(DataSetName setName, Double actualFutureChange, Prediction prediction) {
        if(actualFutureChange == null || prediction == null)
            return;
        if(actualFutureChange.isNaN() || actualFutureChange.isInfinite())
            return;
        SetStats stats = getSetStats(setName);
        stats.update(actualFutureChange,prediction);
    }

    private SetStats getSetStats(DataSetName setName) {
        SetStats stat = stats.get(setName);
        if(stat == null) {
            stat = new SetStats();
            stats.put(setName,stat);
        }
        return stat;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stats for time point: ");
        sb.append(timePoint.toString());
        sb.append(System.lineSeparator());
        for(Map.Entry<DataSetName,SetStats> entry: stats.entrySet()) {
            appendStats(sb,entry);
        }
        double percentPredicted = 0;
        for(Map.Entry<DataSetName, SetStats> entry: stats.entrySet()) {
            percentPredicted += entry.getValue().getTotalPercent();
        }
        sb.append("Total percent predicted: ");
        sb.append(percentPredicted);
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    private void appendStats(StringBuilder sb, Map.Entry<DataSetName, SetStats> entry) {
        sb.append("Data set: ");
        sb.append(entry.getKey().toString());
        sb.append(" ");
        sb.append(entry.getValue().toString());
        sb.append(System.lineSeparator());
    }

}
