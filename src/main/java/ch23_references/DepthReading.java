package ch23_references;

/**
 * @author runningpig66
 * @date 2月10日 周二
 * @time 13:06
 * P.070 §2.2 创建本地副本 §2.2.6 克隆组合对象
 * Cloning a composed object
 */
public class DepthReading implements Cloneable {
    private double depth;

    public DepthReading(double depth) {
        this.depth = depth;
    }

    @Override
    public DepthReading clone() {
        try {
            return (DepthReading) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return String.valueOf(depth);
    }
}
