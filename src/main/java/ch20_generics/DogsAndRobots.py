# generics/DogsAndRobots.py
# (c)2021 MindView LLC: see Copyright.txt
# We make no guarantees that this code is fit for any purpose.
# Visit http://OnJava8.com for more book information.
# P.731 §20.15 潜在类型机制 §20.15.1 Python 中的潜在类型机制

class Dog:
    def speak(self):
        print("Arf!")
    def sit(self):
        print("Sitting")
    def reproduce(self):
        pass

class Robot:
    def speak(self):
        print("Click!")
    def sit(self):
        print("Clank!")
    def oilChange(self):
        pass
"""
【Python：动态潜在类型机制的极致灵活】
本例展示了最纯粹的“鸭子类型（Duck Typing）”：只关注对象是否具备特定方法（speak/sit），完全忽略对象的继承关系与类型标记。
这与 Java 泛型强制要求的“名义类型（Nominal Typing）”形成鲜明对比——Java 必须通过显式的接口继承（extends Interface）来赋予类型资格。
Python 证明了代码复用的最高境界是不依赖任何特定层级结构，虽将类型检查推迟至运行时，却最大限度地暴露了 Java 泛型因过度追求编译期边界而丧失的灵活性。
"""
def perform(anything):
    anything.speak()
    anything.sit()

a = Dog()
b = Robot()
perform(a)
perform(b)

output = """
Arf!
Sitting
Click!
Clank!
"""
