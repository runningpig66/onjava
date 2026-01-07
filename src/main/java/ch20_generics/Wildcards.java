package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/3 周六
 * @time 20:36
 * P.698 §20.9 通配符 §20.9.3 无界通配符
 * Exploring the meaning of wildcards
 * <p>
 * 编译器什么时候才会关心原始类型和带有无界通配符的类型之间的区别呢？下面这个示例用到了之前定义过的 Holder<T> 类。
 * 该示例内部包含若干不同形式的以 Holder 为参数的方法————以原始类型方式、带有具体的类型参数、以及带有无界通配符的参数：
 * <p>
 * notes: 13-泛型方法编译机制与类型推断生命周期.md
 */
public class Wildcards {
    // Raw argument:
    // 类型擦除：无论原引用持有何种泛型约束，在此方法内均被视为原生类型 (Raw Type)，泛型参数信息被完全忽略。
    // 检查缺失：编译器暂停对原生类型的类型安全检查（Unchecked），允许写入任意对象（引发堆污染风险），仅以 Warning 警告。
    // 信息丢失：由于缺乏泛型上下文，所有的读取操作返回值均被视为 Object，丢失了原有的类型信息。
    @SuppressWarnings("unchecked")
    static void rawArgs(Holder holder, Object arg) {
        holder.set(arg);
        // Wildcards.java:31: warning: [unchecked] unchecked call to set(T) as a member of the raw type Holder
        // holder.set(arg);
        //           ^
        // where T is a type-variable:
        // T extends Object declared in class Holder
        // 1 warning

        // Can't do this; don't have any 'T':
        // T t = holder.get();

        // OK, but type information is lost:
        Object obj = holder.get();
    }

    // Like rawArgs(), but errors instead of warnings:
    // CAP#1：表示未知的特定类型（Capture Variable #1）。Holder<?> 并非“持有任意类型”，而是“持有某种特定但未知的类型”。
    // 写入封锁：编译器无法确定 ? 捕获的具体类型 (CAP#1)，为了类型安全，禁止一切写入操作（null 除外）。
    // Holder<?> 是所有泛型实例的通用接收者，通过牺牲“写入权”换取了最大的“兼容性”。
    static void unboundedArg(Holder<?> holder, Object arg) {
        //- holder.set(arg);
        // Wildcards.java:51: error: incompatible types: Object cannot be converted to CAP#1
        // holder.set(arg);
        //            ^
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Object from capture of ?
        // Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output
        // 1 error

        // Can't do this; don't have any 'T':
        // T t = holder.get();

        // OK, but type information is lost:
        // 由于具体类型丢失，读取操作类型退化为上界 Object，统一视为 Object 处理。
        Object obj = holder.get();
    }

    // 要求参数必须严格匹配 Holder<T>，并返回 T 类型。这迫使编译器进行“类型推断”，试图确定 T 具体是什么。
    // exact1(Holder<T>) 是“单向推断”：你给什么，T 就是什么。给 Holder<?>，T 就是 CAP#1。
    static <T> T exact1(Holder<T> holder) {
        return holder.get();
    }

    // 泛型方法定义：要求 holder 的泛型类型 T，必须和第二个参数 arg 的类型 T 严格一致。
    // 在 exact1 中，编译器只有一个参数 Holder<T> 作为线索，遇到原生类型就只能擦除为 Object。
    // 但在 exact2 中，有了第二个参数 T arg，编译器就有了“强约束锚点”。
    // exact2(Holder<T>, T) 是“双向对齐”：编译器必须找到一个 T，同时满足两个条件：1. holder 是 Holder<T>；2. arg 是 T。
    static <T> T exact2(Holder<T> holder, T arg) {
        holder.set(arg);
        return holder.get();
    }

    // 参数 Holder<? extends T> 被视为 PECS 原则 (Producer Extends) 中的生产者：
    // 禁止写入：因为具体子类型未知 (CAP# extends T)，无法安全地写入 T (除非 null)，编译器报错。
    // 允许读取：无论具体子类型是什么，所有子类型都是 T 的子类，因此 get() 返回值可以安全地向上转型为 T。
    static <T> T wildSubtype(Holder<? extends T> holder, T arg) {
        //- holder.set(arg);
        // Wildcards.java:87: error: incompatible types: T cannot be converted to CAP#1
        // holder.set(arg);
        //            ^
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSubtype(Holder<? extends T>,T)
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends T from capture of ? extends T
        // Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output
        // 1 error

        return holder.get();
    }

    // 参数 Holder<? super T> 被视为 PECS 原则 (Consumer Super) 中的消费者：
    // 允许写入：因为具体类型是 T 或 T 的超类 (CAP# super T)，T 类型（及其子类）一定是该未知超类的子类型，因此 holder.set(arg) 是类型安全的。
    // 限制读取：因为具体超类未知（上界开放，可能是 Object），编译器无法保证取出的数据一定是 T。get() 的返回值只能被安全地视为 Object（类型信息丢失）。
    static <T> void wildSupertype(Holder<? super T> holder, T arg) {
        holder.set(arg);
        //- T t = holder.get();
        // Wildcards.java:106: error: incompatible types: CAP#1 cannot be converted to T
        // T t = holder.get();
        //                 ^
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSupertype(Holder<? super T>,T)
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Object super: T from capture of ? super T
        // 1 error

        // OK, but type information is lost:
        Object obj = holder.get();
    }

    public static void main(String[] args) {
        Holder raw = new Holder<>();
        // Or:
        raw = new Holder();
        Holder<Long> qualified = new Holder<>();
        Holder<?> unbounded = new Holder<>();
        Holder<? extends Long> bounded = new Holder<>();
        long lng = 1L;

        rawArgs(raw, lng);
        rawArgs(qualified, lng); // qualified Holder<Long> 携带的具体类型约束 <Long> 在传递给原生参数时失效，失去了编译期类型保护。
        rawArgs(unbounded, lng); // unbounded 原本由 Holder<?> 施加的“写入封锁”限制，在赋值给原生参数后失效，导致只读对象在方法内变为可写。
        rawArgs(bounded, lng);

        unboundedArg(raw, lng);
        unboundedArg(qualified, lng);
        unboundedArg(unbounded, lng);
        unboundedArg(bounded, lng);

        // 未检查转换：传入原生类型 raw，编译器无法推断 T 的具体类型。为了兼容性，编译器将 T 擦除为 Object，并发出 Unchecked 警告，提示类型安全无法保证。
        @SuppressWarnings({"unchecked"})
        Object r1 = exact1(raw);
        // Wildcards.java:141: warning: [unchecked] unchecked method invocation: method exact1 in class Wildcards is applied to given types
        //         Object r1 = exact1(raw);
        //                   ^
        // required: Holder<T>
        // found:    Holder
        // where T is a type-variable:
        // T extends Object declared in method <T>exact1(Holder<T>)
        // Wildcards.java:82: warning: [unchecked] unchecked conversion
        // Object r1 = exact1(raw);
        //                    ^
        // required: Holder<T>
        // found:    Holder
        // where T is a type-variable:
        // T extends Object declared in method <T>exact1(Holder<T>)
        // 2 warnings

        // 精确匹配：传入 Holder<Long>，编译器轻松推断出 T 为 Long。
        Long r2 = exact1(qualified);
        // 捕获转换：传入 Holder<?>，编译器对通配符 ? 进行捕获，将其锁定为临时变量 CAP#1。
        // 推断 T = CAP#1。由于 CAP#1 具体类型未知，返回值只能安全的赋值给 Object。
        Object r3 = exact1(unbounded); // Must return Object
        // 带界捕获：传入 Holder<? extends Long>，编译器捕获通配符，推断 T 为“某个继承自 Long 的未知类型”。
        // 虽然 T 具体未知，但它必定是 Long 的子类，因此返回值可以安全地赋值给 Long。
        Long r4 = exact1(bounded);

        // 锚点推断：与 exact1 不同，这里 T 被推断为 Long。
        // 原因：参数2 (lng) 明确了 T=Long。编译器以此为锚点，强制将参数1 (raw) 视为 Holder<Long> 处理。
        // 代价：因为原生类型无法保证真正持有 Long，所以触发 Unchecked 警告。
        @SuppressWarnings({"unchecked"})
        Long r5 = exact2(raw, lng);
        // Wildcards.java:171: warning: [unchecked] unchecked method invocation: method exact2 in class Wildcards is applied to given types
        //         Long r5 = exact2(raw, lng);
        //                 ^
        // required: Holder<T>,T
        // found:    Holder,long
        // where T is a type-variable:
        // T extends Object declared in method <T>exact2(Holder<T>,T)
        // Wildcards.java:109: warning: [unchecked] unchecked conversion
        // Long r5 = exact2(raw, lng);
        //                  ^
        // required: Holder<T>
        // found:    Holder
        // where T is a type-variable:
        // T extends Object declared in method <T>exact2(Holder<T>,T)
        // 2 warnings

        // 完美匹配：Holder<Long> 和 Long 类型一致，T 被推断为 Long。
        Long r6 = exact2(qualified, lng);

        // 不变性约束：参数1 (unbounded) 捕获为 Holder<CAP#1> (其中 CAP#1 extends Object)。方法签名为 Holder<T> 要求严格匹配（泛型不变性）。实参 Holder<CAP#1> 强制要求 T 必须严格等于 CAP#1 (T = CAP#1)。
        // 下界约束：参数2 (lng) 传入 Long。要求实参 Long 必须能赋值给形参 T，T 必须是 Long 的父类，确立了 T 的下界 (Long <= T)。
        // 合并约束：将约束一代入约束二，编译器必须验证：Long <= CAP#1（即 Long 必须是 CAP#1 的子类型）。
        // 但 CAP#1 是未知的 Object 子类（可能是 String, Integer 等）。编译器无法证明 Long 是该未知类型的子类，推断失败。
        //- Long r7 = exact2(unbounded, lng);
        // Wildcards.java:193: error: method exact2 in class Wildcards cannot be applied to given types;
        // Long r7 = exact2(unbounded, lng);
        //           ^
        // required: Holder<T>,T
        // found:    Holder<CAP#1>,long
        // reason: inference variable T has incompatible bounds
        // equality constraints: CAP#1
        // lower bounds: Long
        // where T is a type-variable:
        // T extends Object declared in method <T>exact2(Holder<T>,T)
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Object from capture of ?
        // 1 error

        // 不变性约束：参数1 (bounded) 捕获为 Holder<CAP#1> (其中 CAP#1 extends Long)。方法签名为 Holder<T> 要求严格匹配（泛型不变性）。实参 Holder<CAP#1> 强制要求 T 必须严格等于 CAP#1 (T = CAP#1)。
        // 下界约束：参数2 (lng) 传入 Long。要求实参 Long 必须能赋值给形参 T，T 必须是 Long 的父类，确立了 T 的下界 (Long <= T)。
        // 合并约束：将约束一代入约束二，编译器必须验证：Long <= CAP#1（即 Long 必须是 CAP#1 的子类型）。
        // 但 CAP#1 被定义为 Long 的某个子类 (CAP#1 <= Long)。这就是逻辑倒置：推断要求 "Long 是 CAP#1 的子类"，定义却是 "CAP#1 是 Long 的子类"。
        // 编译器无法证明 Long 是 CAP#1 的子类型，除非两者相等，否则无法成立，编译器报错。
        // Tip: 因为编译器无法证明 CAP#1 就是 Long（万一 CAP#1 是 Long 的一个子类 SubLong 呢？那你传个 Long 给它就不行了）。
        //- Long r8 = exact2(bounded, lng);
        // Wildcards.java:212: error: method exact2 in class Wildcards cannot be applied to given types;
        // Long r8 = exact2(bounded, lng);
        //           ^
        // required: Holder<T>,T
        // found:    Holder<CAP#1>,long
        // reason: inference variable T has incompatible bounds
        // equality constraints: CAP#1
        // lower bounds: Long
        // where T is a type-variable:
        // T extends Object declared in method <T>exact2(Holder<T>,T)
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Long from capture of ? extends Long
        // 1 error

        // 原生适配：T 被推断为 Long。原生 Holder 被强制视为 Holder<? extends Long>，由于原生类型缺乏约束，编译器 Unchecked 警告。
        @SuppressWarnings("unchecked")
        Long r9 = wildSubtype(raw, lng);
        // Wildcards.java:229: warning: [unchecked] unchecked method invocation: method wildSubtype in class Wildcards is applied to given types
        //         Long r9 = wildSubtype(raw, lng);
        //                      ^
        // required: Holder<? extends T>,T
        // found:    Holder,long
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSubtype(Holder<? extends T>,T)
        // Wildcards.java:176: warning: [unchecked] unchecked conversion
        // Long r9 = wildSubtype(raw, lng);
        //                       ^
        // required: Holder<? extends T>
        // found:    Holder
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSubtype(Holder<? extends T>,T)
        // 2 warnings

        // 精确匹配：T 被推断为 Long。Holder<Long> 是 Holder<? extends Long> 的有效子类型，满足约束。
        Long r10 = wildSubtype(qualified, lng);

        // 推断降级：参数1 (Holder<?>) 暗示 T 可能是 Object；参数2 (lng) 暗示 T 是 Long。
        // 判决：编译器计算两者的“最小上界”，推断 T = Object。因此返回值只能是 Object。
        // OK, but can only return Object:
        Object r11 = wildSubtype(unbounded, lng);

        // Holder<? extends Long> 完美契合方法签名中的 Holder<? extends T>，T 被推断为 Long。
        Long r12 = wildSubtype(bounded, lng);

        // 原生适配：参数1 (raw) 为原生类型，根据 JLS 规则，它不参与泛型约束（失语）。
        // 推断仅依赖参数2 (lng)，即 T >= Long，故 T 被推断为 Long。
        // 代价：原生 Holder 赋值给 Holder<? super Long> 触发 Unchecked 警告（堆污染风险）。
        // wildSupertype(raw, lng);
        // Wildcards.java:264: warning: [unchecked] unchecked method invocation: method wildSupertype in class Wildcards is applied to given types
        //         wildSupertype(raw, lng);
        //              ^
        // required: Holder<? super T>,T
        // found:    Holder,long
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSupertype(Holder<? super T>,T)
        // Wildcards.java:215: warning: [unchecked] unchecked conversion
        // wildSupertype(raw, lng);
        //               ^
        // required: Holder<? super T>
        // found:    Holder
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSupertype(Holder<? super T>,T)
        // 2 warnings

        // 精确匹配：参数2 (lng) 确立 T 的下界 (T >= Long)；参数1 (qualified) 确立 T 的上界 (T <= Long)。
        // 判决：T 既要是 Long 的父类，又要包含于 Long，在双向约束的夹击下，T 被唯一锁定为 Long。
        wildSupertype(qualified, lng);

        // 推断失败：条件 A: 参数2 (lng) 确立 T 的下界为 Long (T >= Long)。
        // 条件 B: 参数1 (unbounded) 捕获为临时变量 CAP#1 (extends Object) 。代入 "? super T"，确立 T 的上界为 CAP#1 (T <= CAP#1)
        // 编译器必须找到一个类型 T，它既是 Long 的父类（条件 A），又是 CAP#1 的子类（条件 B）。(Long <= T <= CAP#1)
        // 即 CAP#1 也应该是 Long 的父类，但根据捕获定义，CAP#1 实际上是 Object 的未知子类（可能是 String，可能是 Integer，未必就是 Long (下界) 的父类）
        // 编译器无法证明 Long (下界) 是未知类型 CAP#1 (上界) 的子类（除非 CAP#1 恰好是 Long 的父类，但编译器无法保证），推断链断裂。
        //- wildSupertype(unbounded, lng);
        // Wildcards.java:286: error: method wildSupertype in class Wildcards cannot be applied to given types;
        // wildSupertype(unbounded, lng);
        // ^
        // required: Holder<? super T>,T
        // found:    Holder<CAP#1>,long
        // reason: inference variable T has incompatible bounds
        // upper bounds: CAP#1,Object
        // lower bounds: Long
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSupertype(Holder<? super T>,T)
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Object from capture of ?
        // 1 error

        // 推断失败：条件 A: 参数2 (lng) 确立 T 的下界为 Long (T >= Long)。
        // 条件 B: 参数1 (bounded) 捕获为临时变量 CAP#1 (extends Long)。代入 "? super T"，确立 T 的上界为 CAP#1 (T <= CAP#1)
        // 编译器必须找到一个类型 T，它既是 Long 的父类（条件 A），又是 CAP#1 的子类（条件 B）。(Long <= T <= CAP#1)
        // 即 CAP#1 也应该是 Long 的父类，但根据捕获定义，CAP#1 实际上是 Long 的未知子类 (CAP#1 extends Long)。
        // 编译器无法证明 Long (下界) 是未知类型 CAP#1 (上界) 的子类（除非 CAP#1 恰好是 Long，但编译器无法保证），推断链断裂。
        //- wildSupertype(bounded, lng);
        // Wildcards.java:306: error: method wildSupertype in class Wildcards cannot be applied to given types;
        // wildSupertype(bounded, lng);
        // ^
        // required: Holder<? super T>,T
        // found:    Holder<CAP#1>,long
        // reason: inference variable T has incompatible bounds
        // upper bounds: CAP#1,Object
        // lower bounds: Long
        // where T is a type-variable:
        // T extends Object declared in method <T>wildSupertype(Holder<? super T>,T)
        // where CAP#1 is a fresh type-variable:
        // CAP#1 extends Long from capture of ? extends Long
        // 1 error
    }
}
