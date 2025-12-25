package onjava;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 1:27
 * P.661 §20.4 泛型方法 §20.4.4 Set 实用工具
 * {java onjava.CollectionMethodDifferences}
 * <p>
 * 下面这个示例用 Sets.difference() 演示了 java.util 中的各种 Collection 和 Map 类之间的区别：
 */
public class CollectionMethodDifferences {
    static Set<String> methodSet(Class<?> type) {
        return Arrays.stream(type.getMethods())
                .map(Method::getName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    static void interfaces(Class<?> type) {
        System.out.print("Interfaces in " + type.getSimpleName() + ": ");
        System.out.println(
                Arrays.stream(type.getInterfaces())
                        .map(Class::getSimpleName)
                        .collect(Collectors.toList()));
    }

    static Set<String> object = methodSet(Object.class);

    static {
        object.add("clone");
    }

    static void difference(Class<?> superset, Class<?> subset) {
        System.out.print(superset.getSimpleName() + " extends " + subset.getSimpleName() + ", adds: ");
        Set<String> comp = Sets.difference(methodSet(superset), methodSet(subset));
        comp.removeAll(object); // Ignore 'Object' methods
        System.out.println(comp);
        interfaces(superset);
    }

    public static void main(String[] args) {
        System.out.println("Collection: " + methodSet(Collection.class));
        interfaces(Collection.class);
        difference(Set.class, Collection.class);
        difference(HashSet.class, Set.class);
        difference(LinkedHashSet.class, HashSet.class);
        difference(TreeSet.class, Set.class);
        difference(List.class, Collection.class);
        difference(ArrayList.class, List.class);
        difference(LinkedList.class, List.class);
        difference(Queue.class, Collection.class);
        difference(PriorityQueue.class, Queue.class);
        System.out.println("Map: " + methodSet(Map.class));
        difference(HashMap.class, Map.class);
        difference(LinkedHashMap.class, HashMap.class);
        difference(SortedMap.class, Map.class);
        difference(TreeMap.class, Map.class);
    }
}
/* Output:
Collection: [add, addAll, clear, contains, containsAll, equals, forEach, hashCode, isEmpty, iterator, parallelStream, remove, removeAll, removeIf, retainAll, size, spliterator, stream, toArray]
Interfaces in Collection: [Iterable]
Set extends Collection, adds: [copyOf, of]
Interfaces in Set: [Collection]
HashSet extends Set, adds: [newHashSet]
Interfaces in HashSet: [Set, Cloneable, Serializable]
LinkedHashSet extends HashSet, adds: [newLinkedHashSet, getLast, removeLast, addLast, getFirst, removeFirst, addFirst, reversed]
Interfaces in LinkedHashSet: [SequencedSet, Cloneable, Serializable]
TreeSet extends Set, adds: [headSet, descendingIterator, descendingSet, getLast, pollLast, removeLast, addLast, getFirst, removeFirst, subSet, floor, tailSet, ceiling, last, lower, comparator, pollFirst, addFirst, first, reversed, higher]
Interfaces in TreeSet: [NavigableSet, Cloneable, Serializable]
List extends Collection, adds: [getLast, removeLast, replaceAll, addLast, getFirst, get, of, removeFirst, indexOf, subList, set, sort, copyOf, lastIndexOf, listIterator, addFirst, reversed]
Interfaces in List: [SequencedCollection]
ArrayList extends List, adds: [trimToSize, ensureCapacity]
Interfaces in ArrayList: [List, RandomAccess, Cloneable, Serializable]
LinkedList extends List, adds: [offerFirst, poll, offer, element, removeLastOccurrence, peekFirst, peekLast, push, pollFirst, removeFirstOccurrence, descendingIterator, pollLast, pop, peek, offerLast]
Interfaces in LinkedList: [List, Deque, Cloneable, Serializable]
Queue extends Collection, adds: [poll, peek, offer, element]
Interfaces in Queue: [Collection]
PriorityQueue extends Queue, adds: [comparator]
Interfaces in PriorityQueue: [Serializable]
Map: [clear, compute, computeIfAbsent, computeIfPresent, containsKey, containsValue, copyOf, entry, entrySet, equals, forEach, get, getOrDefault, hashCode, isEmpty, keySet, merge, of, ofEntries, put, putAll, putIfAbsent, remove, replace, replaceAll, size, values]
HashMap extends Map, adds: [newHashMap]
Interfaces in HashMap: [Map, Cloneable, Serializable]
LinkedHashMap extends HashMap, adds: [newLinkedHashMap, sequencedKeySet, pollFirstEntry, firstEntry, putFirst, putLast, sequencedValues, pollLastEntry, lastEntry, sequencedEntrySet, reversed]
Interfaces in LinkedHashMap: [SequencedMap]
SortedMap extends Map, adds: [sequencedKeySet, pollFirstEntry, lastKey, firstEntry, putFirst, putLast, subMap, sequencedValues, pollLastEntry, comparator, lastEntry, firstKey, headMap, sequencedEntrySet, reversed, tailMap]
Interfaces in SortedMap: [SequencedMap]
TreeMap extends Map, adds: [descendingKeySet, navigableKeySet, higherEntry, higherKey, floorKey, putLast, subMap, sequencedValues, ceilingKey, pollLastEntry, firstKey, lowerKey, headMap, sequencedEntrySet, tailMap, lowerEntry, ceilingEntry, descendingMap, sequencedKeySet, pollFirstEntry, lastKey, firstEntry, putFirst, floorEntry, comparator, lastEntry, reversed]
Interfaces in TreeMap: [NavigableMap, Cloneable, Serializable]
 */
