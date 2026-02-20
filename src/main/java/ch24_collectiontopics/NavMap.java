package ch24_collectiontopics;

import onjava.HTMLColors;

import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static onjava.HTMLColors.*;

/**
 * @author runningpig66
 * @date 2月16日 周一
 * @time 23:13
 * P.105 §3.5 选择 Map 的部分元素
 * NavigableMap produces pieces of a Map
 * <p>
 * TreeMap 和 ConcurrentSkipListMap 都实现了 NavigableMap 接口。
 * 这个接口的目的是解决需要选择某个 Map 中部分元素的问题。下面是利用 HTMLColors 实现的一个示例：
 */
public class NavMap {
    public static final NavigableMap<Integer, String> COLORS =
            new ConcurrentSkipListMap<>(HTMLColors.MAP);

    public static void main(String[] args) {
        // 返回 Map 中按照排序规则最小的键对应的键值对 (Entry)
        show(COLORS.firstEntry());
        border();
        // 返回 Map 中按照排序规则最大的键对应的键值对 (Entry)
        show(COLORS.lastEntry());
        border();
        // 返回键值小于（或等于，由 inclusive: true 指定）"Lime" 的部分视图 (包含边界)
        NavigableMap<Integer, String> toLime = COLORS.headMap(rgb("Lime"), true);
        show(toLime);
        border();
        // 向上查找：返回大于或等于给定键的最小键所对应的键值对。此处用于演示模糊匹配
        show(COLORS.ceilingEntry(rgb("DeepSkyBlue") - 1));
        border();
        // 向下查找：返回小于或等于给定键的最大键所对应的键值对。此处用于演示模糊匹配
        show(COLORS.floorEntry(rgb("DeepSkyBlue") - 1));
        border();
        // 返回包含相同映射关系，但在迭代和导航时按键的逆序（降序）排列的逆向视图
        show(toLime.descendingMap());
        border();
        // 返回键值大于（或等于，由 inclusive: true 指定）"MistyRose" 的部分视图 (包含边界)
        show(COLORS.tailMap(rgb("MistyRose"), true));
        border();
        // 返回指定范围内的部分视图。两个 boolean 参数分别决定是否包含起始键和结束键 (此处为双闭区间 [Orchid, DarkSalmon])
        show(COLORS.subMap(rgb("Orchid"), true, rgb("DarkSalmon"), true));
    }
}
/* Output:
0x000000: Black
******************************
0xFFFFFF: White
******************************
0x000000: Black
0x000080: Navy
0x00008B: DarkBlue
0x0000CD: MediumBlue
0x0000FF: Blue
0x006400: DarkGreen
0x008000: Green
0x008080: Teal
0x008B8B: DarkCyan
0x00BFFF: DeepSkyBlue
0x00CED1: DarkTurquoise
0x00FA9A: MediumSpringGreen
0x00FF00: Lime
******************************
0x00BFFF: DeepSkyBlue
******************************
0x008B8B: DarkCyan
******************************
0x00FF00: Lime
0x00FA9A: MediumSpringGreen
0x00CED1: DarkTurquoise
0x00BFFF: DeepSkyBlue
0x008B8B: DarkCyan
0x008080: Teal
0x008000: Green
0x006400: DarkGreen
0x0000FF: Blue
0x0000CD: MediumBlue
0x00008B: DarkBlue
0x000080: Navy
0x000000: Black
******************************
0xFFE4E1: MistyRose
0xFFEBCD: BlanchedAlmond
0xFFEFD5: PapayaWhip
0xFFF0F5: LavenderBlush
0xFFF5EE: SeaShell
0xFFF8DC: Cornsilk
0xFFFACD: LemonChiffon
0xFFFAF0: FloralWhite
0xFFFAFA: Snow
0xFFFF00: Yellow
0xFFFFE0: LightYellow
0xFFFFF0: Ivory
0xFFFFFF: White
******************************
0xDA70D6: Orchid
0xDAA520: GoldenRod
0xDB7093: PaleVioletRed
0xDC143C: Crimson
0xDCDCDC: Gainsboro
0xDDA0DD: Plum
0xDEB887: BurlyWood
0xE0FFFF: LightCyan
0xE6E6FA: Lavender
0xE9967A: DarkSalmon
 */
