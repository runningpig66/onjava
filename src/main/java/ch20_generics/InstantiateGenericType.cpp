/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 21:42
 * P.677 §20.7 对类型擦除的补偿 §20.7.1 创建类型实例
 *
 * 试图在 Erased.java 中创建 new T() 是不会成功的，部分原因是类型擦除，另一部分原因是编译器无法验证 T 中是否存在无参构造器。
 * 但是在 C++ 中，这种操作相当自然，并且简单安全（因为会在编译时检查）。
 */
template<class T> class Foo {
  T x; // Create a field of type T
  T* y; // Pointer to T
public:
  // Initialize the pointer:
  Foo() { y = new T(); }
};

class Bar {};

int main() {
  Foo<Bar> fb;
  Foo<int> fi; // ... and it works with primitives
}
