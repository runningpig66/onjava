package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 0:30
 * {NewFeature} Since JDK 16
 */
interface Star {
    double brightness();

    double density();
}

record RedDwarf(double brightness) implements Star {
    @Override
    public double density() {
        return 100.0;
    }
}
