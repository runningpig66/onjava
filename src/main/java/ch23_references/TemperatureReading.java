package ch23_references;

/**
 * @author runningpig66
 * @date 2月10日 周二
 * @time 23:23
 * P.071 §2.2 创建本地副本 §2.2.6 克隆组合对象
 * Cloning a composed object
 */
public class TemperatureReading implements Cloneable {
    private long time;
    private double temperature;

    public TemperatureReading(double temperature) {
        time = System.currentTimeMillis();
        this.temperature = temperature;
    }

    @Override
    public TemperatureReading clone() {
        try {
            return (TemperatureReading) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temp) {
        this.temperature = temp;
    }

    @Override
    public String toString() {
        return String.valueOf(temperature);
    }
}
