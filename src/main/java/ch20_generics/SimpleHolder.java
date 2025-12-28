package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/28 周日
 * @time 20:36
 * P.674 §20.6 类型擦除的奥秘 §20.6.4 边界的行为
 * <p>
 * 由于类型擦除移除了方法体中的类型信息，运行时的关键便指向了边界————对象进入和离开某个方法的临界点。
 * 编译器会在编译时在临界点执行类型检查，并插入类型转换的代码。看看下面这个非泛型的示例：
 * <p>
 * set() 方法和 get() 方法分别负责存储和返回值，而在调用 get() 时则会对类型转换进行检查。
 * <p>
 * notes: Java 泛型擦除的字节码表现：指令同一性与 CHECKCAST 自动转型机制
 */
public class SimpleHolder {
    private Object obj;

    public void set(Object obj) {
        this.obj = obj;
    }

    public Object get() {
        return obj;
    }

    public static void main(String[] args) {
        SimpleHolder holder = new SimpleHolder();
        holder.set("Item");
        String s = (String) holder.get();
    }
}
/* 字节码（结果经过了编辑）
  // access flags 0x1
  public set(Ljava/lang/Object;)V
   L0
    LINENUMBER 20 L0
    ALOAD 0
    ALOAD 1
    PUTFIELD ch20_generics/SimpleHolder.obj : Ljava/lang/Object;
   L1
    LINENUMBER 21 L1
    RETURN
   L2
    LOCALVARIABLE this Lch20_generics/SimpleHolder; L0 L2 0
    LOCALVARIABLE obj Ljava/lang/Object; L0 L2 1
    MAXSTACK = 2
    MAXLOCALS = 2

  // access flags 0x1
  public get()Ljava/lang/Object;
   L0
    LINENUMBER 24 L0
    ALOAD 0
    GETFIELD ch20_generics/SimpleHolder.obj : Ljava/lang/Object;
    ARETURN
   L1
    LOCALVARIABLE this Lch20_generics/SimpleHolder; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x9
  public static main([Ljava/lang/String;)V
   L0
    LINENUMBER 28 L0
    NEW ch20_generics/SimpleHolder
    DUP
    INVOKESPECIAL ch20_generics/SimpleHolder.<init> ()V
    ASTORE 1
   L1
    LINENUMBER 29 L1
    ALOAD 1
    LDC "Item"
    INVOKEVIRTUAL ch20_generics/SimpleHolder.set (Ljava/lang/Object;)V
   L2
    LINENUMBER 30 L2
    ALOAD 1
    INVOKEVIRTUAL ch20_generics/SimpleHolder.get ()Ljava/lang/Object;
    CHECKCAST java/lang/String
    ASTORE 2
   L3
    LINENUMBER 31 L3
    RETURN
   L4
    LOCALVARIABLE args [Ljava/lang/String; L0 L4 0
    LOCALVARIABLE holder Lch20_generics/SimpleHolder; L1 L4 1
    LOCALVARIABLE s Ljava/lang/String; L3 L4 2
    MAXSTACK = 2
    MAXLOCALS = 3
 */
