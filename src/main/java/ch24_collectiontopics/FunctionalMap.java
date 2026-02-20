package ch24_collectiontopics;

import onjava.HTMLColors;

import java.util.Map;

/**
 * @author runningpig66
 * @date 2月16日 周一
 * @time 23:00
 * P.104 §3.4 在 Map 上使用函数式操作
 * Functional operations on a Map
 */
public class FunctionalMap {
    public static void main(String[] args) {
        HTMLColors.MAP.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(v -> v.startsWith("Dark"))
                .map(v -> v.replaceFirst("Dark", "Hot"))
                .forEach(System.out::println);
    }
}
/* Output:
HotBlue
HotCyan
HotGoldenRod
HotGray
HotGreen
HotKhaki
HotMagenta
HotOliveGreen
HotOrange
HotOrchid
HotRed
HotSalmon
HotSeaGreen
HotSlateBlue
HotSlateGray
HotTurquoise
HotViolet
 */
