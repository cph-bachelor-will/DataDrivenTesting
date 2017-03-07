package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author William Bech
 */

@RunWith(Parameterized.class)
public class RecognitionAllIT {

    File snapshot;
    String expectedNumberPlate;

    public RecognitionAllIT(File snapshot, String expectedNumberPlate) {
        this.snapshot = snapshot;
        this.expectedNumberPlate = expectedNumberPlate;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testDataCreator() throws Exception {
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream(new File(resultsPath));

        Properties properties = new Properties();
        properties.load(resultsStream);
        resultsStream.close();
        assertTrue(properties.size() > 0);

        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();

        Collection<Object[]> dataForOneImage = new ArrayList();
        for (File file : snapshots) {
            String name = file.getName();
            String plateExpected = properties.getProperty(name);
            dataForOneImage.add(new Object[]{file, plateExpected});
        }
        return dataForOneImage;
    }

    @Test
    public void testAllSnapshots() throws Exception {
        CarSnapshot car = new CarSnapshot(new FileInputStream(numberPlateSnapshot));

        Intelligence intel = new Intelligence();
        String numberPlate = intel.recognize(car, false);

        assertThat(expectedNumberPlate, equalTo(numberPlate));
    }

}
