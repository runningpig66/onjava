package ch20_generics.notes;

import ch20_generics.Holder;

/**
 * @author runningpig66
 * @date 2026/1/7 周三
 * @time 1:08
 * <p>
 * TODO: 这就是为什么在没有传入 Class Token（类型令牌） 进行显式运行时检查的情况下，单纯利用捕获转换和强转来写入异构数据，本质上是在利用 Java 类型系统的漏洞进行“裸奔”。
 * <p>
 * // 可能的解决方案：传入类型令牌
 * static <T> void safeWrite(Holder<T> holder, Class<T> type, Object value) {
 *     T t = type.cast(value);  // 在运行时检查类型
 *     holder.set(t);  // 现在可以安全写入
 * }
 */
@SuppressWarnings("unchecked")
public class StrictCapture2 {
    static <T> void f1(Holder<T> holder, T val) {
        holder.set(val);
        System.out.println("f1写入成功: " + val);
    }

    // 捕获转换
    private static <Q> void captureHelper(Holder<Q> holder, Object val) {
        f1(holder, (Q) val);
    }
    // Q = CAP#1

    public static void f2(Holder<?> holder, Object arg) {
        captureHelper(holder, arg);
    }

    public static void main(String[] args) {
        Holder<Integer> intHolder = new Holder<>(1);
        String badData = "IamString";
        f2(intHolder, badData);
        System.out.println("写入居然成功了！Holder 里的值是：" + intHolder.get());
//        Integer i = intHolder.get();
    }
}
/* Output:
f1写入成功: IamString
写入居然成功了！Holder 里的值是：IamString
 */
