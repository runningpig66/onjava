[TOC]

### 1. 原书示例代码的执行流程梳理 (12.1s)

在程序正式进入 batch() 方法构建流之前，必须首先完成前置的面糊准备工作，这一步由 Batter.mix() 方法主导。在该方法内部，程序并发提交了准备鸡蛋、牛奶、糖和面粉这四个异步任务。由于它们在底层线程池中是完全并行执行的，因此这四个各自耗时 1.1 秒的任务总共只消耗 1.1 秒的绝对时间。随后，allOf().join() 充当了严格的并发屏障，强制主线程阻塞并等待这四种配料全部准备就绪。配料齐备后，主线程紧接着执行耗时 1.1 秒的面糊混合操作。因此，在全局时间轴的最初阶段，准备面糊这个前置任务就已经固定消耗了 2.2 秒的物理时间，并最终向后游传递了一个处于完成状态的 CompletableFuture<Batter> 凭证。

在调用 forEach 方法之前，主线程首先需要完成 Stream 对象的构建。Stream.of 内部传递了四个 bake(batter) 方法调用，由于 Java 严格遵循计算方法参数的执行顺序，这四个 bake 方法实际上是在主线程中被瞬间且串行执行的。这里并没有使用 parallel() 来开启流的并发，流本身依然是普通的串行流。代码之所以展现出高并发的执行效果，是因为流内部装载的元素并不是静态的最终数据，而是四个已经被瞬间分发到后台并开始独立运转的异步任务凭证。

深入到单次 bake 方法的内部，当主线程执行时，传入的 batter 对象已经是处于完成状态的 CompletableFuture<Batter>。主线程遇到第一个 thenApplyAsync 操作，会立刻将生成 Pan（平底锅）的任务提交给 ForkJoinPool 后台线程池。主线程在此处绝对不会发生任何阻塞，而是瞬间拿到一个代表 Pan 的、尚未完成的 CompletableFuture<Pan> 凭证。就在主线程拿到凭证的这一刻，底层分配的后台子线程已经开始执行那段耗时 1.1 秒的准备任务了。

紧接着，主线程拿着这个尚未完成的 Pan 凭证，继续调用第二个 thenApplyAsync 来拼接生成 Baked（烘焙对象）的操作。由于前置的 Pan 任务远未结束，主线程依然不会停下来死等。框架的底层机制会将这个后续的转换操作当作一个回调任务，直接登记在 Pan 凭证的内部等待队列中。当后台线程终于完成 Pan 任务后，由于使用的是带有 Async 后缀的方法，框架会强制将排队的 heat 任务重新打包，再次分配给线程池中的空闲线程去执行，以此实现完整的线程切换。登记完这个回调任务后，主线程瞬间拿到了最终的 Baked 凭证，并立即退出 bake 方法。

因此，当程序执行完 batch() 方法，站在 forEach 循环入口的前一刻，主线程在时间轴上实际上仅仅走过了几毫秒。此时的主线程已经成功构建了一个完整的 Stream，里面整齐地存放着四个未完成的 CompletableFuture<Baked> 凭证。而在主线程的主观视角之外，底层的线程池中正有四个子线程在完全并行地全速执行最初的 Pan 任务。距离这四个任务最终变成真实的 Baked 对象，至少还需要 2.2 秒的物理时间。

当主线程携带构建好的 Stream 踏入 forEach 循环时，第一轮循环的执行揭示了并发降级的核心原因。主线程首先遭遇了 Frosting.make() 的同步调用，这导致主线程被硬生生卡住 1.1 秒。就在主线程阻塞的这段时间里，后台四个并行运转的 Pan 任务恰好执行完毕，并紧接着触发了后续耗时 1.1 秒的 Baked 任务。当主线程完成第一次糖霜制作 (Frosting.make())，装配好 thenCombineAsync 链条并最终执行到 join() 时，第一个 Baked 任务仍在后台运行。因此，主线程被迫再次阻塞 1.1 秒以等待 Baked 的彻底完成。待两者均就位后，合并生成 FrostedCake 的任务被提交给后台子线程并耗时 1.1 秒完成。至此，第一轮循环在主线程的走走停停中总共消耗了 3.3 秒，加上前期准备面糊的 2.2 秒

### 2. 两种并发改造方案的对比 (5.5s)

在将原本耗时 12.1 秒的并发流水线优化至理论极限 5.5 秒的过程中，出现了两种截然不同的改造思路。第一种思路保持了原始代码中制作糖霜方法 `Frosting.make()` 的同步阻塞特性，通过在外部流式调用中强行添加 parallel 开启并发流来实现多线程推进。

```java
    // 1. 保持原有的同步阻塞设计，方法内部存在 1.1 秒的物理休眠
    static CompletableFuture<Frosting> make() {
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + Frosting.class.getSimpleName());
        new Nap(1.1);
        return CompletableFuture.completedFuture(new Frosting());
    }

    public static void main(String[] args) {
        Timer timer = new Timer();

        CompletableFuture<?>[] futures = Baked.batch()
                .map(baked -> baked
                        .thenCombineAsync(Frosting.make(), FrostedCake::new)
                        .thenAcceptAsync(System.out::println))
                // 2. 强行开启并发流，试图用增加底层物理线程的方式去硬抗上述阻塞（主线程会参与任务分配）
                .parallel()
                .toArray(CompletableFuture[]::new);
        // 3. 最终设立全局屏障等待，但在走到这一步之前，主线程可能已在中途被卡住
        CompletableFuture.allOf(futures).join();

        System.out.println(timer.duration());
    }
```

第二种思路则是从源头将制作糖霜的方法 `Frosting.make()` 重构为返回 CompletableFuture 的纯异步操作，放弃使用并发流，转而通过普通的 map 操作极速派发任务，并在最后设立统一的全局并发屏障。

```java
    // 1. 使用 supplyAsync 将耗时操作真正移交到底层线程池
    static CompletableFuture<Frosting> make() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                    "prepare: " + Frosting.class.getSimpleName());
            new Nap(1.1);
            return new Frosting();
        });
    }

    public static void main(String[] args) {
        Timer timer = new Timer();

        // 2 将 forEach 替换为 map，只派发任务，不中途阻塞
        CompletableFuture<?>[] futures = Baked.batch()
                .map(baked -> baked
                        // 此时 Frosting.make() 瞬间返回异步票据，主线程不会卡顿
                        .thenCombineAsync(Frosting.make(), FrostedCake2::new)
                        .thenAcceptAsync(System.out::println))
                // 将所有流水线的末端凭证收集到数组中
                .toArray(CompletableFuture[]::new);
        // 3：在主线程快速派发完 4 条流水线后，统一设立全局并发屏障
        CompletableFuture.allOf(futures).join();

        System.out.println(timer.duration());
    }
/* Output 1:
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Sugar
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Flour
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Milk
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Eggs
currentThread: [main], prepare: Batter
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-5], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-6], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-7], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-8], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-5], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-7], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-8], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-5], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-7], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-4], FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-7], FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-5], FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-1], FrostedCake2
5581
 */
```

虽然第一种通过开启并发流的方案在运行结果上成功达成了 5.5 秒的目标，但从底层机制和架构规范来看，这属于一种典型的**并发反模式**。在该方案中，parallel 会召唤 ForkJoinPool 里的多个工作线程来分别执行 map 里的逻辑，而这里隐藏着一个极具破坏性的细节：这些被召唤的工作线程中通常会**包含主线程本身**。这意味着，当主线程参与流的调度时，也会直接撞上糖霜方法内部 1.1 秒的物理休眠，从而被死死卡住。这种做法的本质是用增加物理线程数量的粗暴方式，甚至不惜牺牲主线程的流动性，去硬抗同步代码带来的阻塞耗时。

从架构设计的宏观视角来看，主线程的职责定位应当是全局的调度者。它负责接收信号、极速派发子线程任务以及统筹全局的运转，绝不应该在中途被拉去充当苦力并陷入具体的耗时计算或同步阻塞中。即使在某些极端情况下（例如糖霜制作耗时变成系统瓶颈所在的关键路径），主线程中途受阻似乎并不会从数值上拖慢整体的最终运行时间，但让唯一的调度线程失去响应性依然是系统设计的大忌。主线程唯一应该发生主动阻塞的时刻，只有在彻底完成所有前置调度与分发任务后，于程序的最末端显式调用 join 方法，在全局并发屏障处统一等待最终执行数据的汇合。

这种反模式做法同时也导致了严重的框架职责错位与滥用。Java 引入并发流设计的初衷是处理 CPU 密集型的大规模纯数据计算。将带有休眠等待、网络请求或 I/O 操作的重度阻塞逻辑塞入并发流中极其危险。由于并发流默认共享整个 Java 虚拟机中唯一的公共 ForkJoin 线程池，强行让多个线程处于休眠等待状态会迅速耗尽公共线程资源。在真实的生产环境中，一旦并发量升高，这种做法会导致整个程序中其他所有依赖该公共池的任务全部陷入停滞，引发严重的线程饥饿问题。

从 API 语义规范和异步生态的完整性来看，如果一个核心方法内部具有较长的固有耗时，其 API 签名本身就应当返回异步类型的票据，以此向调用者明确声明其异步非阻塞的特性。保持原有的同步设计不仅掩盖了潜在的性能风险，还会迫使外部调用者不得不使用并发流等手段来规避性能坍塌。同时，CompletableFuture 具有一套完整的非阻塞回调链条，在其中强行混入维持同步阻塞的并发流，会使整个纯异步的上下文被生硬割裂。

相比之下，第二种重构底层方法为纯异步操作的方案才是符合规范的标准做法。在该方案中，主线程完美履行了极速任务派发者的职责，在几毫秒内便通过普通的 map 映射完成了所有异步任务节点的拼接与组装，期间没有任何线程因为同步等待而被强制挂起。所有真实的耗时操作都被平滑地移交给了 CompletableFuture 框架底层进行异步调度。

在完成全部任务派发后，通过将所有的流水线末端凭证收集为数组，并统一调用 allOf 的 join 方法，程序在执行的最末端设立了一道标准的全局并发屏障。这道屏障完美地实现了对发散异步水流的统一拦截与验收，确保主线程在所有并发分支彻底完工前不会提前退出。这种设计不仅彻底消除了源头阻塞，避免了底层线程池资源的无谓浪费，还以最干净、安全的方式实现了真正意义上的全链路并行。

### 3. 串行流中混入同步阻塞的案例记录 (7.7s)

```java
    public static void main(String[] args) {
        Timer timer = new Timer();

        CompletableFuture<?>[] futures = Baked.batch()
                .map(baked -> baked
                        .thenCombineAsync(Frosting.make(), FrostedCake::new)
                        .thenAcceptAsync(System.out::println))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();

        System.out.println(timer.duration());
    }
/* Output 1:
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Milk
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Flour
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Eggs
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Sugar

currentThread: [main], prepare: Batter

currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Pan
currentThread: [main], prepare: Frosting

currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-8], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Baked
currentThread: [main], prepare: Frosting

currentThread: [main], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: FrostedCake

currentThread: [main], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-8], prepare: FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-1], FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-4], FrostedCake

currentThread: [ForkJoinPool.commonPool-worker-8], FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-1], FrostedCake
7777
 */
```

为了更深刻地理解主线程参与阻塞调用所带来的性能损耗，我们可以分析一个不使用并发流的对照案例。在原始的流式调用中，如果不修改制作糖霜方法的同步阻塞特性，并且去掉了 parallel() 方法，整个 Stream 就会默认作为串行流执行。在串行流的机制下，map 操作中的转换逻辑必须由发起终端操作的线程（即主线程）依次顺序执行。由于传入的 Frosting.make() 是一个包含 1.1 秒物理休眠的同步方法，主线程在遍历流的过程中会发生四次连续的线程阻塞。这种机制导致总绝对执行时间精准地落在 7.7 秒，我们可以将其严格划分为七个耗时 1.1 秒的连续时间块，以还原底层的真实执行轨迹。

在第一个时间块（0.0s - 1.1s）中，程序的并发点集中在配料准备阶段。底层的四个工作线程在 ForkJoinPool 中并行执行鸡蛋、牛奶、糖和面粉的准备任务。此时，主线程执行到了 allOf().join()，由于前置任务尚未完成，主线程进入等待状态。

进入第二个时间块（1.1s - 2.2s），四种配料准备就绪，allOf 屏障放行。主线程解除阻塞，开始执行耗时 1.1 秒的面糊混合操作（Batter.mix）。至此，耗时 2.2 秒的前置工作全部结束，主线程携带着完整的面糊对象，开始构建流并正式踏入 map 转换阶段。

在第三个时间块（2.2s - 3.3s），主线程瞬间生成了四个包含平底锅（Pan）任务的异步票据，底层的四个工作线程立刻开始并行处理这些耗时 1.1 秒的平底锅任务。与此同时，主线程开始对流的第一个元素执行 map 操作，调用了同步的 Frosting.make() 方法。这导致主线程立刻被挂起 1.1 秒。在这个时间块结束时，主线程完成了第一份糖霜的制作，而底层的四个平底锅任务也恰好全部执行完毕。

进入第四个时间块（3.3s - 4.4s），底层的平底锅任务通过异步回调无缝触发了后续的烘焙（Baked）任务，四个工作线程继续并行烘烤面包。主线程方面，在完成了第一份糖霜后，迅速将其与第一个烘焙票据通过 thenCombineAsync 绑定，完成了第一条流水线的装配。由于是串行流，主线程必须立刻处理流的第二个元素，因此再次调用 Frosting.make()，触发了第二次长达 1.1 秒的线程挂起。当该时间块结束时，底层所有的烘焙任务已经全部完成，四个真实的 Baked 对象被存放在堆内存中。

在第五个时间块（4.4s - 5.5s），主线程结束了第二次休眠，完成了第二份糖霜。此时，由于前两个蛋糕的 Baked 对象早已在上一阶段由子线程准备完毕，thenCombineAsync 瞬间集齐了双参数，立刻将前两个蛋糕的最终组装任务（FrostedCake::new）派发给底层线程并行执行。派发完成后，主线程继续遍历流的第三个元素，发生第三次 1.1 秒的同步阻塞。

来到第六个时间块（5.5s - 6.6s），底层线程完成了前两个蛋糕的组装，并通过 thenAcceptAsync 打印了输出结果。主线程结束第三次休眠，将第三份糖霜与早已就绪的第三个 Baked 对象结合，派发了第三个蛋糕的组装任务。随后，主线程进入流的最后一个元素，第四次被挂起 1.1 秒以制作最后一份糖霜。

在最后一个时间块（6.6s - 7.7s），第三个蛋糕在底层组装完毕并打印。主线程结束了最后一次挂起，派发了第四个蛋糕的组装任务，并终于完成了整个流的 map 遍历。主线程迅速执行 toArray 收集票据，并调用 allOf().join() 设立最终的全局屏障。此时，程序只需等待底层工作线程将最后一个蛋糕组装完毕（耗时 1.1 秒）。该任务完成后，屏障放行，整个程序的生命周期在精准的 7.7 秒处终止。

这个 7.7 秒的案例精确地揭示了流式编程与异步框架结合时的一个核心误区。串行流要求遍历操作严格按序执行，如果在遍历节点中混入了同步耗时调用，会导致主线程无法快速完成异步任务图（DAG）的构建与派发。主线程因执行具体的耗时逻辑而频繁挂起，不仅切断了任务派发的连续性，还使得原本可以完全并行的下游异步任务因为得不到及时调度而被迫交错等待，最终将高并发的异步模型退化成了局部的串行执行。
