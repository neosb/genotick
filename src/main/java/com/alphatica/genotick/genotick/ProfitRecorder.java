package com.alphatica.genotick.genotick;

public class ProfitRecorder {
    double account = 1;
    private double maxAccount = account;
    private double drawdown;
    private double maxDrawdown;

    public double getProfit() {
        return 100 * (account - 1);
    }

    public double getMaxDrawdown() {
        return maxDrawdown;
    }

    public void recordProfit(double percentEarned) {
        double change = (percentEarned / 100) + 1;
        account *= change;
        if(account > maxAccount) {
            drawdown = 0;
            maxAccount = account;
            return;
        }
        drawdown = 100 * Math.abs(account - maxAccount) / maxAccount;
        if(drawdown > maxDrawdown) {
            maxDrawdown = drawdown;
        }
    }
}
