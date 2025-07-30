package ch12_collections;

import onjava.CollectionMethodDifferences;

/**
 * @author runningpig66
 * @date 2025-07-30 周三
 * @time 下午 16:53
 */
public class CollectionDifferences {
    public static void main(String[] args) {
        CollectionMethodDifferences.main(args);
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
