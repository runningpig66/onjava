package ch23_references;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author runningpig66
 * @date 2月10日 周二
 * @time 23:37
 * P.072 §2.2 创建本地副本 §2.2.6 克隆组合对象
 */
public class DeepCopyTest {
    @Test
    public void testClone() {
        OceanReading reading = new OceanReading(33.9, 100.5);
        // Now clone it:
        OceanReading clone = reading.clone();
        TemperatureReading tr = clone.getTemperatureReading();
        tr.setTemperature(tr.getTemperature() + 1);
        // clone.setTemperatureReading(tr);
        DepthReading dr = clone.getDepthReading();
        dr.setDepth(dr.getDepth() + 1);
        // clone.setDepthReading(dr);
        assertEquals("temperature: 33.9, depth: 100.5", reading.toString());
        assertEquals("temperature: 34.9, depth: 101.5", clone.toString());
    }
}
