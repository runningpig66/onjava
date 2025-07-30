package ch12_collections;

import java.util.Map;

/**
 * @author runningpig66
 * @date 2025-07-30
 * @time 下午 14:21
 * {VisuallyInspectOutput}
 */
public class EnvironmentVariables {
    public static void main(String[] args) {
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
