package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 16:03
 * P.647 §20.2 简单泛型 §20.2.2 栈类
 * A stack implemented with an internal linked structure
 * <p>
 * 以上示例利用了末端哨兵（end sentinel）来检查栈何时为空。末端哨兵在 LinkedStack 完成构造后被创建，
 * 每次调用 push() 都会创建一个新的 Node<T>，并被链接到前一个 Node<T>。调用 pop() 时总会返回 top.item，
 * 然后丢弃当前的 Node<T>，并移动到下一个 Node<T>————直到命中末端哨兵，这时便会停止移动。
 * 这种情况下，如果调用者继续调用 pop()，便会一直返回 null，这表明栈已为空。
 */
public class LinkedStack<T> {
    // 内部类 Node 同样也是泛型，并且有着自己的类型参数。
    private static class Node<U> {
        U item;
        Node<U> next;

        Node() {
            item = null;
            next = null;
        }

        Node(U item, Node<U> next) {
            this.item = item;
            this.next = next;
        }

        boolean end() {
            return item == null && next == null;
        }
    }

    private Node<T> top = new Node<>(); // 末端哨兵 End sentinel

    public void push(T item) {
        top = new Node<>(item, top);
    }

    public T pop() {
        T result = top.item;
        if (!top.end()) {
            top = top.next;
        }
        return result;
    }

    public static void main(String[] args) {
        LinkedStack<String> lss = new LinkedStack<>();
        for (String s : "Phasers on stun!".split(" ")) {
            lss.push(s);
        }
        String s;
        while ((s = lss.pop()) != null) {
            System.out.println(s);
        }
    }
}
/* Output:
stun!
on
Phasers
 */
