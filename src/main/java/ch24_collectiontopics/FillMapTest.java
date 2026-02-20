package ch24_collectiontopics;

import onjava.Count;
import onjava.FillMap;
import onjava.Rand;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author runningpig66
 * @date 2月17日 周二
 * @time 18:20
 * P.112 §3.6 填充集合 §3.6.2 使用 Suppliers 来填充 Map
 */
public class FillMapTest {
    public static void main(String[] args) {
        Map<String, Integer> mcs = FillMap.basic(new Rand.String(4), new Count.Integer(), 7);
        System.out.println(mcs);
        HashMap<String, Integer> hashm = FillMap.create(
                new Rand.String(4), new Count.Integer(), HashMap::new, 7);
        System.out.println(hashm);
        LinkedHashMap<String, Integer> linkm = FillMap.create(
                new Rand.String(4), new Count.Integer(), LinkedHashMap::new, 7);
        System.out.println(linkm);
    }
}
/* Output:
{npcc=1, gvgm=3, ztdv=6, btpe=0, einn=4, eelo=5, uxsz=2}
{npcc=1, ztdv=6, gvgm=3, btpe=0, einn=4, eelo=5, uxsz=2}
{btpe=0, npcc=1, uxsz=2, gvgm=3, einn=4, eelo=5, ztdv=6}
 */
