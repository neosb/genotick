package com.alphatica.genotick.reversal;

import com.alphatica.genotick.data.DataSet;
import com.alphatica.genotick.data.DataSetName;
import com.alphatica.genotick.data.FileSystemDataLoader;
import com.alphatica.genotick.data.MainAppData;
import com.alphatica.genotick.ui.UserOutput;

import java.io.*;
import java.util.*;

public class Reversal {
    private final String reverseValue;
    private final UserOutput output;

    public Reversal(String reverseValue, UserOutput output) {
        this.reverseValue = reverseValue;
        this.output = output;
    }

    public void reverse() {
        FileSystemDataLoader loader = new FileSystemDataLoader(reverseValue);
        MainAppData data = loader.createProgramData();
        reverseData(data);
    }

    private void reverseData(MainAppData data) {
        for (DataSet set : data.listSets()) {
            reverseSet(set);
        }
    }

    private void reverseSet(DataSet set) {
        String reverseFileName = getReverseFileName(set.getName());
        File reverseFile = new File(reverseFileName);
        if(reverseFile.exists()) {
            output.warningMessage("File " + reverseFileName + " already exists. Not reversing " + set.getName());
            return;
        }
        List<Number[]> original = getOriginalNumbers(set);
        List<Number[]> reverse = reverseList(original);
        writeReverseToFile(reverse, reverseFileName);
    }

    private String getReverseFileName(DataSetName name) {
        return reverseValue + File.separator + "reverse_" + name;
    }

    private List<Number[]> reverseList(List<Number[]> original) {
        List<Number []> reverse = new ArrayList<>();
        Number [] lastOriginal = null;
        Number [] lastReversed = null;
        for(Number[] table: original) {
            Number[] last = reverseLineOHLCV(table, lastOriginal, lastReversed);
            reverse.add(last);
            lastOriginal = table;
            lastReversed = last;
        }
        return reverse;
    }

    /*
     * This method is for reversing traditional open-high-low-close stock market data.
     * What happens with numbers (by column):
     * 0 - TimePoint: Doesn't change.
     * 1 - Open: It goes opposite direction to original, by the same percent.
     * 2 and 3 - High and Low: First of all they swapped. This is because data should be a mirror reflection of
     * original, so high becomes low and low becomes high. Change is calculated comparing to open column
     * (column 1). So it doesn't matter what High was in previous TimePoint, it matters how much higher it was comparing
     * to the open on the same line. When High becomes low - it goes down by same percent as original high was higher
     * than open.
     * 4 - Close. Goes opposite to original open by the same percent.
     * 5 and more - Volume, open interest or whatever. These don't change.
     */

    private Number[] reverseLineOHLCV(Number[] table, Number[] lastOriginal, Number[] lastReversed) {
        Number [] reversed = new Number[table.length];
        // Column 0 is unchanged
        reversed[0] = table[0];
        // Column 1. Rewrite if first line
        if(lastOriginal == null) {
            reversed[1] = table[1];
        } else {
            // Change by % if not first line
            reversed[1] = getReverseValue(table[1],lastOriginal[1],lastReversed[1]);
        }
        // Check if 4 columns here, because we need time, open, high, low to do swapping later.
        if(table.length < 4)
            return reversed;
        // Column 2. Change by % comparing to open
        // Write into 3 - we swap 2 & 3
        reversed[3] = getReverseValue(table[2], table[1], reversed[1]);
        // Column 3. Change by % comparing to open
        // Write into 2 - we swap 2 & 3
        reversed[2] = getReverseValue(table[3],table[1],reversed[1]);
        if(table.length == 4)
            return reversed;
        // Column 4. Change by % comparing to open.
        reversed[4] = getReverseValue(table[4],table[1],reversed[1]);
        // Rewrite rest
        System.arraycopy(table, 5, reversed, 5, table.length - 5);
        return reversed;
    }

    private Number getReverseValue(Number from, Number to, Number compare) {
        double diff = Math.abs((from.doubleValue() / to.doubleValue()) -2);
        return diff * compare.doubleValue();
    }

    private void writeReverseToFile(List<Number[]> reverse, String reversedFileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reversedFileName))) {
            for(Number[] table: reverse) {
                String row = mkString(table,",");
                bw.write(row + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String mkString(Number[] table, @SuppressWarnings("SameParameterValue") String string) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Number number: table) {
            sb.append(number);
            count++;
            if(count < table.length) {
                sb.append(string);
            }
        }
        return sb.toString();
    }

    private List<Number[]> getOriginalNumbers(DataSet set) {
        List<Number[]> list = new ArrayList<>();
        for (int i = 0; i < set.getLinesCount(); i++) {
            list.add(set.getLine(i));
        }
        return list;
    }
}
