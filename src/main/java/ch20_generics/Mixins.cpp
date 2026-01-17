/**
 * @author runningpig66
 * @date 2026/1/15 周四
 * @time 1:00
 * P.725 §20.14 混型 §20.14.1 C++ 中的混型
 *
 * 在 C++ 中，使用多重继承的最重要原因之一就是混型。实现混型的一种更优雅的方式是使用参数化类型，
 * 因为混型就是继承自其类型参数的类。在 C++ 中，你可以轻松地混型，因为 C++ 能记住其模板参数的类型。
 * 下面是一个 C++ 的示例，带有两个混型类型。其中一个给每个对象实例都混入了自带时间戳这样一种属性，另一个则给每个对象实例混入了一个序列号：
 */
#include <string>
#include <ctime>
#include <iostream>
using namespace std;

template<class T> class TimeStamped : public T {
  long timeStamp;
public:
  TimeStamped() { timeStamp = time(0); }
  long getStamp() { return timeStamp; }
};

template<class T> class SerialNumbered : public T {
  long serialNumber;
  static long counter;
public:
  SerialNumbered() { serialNumber = counter++; }
  long getSerialNumber() { return serialNumber; }
};

// Define and initialize the static storage:
template<class T> long SerialNumbered<T>::counter = 1;

class Basic {
  string value;
public:
  void set(string val) { value = val; }
  string get() { return value; }
};

// 在 main() 方法中，mixin1 和 mixin2 所产生的类型都拥有所混入的类型的所有方法。你可以将混型想象为一种将已有的类映射到新的子类上的功能。
// 可以看到，用这种技术创建混型是多么简单。基本上，你只需要声明“这就是我想要的”，然后它就出现了：
int main() {
  TimeStamped<SerialNumbered<Basic>> mixin1, mixin2;
  mixin1.set("test string 1");
  mixin2.set("test string 2");
  cout << mixin1.get() << " " << mixin1.getStamp() <<
    " " << mixin1.getSerialNumber() << endl;
  cout << mixin2.get() << " " << mixin2.getStamp() <<
    " " << mixin2.getSerialNumber() << endl;
}
/* Output:
test string 1 1452987605 1
test string 2 1452987605 2
*/
