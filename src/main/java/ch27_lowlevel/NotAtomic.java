package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月11日 周三
 * @time 5:52
 * P.298 §6.5 原子性
 * javap -v -p -c -cp E:/IdeaProjects/onjava/build/classes/java/main ch27_lowlevel.NotAtomic
 * -v (verbose)：输出极其详细的附加信息，包括常量池（Constant Pool）、局部变量表、栈大小等。
 * -p (private)：显示所有类和成员，包括私有（private）的方法和字段。
 * -c：输出方法体内的反汇编字节码。
 * {ExcludeFromGradle}
 * {VisuallyInspectOutput}
 * <p>
 * 在 Java 中，上述操作（i++、i += 3等）一定不是原子操作，如以下方法所生成的 JVM 指令所示：
 * 每个指令都会生成一个 “get”（读取）和一个 “put”（写入），中间夹杂着一些其他指令。
 * 所以在读取和写入操作中间，另一个任务可能会修改该字段，因此该操作并不是原子的。
 */
public class NotAtomic {
    int i;

    void f1() {
        i++;
    }

    void f2() {
        i += 3;
    }
}
/* Output:
Compiled from "NotAtomic.java"
public class ch27_lowlevel.NotAtomic {
  int i;

  public ch27_lowlevel.NotAtomic();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  void f1();
    Code:
       0: aload_0
       1: dup
       2: getfield      #7                  // Field i:I
       5: iconst_1
       6: iadd
       7: putfield      #7                  // Field i:I
      10: return

  void f2();
    Code:
       0: aload_0
       1: dup
       2: getfield      #7                  // Field i:I
       5: iconst_3
       6: iadd
       7: putfield      #7                  // Field i:I
      10: return
}
 */
