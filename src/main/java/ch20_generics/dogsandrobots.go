// generics/dogsandrobots.go
// (c)2021 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.
// P.734 §20.15 潜在类型机制 §20.15.3 Go 中的潜在类型机制

package main
import "fmt"

type Dog struct {}
func (this Dog) speak() { fmt.Printf("Arf!\n")}
func (this Dog) sit() { fmt.Printf("Sitting\n")}
func (this Dog) reproduce() {}

type Robot struct {}
func (this Robot) speak() { fmt.Printf("Click!\n") }
func (this Robot) sit() { fmt.Printf("Clank!\n") }
func (this Robot) oilChange() {}
/*
 * 【Go：结构化类型与隐式接口的完美折中】
 * 本例展示了 Go 语言如何通过“结构化类型（Structural Typing）”在静态安全与动态灵活之间找到平衡。
 * Go 允许在参数列表中内联定义匿名接口（interface { speak(); sit() }），且不需要具体类型（Dog/Robot）显式声明实现该接口（无 implements 关键字）。
 * 只要结构满足要求即视为匹配，这种“隐式契约”彻底打破了 Java 中实现类必须依赖接口定义的强耦合，是对 Java 繁琐的“名义类型系统”最现代化的降维打击。
 */
func perform(speaker interface { speak(); sit() }) {
  speaker.speak();
  speaker.sit();
}

func main() {
  perform(Dog{})
  perform(Robot{})
}
/* Output:
Arf!
Sitting
Click!
Clank!
*/
