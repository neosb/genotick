import com.alphatica.genotick.genotick.DataSetResult;
import com.alphatica.genotick.genotick.Prediction;
import com.alphatica.genotick.genotick.ProgramResult;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class DataSetResultTest {
    ProgramResult upOne = new ProgramResult(null,null,Prediction.UP,0.0,1);
    ProgramResult upTwo = new ProgramResult(null,null,Prediction.UP,0.0,2);
    ProgramResult upNegativeThree = new ProgramResult(null,null,Prediction.UP,0.0,-3);

    ProgramResult downOne = new ProgramResult(null,null,Prediction.DOWN,0.0,1);
    ProgramResult downTwo = new ProgramResult(null,null,Prediction.DOWN,0.0,2);
    ProgramResult downNegativeThree = new ProgramResult(null,null,Prediction.DOWN,0.0,-3);

    // Return prediction out if empty set
    @Test
    public void testEmpty() {
        DataSetResult dsr = createDataSetResult();
        assertTrue(dsr.getCumulativePrediction() == Prediction.OUT);
    }

    // Return UP if only one positive weight
    @Test
    public void testSimpleUp() {
        DataSetResult dsr = createDataSetResult(upOne);
        assertTrue(dsr.getCumulativePrediction() == Prediction.UP);
    }

    // Return UP if more than one positive weight
    @Test
    public void testTwoUp() {
        DataSetResult dsr = createDataSetResult(upOne,upTwo);
        assertTrue(dsr.getCumulativePrediction() == Prediction.UP);
    }

    // Return DOWN if one positive weight
    @Test
    public void testSimpleDown() {
        DataSetResult dsr = createDataSetResult(downOne);
        assertTrue(dsr.getCumulativePrediction() == Prediction.DOWN);
    }

    // Return DOWN if two positive weight
    @Test
    public void testTwoDown() {
        DataSetResult dsr =createDataSetResult(downOne,downTwo);
        assertTrue(dsr.getCumulativePrediction() == Prediction.DOWN);
    }

    // Return OUT if cancel each other out
    @Test
    public void testNeutralSimple() {
        DataSetResult dsr = createDataSetResult(upOne,upTwo,downOne,downTwo);
        assertTrue(dsr.getCumulativePrediction() == Prediction.OUT);
    }

    // Check all negative
    @Test
    public void testAllNegative() {
        DataSetResult dsr = createDataSetResult(upNegativeThree,upNegativeThree,downNegativeThree);
        assertTrue(dsr.getCumulativePrediction() == Prediction.DOWN);
    }

    // Check mix
    @Test
    public void testMix() {
        DataSetResult dsr = createDataSetResult(upOne,upTwo,downNegativeThree, // +6 so far
                downTwo,downTwo,upNegativeThree); // -7
        assertTrue(dsr.getCumulativePrediction() == Prediction.DOWN);
    }


    private DataSetResult createDataSetResult(ProgramResult... array) {
        DataSetResult dsr = new DataSetResult(null);
        for(ProgramResult result: array) {
            dsr.addResult(result);
        }
        return dsr;
    }
}
