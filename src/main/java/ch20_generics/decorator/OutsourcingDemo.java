package ch20_generics.decorator;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author runningpig66
 * @date 2026/1/15 周四
 * @time 21:10
 * P.728 §20.14 混型 §20.14.3 使用装饰器模式
 * <p>
 * 这个“层层外包、层层扒皮”的比喻简直太生动了！而且非常深刻地揭示了装饰器模式（Decorator）和
 * 责任链模式（Chain of Responsibility）在实际业务中的阴暗面……啊不，是灵活性。
 * 正如你所说，装饰器最强大的地方就在于它既可以**“篡改入参”（在传给下一层之前，把数据改了），
 * 也可以“篡改出参”**（拿到下一层干完活的结果，再偷偷改了）。
 * 结合你记账/外包的业务背景，我们设计这样一个实际场景：
 * <p>
 * 实际业务场景：日本 IT 外包项目报价系统
 * 假设你的 App 里有一个模块是给甲方客户生成“项目最终报价单”。
 * 最里面的人（实际干活的程序员）：根据工时算出基础成本（比如 20 万日元）。
 * 第 1 层外包（项目经理）：为了显得工作量大，偷偷篡改入参，把程序员报的 100 小时工时，改成 120 小时。
 * 第 2 层外包（派遣公司老板）：拿到计算结果后，篡改出参，直接在总价上加 50% 的管理费。
 * 第 3 层外包（税局/发票系统）：篡改出参，在总价上再加 10% 的消费税。
 * 这就是一个既有入参篡改（虚报工时），又有出参篡改（加收管理费）的经典案例。
 * <p>
 * 既有入参篡改，又有出参篡改的装饰器模式示例
 * 场景：层层外包的项目报价计算器
 */
// 1. 数据模型：项目请求（入参）
class ProjectRequest {
    String projectName;
    BigDecimal hourlyRate; // 时薪
    int hours; // 工时

    public ProjectRequest(String projectName, BigDecimal hourlyRate, int hours) {
        this.projectName = projectName;
        this.hourlyRate = hourlyRate;
        this.hours = hours;
    }
}

// 2. 顶层接口：估价器
interface Estimator {
    /**
     * @param request 项目请求详情
     * @return 计算出的总价
     */
    BigDecimal estimate(ProjectRequest request);
}

// 3. 核心组件：老实肯干的程序员 (Base Component)
// 他只拿固定的时薪，工时是多少就是多少
class HonestDeveloper implements Estimator {
    @Override
    public BigDecimal estimate(ProjectRequest request) {
        // 核心逻辑：单价 * 时间
        BigDecimal cost = request.hourlyRate.multiply(new BigDecimal(request.hours));
        System.out.println("【核心】程序员实际干活：单价 " + request.hourlyRate + " * 工时 " + request.hours + " = " + cost);
        return cost;
    }
}

// 4. 装饰器基类 (Middleman)
abstract class EstimatorDecorator implements Estimator {
    private Estimator estimator;

    public EstimatorDecorator(Estimator estimator) {
        this.estimator = estimator;
    }

    // 默认行为是不篡改，直接透传
    @Override
    public BigDecimal estimate(ProjectRequest request) {
        return estimator.estimate(request);
    }
}

// 5. 具体装饰器 1：狡猾的项目经理 (入参篡改者)
// 他的作用：在把任务派给程序员之前，先虚报工时！
class ProjectManagerDecorator extends EstimatorDecorator {
    public ProjectManagerDecorator(Estimator estimator) {
        super(estimator);
    }

    @Override
    public BigDecimal estimate(ProjectRequest request) {
        // --- 1. 篡改入参 (Before Logic) ---
        // 经理心想：程序员说干100小时，我给客户报120小时不过分吧？
        int inflatedHours = (int) (request.hours * 1.2); // 虚报 20%
        System.out.println("【中介1】项目经理：偷偷把工时从 " + request.hours + " 改成了 " + inflatedHours);

        // 创建一个新的请求对象（或者修改原对象），传给下一层
        ProjectRequest fakedRequest = new ProjectRequest(request.projectName, request.hourlyRate, inflatedHours);

        // --- 2. 调用下一层 ---
        // 此时，核心程序员拿到的是被篡改过的工时
        BigDecimal result = super.estimate(fakedRequest);

        // --- 3. 出参保持不变（经理只靠虚报工时赚钱，不直接加价）---
        return result;
    }
}

// 6. 具体装饰器 2：贪婪的派遣公司老板 (出参篡改者)
// 他的作用：拿到下属算出来的总价，直接乘以 1.5 倍作为管理费
class CompanyBossDecorator extends EstimatorDecorator {
    public CompanyBossDecorator(Estimator nextEstimator) {
        super(nextEstimator);
    }

    @Override
    public BigDecimal estimate(ProjectRequest request) {
        // --- 1. 入参保持不变（老板不关心细节）---
        // 直接让下面的人去算
        BigDecimal rawCost = super.estimate(request);

        // --- 2. 篡改出参 (After Logic) ---
        // 老板心想：不管成本多少，我要拿 50% 的利润
        BigDecimal finalPrice = rawCost.multiply(new BigDecimal("1.5"));
        System.out.println("【中介2】派遣公司老板：在成本 " + rawCost + " 的基础上加价 50% -> " + finalPrice);
        return finalPrice;
    }
}

// 7. 具体装饰器 3：税务系统 (出参篡改者)
// 最后还要加税
class TaxDecorator extends EstimatorDecorator {
    public TaxDecorator(Estimator nextEstimator) {
        super(nextEstimator);
    }

    @Override
    public BigDecimal estimate(ProjectRequest request) {
        BigDecimal preTaxPrice = super.estimate(request);

        // 加 10% 税
        BigDecimal withTax = preTaxPrice.multiply(new BigDecimal("1.1"));
        System.out.println("【政府】税务系统：含税价 " + withTax);
        return withTax;
    }
}

// 8. 客户端调用
public class OutsourcingDemo {
    public static void main(String[] args) {
        // 原始需求：时薪 2000，工时 100 小时，理论上程序员应该拿：200,000
        ProjectRequest req = new ProjectRequest("アプリ開発", new BigDecimal("2000"), 100);
        BigDecimal rawPrice = new HonestDeveloper().estimate(req);
        System.out.println("=== 开始报价流程 ===");

        // 层层包装：
        // 最外面是税 -> 里面是老板 -> 再里面是经理 -> 核心是程序员
        Estimator finalQuote =
                new TaxDecorator(
                        new CompanyBossDecorator(
                                new ProjectManagerDecorator(
                                        new HonestDeveloper()
                                )
                        )
                );

        // 客户请求报价
        BigDecimal finalPrice = finalQuote.estimate(req);
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("\n=== 最终结果 ===");
        System.out.println("客户原本只要付: " + df.format(rawPrice));
        System.out.println("层层剥皮后变成: " + df.format(finalPrice));
    }
}
/* Output:
【核心】程序员实际干活：单价 2000 * 工时 100 = 200000
=== 开始报价流程 ===
【中介1】项目经理：偷偷把工时从 100 改成了 120
【核心】程序员实际干活：单价 2000 * 工时 120 = 240000
【中介2】派遣公司老板：在成本 240000 的基础上加价 50% -> 360000.0
【政府】税务系统：含税价 396000.00

=== 最终结果 ===
客户原本只要付: 200,000
层层剥皮后变成: 396,000
 */
