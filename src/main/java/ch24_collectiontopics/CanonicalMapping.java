package ch24_collectiontopics;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 0:30
 * P.154 §3.14 持有引用 §WeakHashMap
 * Demonstrates WeakHashMap
 */
public class CanonicalMapping {
    static void showKeys(Map<String, String> m) {
        // Display sorted keys
        List<String> keys = new ArrayList<>(m.keySet());
        Collections.sort(keys);
        System.out.println(keys);
    }

    public static void main(String[] args) {
        int size = 100;
        String[] savedKeys = new String[size];
        WeakHashMap<String, String> map = new WeakHashMap<>();
        for (int i = 0; i < size; i++) {
            String key = String.format("%03d", i);
            String value = Integer.toString(i);
            if (i % 3 == 0) {
                savedKeys[i] = key; // Save as "real" references
            }
            map.put(key, value);
        }
        showKeys(map);
        System.gc();
        showKeys(map);
        System.out.println(Arrays.toString(savedKeys));
    }
}
/* Output:
[000, 001, 002, 003, 004, 005, 006, 007, 008, 009, 010, 011, 012, 013, 014, 015, 016, 017, 018, 019, 020, 021, 022, 023, 024, 025, 026, 027, 028, 029, 030, 031, 032, 033, 034, 035, 036, 037, 038, 039, 040, 041, 042, 043, 044, 045, 046, 047, 048, 049, 050, 051, 052, 053, 054, 055, 056, 057, 058, 059, 060, 061, 062, 063, 064, 065, 066, 067, 068, 069, 070, 071, 072, 073, 074, 075, 076, 077, 078, 079, 080, 081, 082, 083, 084, 085, 086, 087, 088, 089, 090, 091, 092, 093, 094, 095, 096, 097, 098, 099]
[000, 003, 006, 009, 012, 015, 018, 021, 024, 027, 030, 033, 036, 039, 042, 045, 048, 051, 054, 057, 060, 063, 066, 069, 072, 075, 078, 081, 084, 087, 090, 093, 096, 099]
[000, null, null, 003, null, null, 006, null, null, 009, null, null, 012, null, null, 015, null, null, 018, null, null, 021, null, null, 024, null, null, 027, null, null, 030, null, null, 033, null, null, 036, null, null, 039, null, null, 042, null, null, 045, null, null, 048, null, null, 051, null, null, 054, null, null, 057, null, null, 060, null, null, 063, null, null, 066, null, null, 069, null, null, 072, null, null, 075, null, null, 078, null, null, 081, null, null, 084, null, null, 087, null, null, 090, null, null, 093, null, null, 096, null, null, 099]
 */
