package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 0:23
 */
record FinalFields(int i) {
    int timesTen() {
        return i * 10;
    }

    void tryToChange() {
        // i++; // Error: Cannot assign a value to final variable 'i'
    }
}
