package ch19_reflection;

import java.util.Optional;

/**
 * @author runningpig66
 * @date 2025/12/23 周二
 * @time 15:15
 * P.627 §19.8 使用 Optional
 * <p>
 * 现在假设你已经为自己的惊人创意获得了大量风险投资，并准备好了要招聘人员。
 * 但在职位空缺时，你可以用 Optional 来为 Position 的 Person 字段提供占位符：
 * <p>
 * 这个示例以不同的方式来使用 Optional。注意，title 和 person 都是普通字段，不受 Optional 的保护。
 * 但是，修改这些字段唯一的方法是通过 setTitle() 和 setPerson()，而这两者都使用了 Optional 的功能来对字段加以限制。
 * <p>
 * 对于 Position，我们不需要创建“空”的标记或方法，因为如果 person 字段的值是一个空的 Person 对象，
 * 这就意味着这个 Position 还是处于空缺状态。稍后，你可能会发现必须在此处添加一些明确的内容，
 * 但是根据 YAGNI（You Aren't Going to Need It，你并不需要它）原则，
 * 在初稿中只“尝试最简单且可行的事情”，直到程序的某些方面要求你添加额外的功能，而不是一开始就假设它是必要的。
 */
class EmptyTitleException extends RuntimeException {
}

class Position {
    // 我们并没有将字段存储为 Optional，但使用了 Optional 的功能来对字段施加想要的约束。
    private String title;
    private Person person;

    Position(String jobTitle, Person employee) {
        setTitle(jobTitle);
        setPerson(employee);
    }

    Position(String jobTitle) {
        this(jobTitle, null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        // Throws EmptyTitleException if newTitle is null:
        title = Optional.ofNullable(newTitle).orElseThrow(EmptyTitleException::new);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person newPerson) {
        // Uses empty Person if newPerson is null:
        person = Optional.ofNullable(newPerson).orElse(new Person());
    }

    @Override
    public String toString() {
        return "Position: " + title + ", Employee: " + person;
    }

    public static void main(String[] args) {
        System.out.println(new Position("CEO"));
        System.out.println(new Position("Programmer", new Person("Arthur", "Fonzarelli")));
        try {
            new Position(null);
        } catch (Exception e) {
            System.out.println("caught " + e);
        }
    }
}
/*
Output:
Position: CEO, Employee: <Empty>
Position: Programmer, Employee: Arthur Fonzarelli
caught ch19_reflection.EmptyTitleException
 */
