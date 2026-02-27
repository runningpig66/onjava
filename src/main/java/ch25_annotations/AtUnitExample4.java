package ch25_annotations;

import onjava.ConvertTo;
import onjava.atunit.Test;
import onjava.atunit.TestObjectCreate;
import onjava.atunit.TestProperty;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月26日 周四
 * @time 5:34
 * P.188 §4.4 基于注解的单元测试
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/AtUnitExample4.class}
 * {VisuallyInspectOutput}
 * <p>
 * 有时你需要额外的字段来支持单元测试。@TestProperty 注解可以标识仅用于单元测试的字段（这样在交付给客户前便可以随意移除这些字段）。
 * 下面是一个示例，它读取一个被 String.split() 方法切割后的字符串的值，该值会作为输入来生成测试对象：
 * @TestProperty 同样可以用来标识在测试期间可用，但自身并不是测试的方法。
 * <p>
 * 这个程序依赖于测试执行的顺序，通常来说这并不是一种好的实现方式。
 * 测试隔离性：本示例是作者刻意设计的一个反面教学案例，它强依赖于测试框架执行方法的顺序。
 * 根本原因：测试框架调用 @Test 方法的顺序具有随机性，而本例中提供数据的机制是固定的单向流。
 * <p>
 * 反面模式演示：本例中，多个独立的测试方法共享了同一个具有状态且不可逆的类级别数据源（即 static Iterator）。
 * 这种设计破坏了测试间的隔离性，导致后执行的测试用例强耦合于前置测试造成的副作用（数据消耗），从而引发随机性的测试失败。
 * <p>
 * 最佳实践：优秀的单元测试必须保证绝对的运行环境隔离。每一个 @Test 方法都应被视作一个独立的沙箱。
 * 无论测试的执行顺序如何，或被单独运行多少次，其结果都必须具备确定性。在每次测试启动前，所有的依赖数据和上下文都应当被重置为初始状态。
 */
public class AtUnitExample4 {
    static String theory = "All brontosauruses are thin at one end, " +
            "much MUCH thicker in the middle, and then thin again at the far end.";
    private String word;
    private Random rand = new Random(); // Time-based seed

    public AtUnitExample4(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public String scrambleWord() {
        List<Character> chars = Arrays.asList(ConvertTo.boxed(word.toCharArray()));
        Collections.shuffle(chars, rand);
        StringBuilder result = new StringBuilder();
        for (char ch : chars) {
            result.append(ch);
        }
        return result.toString();
    }

    @TestProperty
    static List<String> input = Arrays.asList(theory.split(" "));
    @TestProperty
    static Iterator<String> words = input.iterator();

    @TestObjectCreate
    static AtUnitExample4 create() {
        if (words.hasNext()) {
            return new AtUnitExample4(words.next());
        } else {
            return null;
        }
    }

    @Test
    boolean words() {
        System.out.println("'" + getWord() + "'");
        return getWord().equals("are");
    }

    @Test
    boolean scramble1() {
        // Use specific seed to get verifiable results:
        rand = new Random(47);
        System.out.println("'" + getWord() + "'");
        String scrambled = scrambleWord();
        System.out.println(scrambled);
        return scrambled.equals("lAl");
    }

    @Test
    boolean scramble2() {
        rand = new Random(74);
        System.out.println("'" + getWord() + "'");
        String scrambled = scrambleWord();
        System.out.println(scrambled);
        return scrambled.equals("tsaeborornussu");
    }
}
/* Output 1:
ch25_annotations.AtUnitExample4
  . words 'All'
(failed)
  . scramble1 'brontosauruses'
ntsaueorosurbs
(failed)
  . scramble2 'are'
are
(failed)
(3 tests)

>>> 3 FAILURES <<<
  ch25_annotations.AtUnitExample4: words
  ch25_annotations.AtUnitExample4: scramble1
  ch25_annotations.AtUnitExample4: scramble2
 */
/* Output 2: (Etc.)
ch25_annotations.AtUnitExample4
  . scramble1 'All'
lAl

  . scramble2 'brontosauruses'
tsaeborornussu

  . words 'are'

OK (3 tests)
 */
