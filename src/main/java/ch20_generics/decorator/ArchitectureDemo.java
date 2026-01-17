package ch20_generics.decorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author runningpig66
 * @date 2026/1/16 周五
 * @time 0:33
 * P.728 §20.14 混型 §20.14.3 使用装饰器模式
 * <p>
 * 实际 Android 开发架构中的装饰器模式示例
 * 场景：数据仓库层（Repository），为网络请求动态添加缓存策略和性能日志。
 * <p>
 * 1. 开闭原则 (Open/Closed Principle)：
 * 开始写 App，你只有 RemoteBillDataSource（只查网络）。
 * 上线后用户反馈慢，你需要加缓存。你不需要修改 RemoteBillDataSource 里的任何一行代码，也不用改 ViewModel。
 * 你只需要新建一个 CachingDataSource，然后在创建 Repository 的地方（通常是 Hilt/Dagger 注入的地方）套上一层即可。
 * 2. 解耦 (Decoupling)：
 * 网络请求的代码只管网络（Remote）。
 * 缓存逻辑的代码只管缓存（Cache）。
 * 它们互不干扰，测试的时候可以单独测网络，也可以单独测缓存逻辑。
 */
// 1. 数据实体 (Entity)
class BillRecord {
    String id;
    double amount;
    String note;

    public BillRecord(String id, double amount, String note) {
        this.id = id;
        this.amount = amount;
        this.note = note;
    }

    @Override
    public String toString() {
        return "{id='" + id + "', amount=" + amount + ", note='" + note + "'}";
    }
}

// 2. 顶层接口 (Component)
// 定义数据源的标准行为。
// 在 Android 中，这通常是 Repository 接口，UI 层（ViewModel）只依赖这个接口。
interface BillDataSource {
    // 获取指定用户的账单列表
    List<BillRecord> getBills(String userId);
}

// 3. 核心组件 (Concrete Component)
// 对应：远程数据源 (RemoteDataSource)。
// 职责：负责真正的网络请求（模拟 Retrofit / OkHttp 调用）。
class RemoteBillDataSource implements BillDataSource {
    @Override
    public List<BillRecord> getBills(String userId) {
        // 模拟网络耗时
        try {
            System.out.println("[Network] 正在从服务器拉取数据...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Tip: 你的处理：把它包装成 RuntimeException 抛出去，或者在真实 App 中，我们会在这里 catch 住异常，然后返回一个 Result.Error 或者空列表，保证 App 不会崩。
            throw new RuntimeException(e);
        }
        // 模拟服务器返回的数据
        List<BillRecord> bills = new ArrayList<>();
        bills.add(new BillRecord("1001", 50.00, "午餐"));
        bills.add(new BillRecord("1002", 3600.00, "买手机"));
        return bills;
    }
}

// 4. 装饰器基类 (Decorator)
// 职责：持有另一个 DataSource，标准转发。
abstract class DataSourceDecorator implements BillDataSource {
    protected BillDataSource nextDataSource;

    public DataSourceDecorator(BillDataSource nextDataSource) {
        this.nextDataSource = nextDataSource;
    }

    @Override
    public List<BillRecord> getBills(String userId) {
        return nextDataSource.getBills(userId);
    }
}

// 5. 具体装饰器 A：内存缓存装饰器 (Caching Decorator)
// 职责：实现“读写穿透”缓存策略。
// 逻辑：先查缓存 -> 有则直接返回（拦截） -> 无则查下一层（通常是网络） -> 拿到结果存缓存 -> 返回。
class CachingDataSource extends DataSourceDecorator {
    // 模拟内存缓存 (Map)
    /*
    Tip: 为什么 MEMORY_CACHE 要用 static？
    Demo 里的原因：在这个简单的 main 函数里，如果不加 static，每次 new CachingDataSource() 都会创建一个新的空 Map。如果你在代码其他地方重新 new 了一个 Repository，缓存就丢失了。加了 static 保证了全局共享。
    Android 实战的映射：在真正的 Android 开发（结合 Hilt/Dagger）中，我们通常不使用 static。我们会把 CachingDataSource 标记为 @Singleton（单例作用域）。
    Hilt 的做法：Hilt 保证了整个 App 生命周期内 CachingDataSource 只有一个实例，所以那个 Map 也就只有一份，不需要 static 修饰。这比 static 更容易进行单元测试（因为测试完可以销毁单例，而 static 很难清理）。
     */
    private static final Map<String, List<BillRecord>> MEMORY_CACHE = new HashMap<>();
    private static final long CACHE_EXPIRY = 60 * 1000; // 假设缓存有效期
    private long lastUpdateTime = 0;

    public CachingDataSource(BillDataSource nextDataSource) {
        super(nextDataSource);
    }

    @Override
    public List<BillRecord> getBills(String userId) {
        // --- 1. 尝试从缓存读取 (拦截入参) ---
        if (MEMORY_CACHE.containsKey(userId) &&
                System.currentTimeMillis() - lastUpdateTime < CACHE_EXPIRY) {
            System.out.println("[Cache] 命中内存缓存，直接返回，跳过网络请求。");
            return MEMORY_CACHE.get(userId);
        }
        // --- 2. 缓存未命中，委托下一层 (网络层) ---
        System.out.println("[Cache] 缓存为空或过期，穿透请求下一层...");
        List<BillRecord> data = super.getBills(userId);

        // --- 3. 结果后处理 (写入缓存) ---
        if (data != null && !data.isEmpty()) {
            MEMORY_CACHE.put(userId, data);
            lastUpdateTime = System.currentTimeMillis();
            System.out.println("[Cache] 已将网络数据写入本地缓存。");
        }
        return data;
    }

    // 额外功能：手动清除缓存（装饰器特有的扩展能力）
    public void invalidateCache() {
        MEMORY_CACHE.clear();
        System.out.println("[Cache] 缓存已清空");
    }
}

// 6. 具体装饰器 B：性能日志装饰器 (Logging Decorator)
// 职责：监控数据加载耗时，用于性能分析。
class PerformanceLoggerDataSource extends DataSourceDecorator {
    public PerformanceLoggerDataSource(BillDataSource nextDataSource) {
        super(nextDataSource);
    }

    @Override
    public List<BillRecord> getBills(String userId) {
        long startTime = System.currentTimeMillis();
        // 委托执行
        List<BillRecord> result = super.getBills(userId);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("[Logger] 数据加载耗时: " + duration + "ms");
        return result;
    }
}

// 客户端代码 (模拟 ViewModel 或 Dependency Injection 模块)
public class ArchitectureDemo {
    public static void main(String[] args) {
        String currentUserId = "user_123";
        // --- 组装数据源 (Dependency Injection) ---
        // 结构：日志(最外层) -> 缓存(中间层) -> 网络(核心层)
        // 这样包裹的好处：日志层会统计“缓存+网络”的总耗时，或者看缓存是否命中
        BillDataSource repository =
                new PerformanceLoggerDataSource(
                        new CachingDataSource(
                                new RemoteBillDataSource()
                        )
                );
        System.out.println("=== 第一次请求 (冷启动) ===");
        // 预期：穿透缓存 -> 访问网络 -> 写入缓存 -> 打印耗时
        List<BillRecord> data1 = repository.getBills(currentUserId);
        System.out.println("UI显示数据: " + data1);

        System.out.println("\n=== 第二次请求 (刷新界面) ===");
        // 预期：命中缓存 -> 直接返回 -> 耗时极短
        List<BillRecord> data2 = repository.getBills(currentUserId);
        System.out.println("UI显示数据: " + data2);
    }
}
/* Output:
=== 第一次请求 (冷启动) ===
[Cache] 缓存为空或过期，穿透请求下一层...
[Network] 正在从服务器拉取数据...
[Cache] 已将网络数据写入本地缓存。
[Logger] 数据加载耗时: 1007ms
UI显示数据: [{id='1001', amount=50.0, note='午餐'}, {id='1002', amount=3600.0, note='买手机'}]

=== 第二次请求 (刷新界面) ===
[Cache] 命中内存缓存，直接返回，跳过网络请求。
[Logger] 数据加载耗时: 0ms
UI显示数据: [{id='1001', amount=50.0, note='午餐'}, {id='1002', amount=3600.0, note='买手机'}]

 */
