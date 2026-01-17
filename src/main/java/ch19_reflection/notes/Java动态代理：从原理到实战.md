[TOC]

# Java动态代理：从原理到实战

**标签**：`Proxy` `dynamic proxy` `InvocationHandler`
**日期**：2025-12-22
**代码案例**：ch19: 反射 - Reflection §19.7 动态代理 `SimpleProxyDemo.java` `SimpleDynamicProxy.java` `SelectingMethods.java` 

## 一、动态代理的核心思想

### 1.1 什么是动态代理？
动态代理是在**运行时**创建代理类和对象的技术，所有方法调用都被重定向到一个统一的调用处理器（InvocationHandler）。

### 1.2 核心组件
- **`Proxy`类**：负责生成代理类字节码并创建代理对象
- **`InvocationHandler`接口**：定义代理行为的处理器
- **目标对象**：被代理的真实对象

## 二、静态代理 vs 动态代理

### 2.1 静态代理的问题
```java
// 问题：每个方法都要写重复的代理逻辑
class StaticProxy implements Interface {
    private Interface real;
    
    public void method1() {
        // 前置处理
        real.method1();
        // 后置处理
    }
    
    public void method2() {
        // 前置处理
        real.method2();
        // 后置处理
    }
    // ...每个方法都要写一遍
}
```

### 2.2 动态代理的优势
- **代码复用**：所有方法的代理逻辑集中在`invoke()`方法中
- **维护简单**：接口变化不影响代理代码
- **运行时灵活**：可根据条件动态决定代理行为

## 三、动态代理工作原理

### 3.1 创建动态代理的三要素
```java
Interface proxy = (Interface) Proxy.newProxyInstance(
    Interface.class.getClassLoader(),    // 1. 类加载器
    new Class[]{Interface.class},        // 2. 代理的接口数组
    new InvocationHandler() {            // 3. 调用处理器
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // 统一的代理逻辑
            return method.invoke(realObject, args);
        }
    }
);
```

### 3.2 JVM生成的代理类（伪代码）
```java
final class $Proxy0 extends Proxy implements Interface {
    public $Proxy0(InvocationHandler h) { super(h); }
    
    // 自动生成接口的所有方法实现
    public void doSomething() {
        h.invoke(this, 
            Interface.class.getMethod("doSomething"), 
            null);
    }
    
    public void somethingElse(String arg) {
        h.invoke(this,
            Interface.class.getMethod("somethingElse", String.class),
            new Object[]{arg});
    }
}
```

## 四、关键理解：代理 vs 委托

### 4.1 本质区别
| 特性         | 代理模式           | 委托模式           |
| ------------ | ------------------ | ------------------ |
| **接口要求** | 必须实现完整接口   | 无需实现接口       |
| **方法暴露** | 暴露接口所有方法   | 选择性暴露方法     |
| **关系**     | "替身"关系         | "使用"关系         |
| **目的**     | 控制访问、增强功能 | 功能组合、隐藏细节 |

### 4.2 代码对比

**代理（必须实现所有方法）：**
```java
class Proxy implements Interface {  // 必须实现Interface
    private RealObject real;
    
    public void method1() { real.method1(); }
    public void method2() { real.method2(); }  // 必须实现，即使直接转发
    public void method3() { real.method3(); }  // 必须实现，即使直接转发
}
```

**委托（选择性暴露）：**
```java
class Delegator {  // 不实现Interface
    private Interface real;
    
    // 只暴露需要的方法
    public void exposedMethod() {
        real.method1();  // 内部使用
    }
    // 不暴露method2和method3
}
```

### 4.3 动态代理的限制
动态代理**必须代理接口的所有方法**，但可以在invoke()中做选择性处理：
```java
public Object invoke(Object proxy, Method method, Object[] args) {
    // 只增强特定方法
    if ("importantMethod".equals(method.getName())) {
        System.out.println("增强处理...");
    }
    
    // 可以阻止某些方法
    if ("restrictedMethod".equals(method.getName())) {
        throw new UnsupportedOperationException("方法不可用");
    }
    
    return method.invoke(target, args);
}
```

## 五、动态代理实战应用

### 5.1 日志记录代理
```java
public class LoggingProxy {
    public static <T> T create(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class[]{interfaceClass},
            (proxy, method, args) -> {
                long start = System.currentTimeMillis();
                System.out.println("调用方法: " + method.getName());
                
                try {
                    Object result = method.invoke(target, args);
                    System.out.println("方法执行成功，耗时: " + 
                        (System.currentTimeMillis() - start) + "ms");
                    return result;
                } catch (Exception e) {
                    System.out.println("方法执行失败: " + e.getMessage());
                    throw e;
                }
            }
        );
    }
}
```

### 5.2 缓存代理
```java
public class CacheProxy {
    private Map<String, Object> cache = new HashMap<>();
    
    public static <T> T create(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class[]{interfaceClass},
            new CacheHandler(target)
        );
    }
    
    class CacheHandler implements InvocationHandler {
        private Object target;
        
        public Object invoke(Object proxy, Method method, Object[] args) {
            String key = method.getName() + Arrays.toString(args);
            
            if (cache.containsKey(key)) {
                System.out.println("缓存命中: " + key);
                return cache.get(key);
            }
            
            Object result = method.invoke(target, args);
            cache.put(key, result);
            return result;
        }
    }
}
```

## 六、注意事项与常见陷阱

### 6.1 无限递归陷阱
```java
// ❌ 错误：在invoke中调用proxy的方法
public Object invoke(Object proxy, Method method, Object[] args) {
    // 这会无限递归！
    proxy.toString();  // 再次进入invoke方法
    return method.invoke(target, args);
}
```

### 6.2 正确区分调用来源
```java
public Object invoke(Object proxy, Method method, Object[] args) {
    // 如果要调用目标对象的方法，不要用proxy！
    if (method.getName().startsWith("internal")) {
        // 直接调用目标对象的方法
        return method.invoke(target, args);
    } else {
        // 处理其他方法
        System.out.println("处理外部调用...");
        return method.invoke(target, args);
    }
}
```

### 6.3 性能考虑
- 反射调用比直接调用慢10-100倍
- 适合AOP、监控等非核心路径
- 高频调用场景要考虑性能损失

## 七、在框架中的应用

### 7.1 Spring AOP
```java
// Spring使用动态代理实现AOP
@Service
public class UserService {
    @Transactional
    public User createUser(User user) {
        // 事务由动态代理管理
    }
}
```

### 7.2 MyBatis Mapper接口
```java
// MyBatis为Mapper接口生成动态代理实现
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(int id);
}

// 使用时，MyBatis创建动态代理处理SQL执行
UserMapper mapper = sqlSession.getMapper(UserMapper.class);
```

### 7.3 RPC框架
```java
// RPC客户端使用动态代理透明调用远程服务
public interface UserService {
    User getUser(int id);
}

// 动态代理将本地调用转为网络请求
UserService service = RpcProxy.create(UserService.class, "127.0.0.1:8080");
User user = service.getUser(123);  // 实际发送网络请求
```

## 八、总结要点

### 8.1 核心理解
1. **动态代理 = 运行时生成类 + 方法调用统一转发**
2. **Proxy是"工厂"，InvocationHandler是"处理器"**
3. **所有方法调用都经过invoke()，实现集中控制**

### 8.2 适用场景
- **横切关注点**：日志、事务、权限、缓存
- **接口增强**：为已有接口添加额外功能
- **透明代理**：客户端无需知道代理存在
- **远程调用**：透明化远程服务调用

### 8.3 不适用场景
- 对性能要求极高的核心业务
- 需要代理类而非接口的情况
- 需要选择性暴露方法的场景（考虑委托模式）

### 8.4 学习建议
1. 先理解静态代理的痛点
2. 掌握`Proxy.newProxyInstance()`的三参数含义
3. 理解`InvocationHandler.invoke()`的工作机制
4. 通过实际案例理解应用场景
5. 注意代理和委托的区别与选择

---

**最后提醒**：动态代理是Java高级特性，理解它需要时间。当你遇到"为什么需要这样设计"的疑问时，回想一下静态代理的缺点，就能理解动态代理的价值所在。记住，技术选择总是权衡的结果，动态代理提供了灵活性和解耦，但也带来了性能开销和复杂性。