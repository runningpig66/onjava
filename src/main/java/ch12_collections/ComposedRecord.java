package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 0:26
 * {NewFeature} Since JDK 16
 */
record Company(Employee[] e) {
}

//class Conglomerate extends Company {}
// error: cannot inherit from final Company
