package ch19_reflection;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 21:27
 * P.590 §19.1 为什么需要反射
 */
abstract class Shape {
    void draw() {
        System.out.println(this + ".draw()");
    }

    @Override
    public abstract String toString();
}

class Circle extends Shape {
    @Override
    public String toString() {
        return "Circle";
    }
}

class Square extends Shape {
    @Override
    public String toString() {
        return "Square";
    }
}

class Triangle extends Shape {
    @Override
    public String toString() {
        return "Triangle";
    }
}

public class Shapes {
    public static void main(String[] args) {
        Stream.of(new Circle(), new Square(), new Triangle())
                .forEach(Shape::draw);
    }
}
/* Output:
Circle.draw()
Square.draw()
Triangle.draw()
 */
