package ch14_streams.practice;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * @author runningpig66
 * @date 2025/10/24 周五
 * @time 22:43
 * Spliterator 等差序列练习
 */
public final class RangeSpliterator implements Spliterator.OfInt {
    /**
     * 有限版：等差序列 [start, start+step, ... , endExclusive) 步长 > 0
     */
    // 区间是 [start, end) 左闭右开
    private int current;
    private final int end;
    private final int step;

    public RangeSpliterator(int start, int end, int step) {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be > 0");
        }
        this.current = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // 终止逻辑永远以 tryAdvance 为准，这是唯一可靠的结束信号
        if (current >= end) {
            return false;
        }
        int v = current;
        current += step;
        action.accept(v);
        return true;
    }

    /* 还能成功产出的元素个数（包含 current，不包含 end）*/
    private long remaining() {
        // (long) end 防溢出: 想要避免中间溢出，必须在“运算之前”让至少一个操作数变宽
        long diff = (long) end - current;
        if (diff <= 0) {
            return 0;
        }
        return (diff + step - 1) / step;
    }

    /* 我们真的能对半切，所以并行速度能飞起来。*/
    @Override
    public Spliterator.OfInt trySplit() {
        long remaining = remaining();
        if (remaining < 2) {
            return null;
        }
        int mid = (int) (current + (remaining / 2) * step); // 至少 current + step
        Spliterator.OfInt prefix = new RangeSpliterator(current, mid, step);
        this.current = mid; // 留后半给自己
        return prefix;
    }

    /* estimateSize 和 SIZED/SUBSIZED 让框架能精准预算与切分。*/
    @Override
    public long estimateSize() {
        return remaining();
    }

    /*
    ORDERED 元素有固定遇到顺序
    SIZED 精确大小可知
    SUBSIZED 所有子切片也 SIZED
    NONNULL 永不返回 null
    IMMUTABLE 源在遍历中不可结构性修改
    DISTINCT 元素互不重复
    SORTED 已排序
    */
    @Override
    public int characteristics() {
        // DISTINCT: step!=0 已由构造器保证
        // SORTED: step>0 为自然升序
        return ORDERED | SIZED | SUBSIZED | NONNULL | IMMUTABLE | DISTINCT | SORTED;
    }

    /* 按契约：自然顺序的 SORTED 必须返回 null */
    @Override
    public Comparator<? super Integer> getComparator() {
        return null;
    }

    /* 便捷工厂：转换为并行/顺序流 */
    public static IntStream stream(int start, int end, int step, boolean parallel) {
        return StreamSupport.intStream(new RangeSpliterator(start, end, step), parallel);
    }

    /**
     * 无限版 Int 专用：自制版 iterate(seed, f)
     */
    public static IntStream iterateInt(int seed, IntUnaryOperator f) {
        Spliterator.OfInt sp = new Spliterators.AbstractIntSpliterator(
                Long.MAX_VALUE, ORDERED | IMMUTABLE) { // 不要声明 NONNULL 除非你确信
            boolean started = false;
            int prev;

            @Override
            public boolean tryAdvance(IntConsumer action) {
                int t;
                if (!started) {
                    t = seed;
                    started = true;
                } else {
                    t = f.applyAsInt(prev);
                }
                action.accept(prev = t);
                return true; // 无限：不自己结束
            }
        };
        return StreamSupport.intStream(sp, false);
    }

    public static void main(String[] args) {
        // 有限版：0, 100, ... , 900
        int sum = RangeSpliterator.stream(0, 1_000, 100, true).sum();
        System.out.println(sum);

        //无限版：与上面相同的 10 个元素求和
        int start = 0, end = 1_000, step = 100;
        int limitedSize = (end - start + step - 1) / step;
        int sum1 = RangeSpliterator.iterateInt(start, n -> n + step)
                .limit(limitedSize)
                .sum();
        System.out.println(sum1);
    }
}
