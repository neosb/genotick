package com.alphatica.genotick.timepoint;

import java.io.Serializable;

public class TimePoint implements Comparable<TimePoint>, Serializable {


    private static final long serialVersionUID = -6541300869299964665L;
    private final long value;
    public TimePoint(long i) {
        this.value = i;
    }

    public TimePoint(TimePoint startTimePoint) {
        this(startTimePoint.value);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") TimePoint timePoint) {
        return (int)(this.value - timePoint.value);
    }

    public long getValue() {
        return value;
    }

    public TimePoint next() {
        return new TimePoint(value + 1);
    }
}
