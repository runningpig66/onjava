package ch23_references;

/**
 * @author runningpig66
 * @date 2月10日 周二
 * @time 23:29
 * P.071 §2.2 创建本地副本 §2.2.6 克隆组合对象
 * Cloning a composed object
 */
public class OceanReading implements Cloneable {
    private DepthReading depth;
    private TemperatureReading temperature;

    public OceanReading(double tdata, double ddata) {
        temperature = new TemperatureReading(tdata);
        depth = new DepthReading(ddata);
    }

    @Override
    public OceanReading clone() {
        OceanReading or;
        try {
            or = (OceanReading) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        // Must clone references:
        or.depth = or.depth.clone();
        or.temperature = or.temperature.clone();
        return or;
    }

    public TemperatureReading getTemperatureReading() {
        return temperature;
    }

    public void setTemperatureReading(TemperatureReading tr) {
        temperature = tr;
    }

    public DepthReading getDepthReading() {
        return depth;
    }

    public void setDepthReading(DepthReading dr) {
        this.depth = dr;
    }

    @Override
    public String toString() {
        return "temperature: " + temperature + ", depth: " + depth;
    }
}
