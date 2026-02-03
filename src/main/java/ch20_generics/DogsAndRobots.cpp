// generics/DogsAndRobots.cpp
// (c)2021 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.
// P.733 §20.15 潜在类型机制 §20.15.2 C++ 中的潜在类型机制

#include <iostream>
using namespace std;

class Dog {
public:
  void speak() { cout << "Arf!" << endl; }
  void sit() { cout << "Sitting" << endl; }
  void reproduce() {}
};

class Robot {
public:
  void speak() { cout << "Click!" << endl; }
  void sit() { cout << "Clank!" << endl; }
  void oilChange() {}
};
/*
 * 【C++：静态潜在类型机制的安全性证明】
 * 本例通过 C++ 模板展示了即便在“静态编译期”，也无需像 Java 那样依赖接口继承来实现类型约束。
 * C++ 编译器在模板实例化时会自动检查类型是否具备所需方法（speak/sit），若不匹配则编译失败。
 * 这一机制有力地反驳了 Java 的设计哲学——它证明了“静态类型安全”并不必然导致“死板的接口束缚”。
 * 相较于 Java 基于擦除（Erasure）和边界的泛型实现，C++ 证明了静态语言完全可以拥有像动态语言一样的结构化灵活性。
 */
template<class T> void perform(T anything) {
  anything.speak();
  anything.sit();
}

int main() {
  Dog d;
  Robot r;
  perform(d);
  perform(r);
}
/* Output:
Arf!
Sitting
Click!
Clank!
*/
