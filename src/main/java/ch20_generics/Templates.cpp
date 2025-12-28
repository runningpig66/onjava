/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 17:52
 * P.667 §20.6 类型擦除的奥秘 §20.6.1 C++ 的实现方法
 *
 * 下面是使用了模板的 C++ 示例。由于 Java 的设计是受 C++ 所启发，因此两者参数化类型的语法十分相似：
 * Manipulator 类中存放了一个 T 类型的对象 obj，manipulate() 方法则调用了 obj 上的 f() 方法。
 * 它是如何知道类型参数 T 中存在 f() 方法的呢？C++ 编译器会在你实例化模板的时候进行检查，
 * 这样在实例化 Manipulator<Hash> 时，编译器便会看到 Hash 中存在方法 f()。
 * 如果情况并非如此，就会出现编译时错误，从而保证了类型的安全。
 */
#include <iostream>
using namespace std;

template<class T> class Manipulator {
  T obj;
public:
  Manipulator(T x) { obj = x; }
  void manipulate() { obj.f(); }
};

class HasF {
public:
  void f() { cout << "HasF::f()" << endl; }
};

int main() {
  HasF hf;
  Manipulator<HasF> manipulator(hf);
  manipulator.manipulate();
}
/* Output:
HasF::f()
*/
