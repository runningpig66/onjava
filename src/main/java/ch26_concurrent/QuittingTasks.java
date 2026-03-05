package ch26_concurrent;

import onjava.Nap;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月3日 周二
 * @time 4:44
 * P.239 §5.9 终止长时间运行的任务
 */
public class QuittingTasks {
    public static final int COUNT = 150;

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        List<QuittableTask> tasks = IntStream.range(1, COUNT)
                .mapToObj(QuittableTask::new)
                .peek(es::execute)
                .toList();
        new Nap(1);
        tasks.forEach(QuittableTask::quit);
        es.shutdown();
    }
}
/* Output:
11 42 80 3 88 27 122 110 114 86 17 126 91 30 6 142 34 125 71 102 32 112 149 96 127 41 134 123 16 8 75 99 119 52 95 37 50 145 47 130 107 58 116 72 26 83 40 38 104 63 141 60 108 85 14 143 146 139 147 140 43 24 49 36 124 13 23 20 33 9 39 15 55 131 148 118 132 137 128 67 74 21 92 115 7 117 105 45 68 100 133 46 101 84 48 76 56 18 109 31 2 10 1 77 78 93 144 66 62 94 69 70 61 25 64 111 57 4 87 65 51 28 35 53 136 103 54 59 135 19 98 121 97 120 44 79 22 81 12 82 90 73 89 106 5 113 129 29 138
 */
