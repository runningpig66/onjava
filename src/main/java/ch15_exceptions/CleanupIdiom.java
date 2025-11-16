package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 22:45
 * 代码清单 P.455 构造器
 * Disposable objects must be followed by a try-finally
 * <p>
 * 即使构造器不会抛出任何异常，也应该使用这种通用的清理惯用法。
 * 其基本规则是，在创建了一个需要滑理的对象之后，直接跟一个 try-finally 块。
 */
class NeedsCleanup { // Construction can't fail
    private static long counter = 1;
    private final long id = counter++;

    public void dispose() {
        System.out.println("NeedsCleanup " + id + " disposed");
    }
}

class ConstructionException extends Exception {
}

class NeedsCleanup2 extends NeedsCleanup {
    // Construction can fail:
    NeedsCleanup2() throws ConstructionException {
    }
}

public class CleanupIdiom {
    public static void main(String[] args) {
        // [1]: 这个相当直接：在需要清理的对象后面紧跟一个 try-finally 块。
        // 如果对象构造不会失败，也就不需要 catch 子句了。
        NeedsCleanup nc1 = new NeedsCleanup();
        try {
            // ...
        } finally {
            nc1.dispose();
        }

        // [2]: 这里可以看到，对于构造不会失败的对象，多个对象的构造和消理可以分别组织到一起。
        // If construction cannot fail, you can group objects:
        NeedsCleanup nc2 = new NeedsCleanup();
        NeedsCleanup nc3 = new NeedsCleanup();
        try {
            // ...
        } finally {
            nc3.dispose(); // Reverse order of construction
            nc2.dispose();
        }

        // [3]: 这里演示了如何处理构造可能会失败并且需要清理的对象。
        // 要正确处理这种情况，事情会变得很棘手，因为必须将每个构造用它自已的 try-catch 包围起来，
        // 而且每个对象的构造操作后面必须有一个 try-finally 语句块来保证清理。
        // 注意，如果 dispose() 会抛出异常，我们可能还需要额外的 try 块。
        // 基本上，我们应该仔细考虑所有的可能性，并确保正确处理每一种情况。
        // If construction can fail you must guard each one:
        try {
            NeedsCleanup2 nc4 = new NeedsCleanup2();
            try {
                NeedsCleanup2 nc5 = new NeedsCleanup2();
                try {
                    // ...
                } finally {
                    nc5.dispose();
                }
            } catch (ConstructionException e) { // nc5 const.
                System.out.println(e);
            } finally {
                nc4.dispose();
            }
        } catch (ConstructionException e) { // nc4 const.
            System.out.println(e);
        }
    }
}
/* Output:
NeedsCleanup 1 disposed
NeedsCleanup 3 disposed
NeedsCleanup 2 disposed
NeedsCleanup 5 disposed
NeedsCleanup 4 disposed
 */
