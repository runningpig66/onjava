## notes: CircularQueue1.md    FROM ChatGPT《Java-202511》

**你可以在全满，全空，部分有元素（不全满不全空）的3种情况下，分别走一次 get 和 put 吗？这样算下来一共是 6 种路径。然后每种都帮我分析一下 invariant 的具体执行情况。**

好，咱们来“跑表格”，把你要的 3 种状态 × `put/get` 6 条路径全部摊开，把 **前后的两次 `invariant()`** 都说清楚。

先简单回顾一下关键点（和你代码一致的那一版）：

```java
boolean empty()   { return in == out && !wrapped; }
boolean full()    { return in == out &&  wrapped; }

private boolean invariant() {
    // 不变式 1：元素区间里不能有 null
    for (int i = out; i != in; i = (i + 1) % data.length)
        if (data[i] == null)
            throw new CircularQueueException("null in CircularQueue");

    // 不变式 2：非元素区间只能是 null
    if (full()) return true;
    for (int i = in; i != out; i = (i + 1) % data.length)
        if (data[i] != null)
            throw new CircularQueueException(
                "non-null outside of CircularQueue range: " + dump());
    return true;
}

public void put(Object item) {
    precondition(item != null, "put() null item");
    precondition(!full(), "put() into full CircularQueue");

    assert invariant();   // put 前检查

    ... // 写入 data[in]，推进 in，维护 wrapped

    assert invariant();   // put 后检查
}

public Object get() {
    precondition(!empty(), "get() from empty CircularQueue");

    assert invariant();   // get 前检查

    ... // 读 data[out]，置 null，推进 out，维护 wrapped

    assert invariant();   // get 后检查
    return returnVal;
}
```

> 三种状态：
>
> - **空**：`in == out && !wrapped`
> - **满**：`in == out && wrapped`
> - **部分有元素**：`in != out`

下面按你说的 6 条路径逐一走一遍。

------

## 一、起始状态：空队列（in == out && !wrapped）

### 1. 空队列 + `put()`

这条路径是**合法**的，precondition 不会挡住。

1. 调用 `put()`

   - `item != null` → 通过
   - `!full()` 为 `true`（空队列当然不是 full）→ 通过

2. 第一次 `assert invariant()`（**put 前**，状态仍然是空）

   - 第一段：`for(i = out; i != in; ...)`
     - 此时 `in == out`，循环 **0 次**，什么都不检查；
   - `full()` 为 `false`，不会提前 `return true`；
   - 第二段：`for(i = in; i != out; ...)`
     - 同样 `in == out`，循环 **0 次**；
   - `invariant()` 直接返回 `true`。

   👉 **“空队列”时，invariant 实际啥都没检查。**

3. 中间逻辑：

   - 向 `data[in]` 写入一个非 null 元素；
   - `in` 自增；根据原始位置的不同，状态会变成“部分有元素”（一般情况），不会是空，也不会满。

4. 第二次 `assert invariant()`（**put 后**，状态是“部分有元素”）

   - 现在 `in != out` 且 `full() == false`；
   - 第一段 `for(out → in)`：**循环一圈**，检查所有元素位置 `data[i] != null`；
   - `full()` 为 `false`，继续；
   - 第二段 `for(in → out)`：检查所有非元素位置 `data[i] == null`；

   👉 这一次检查是**完整执行不变式 1 + 2** 的。

------

### 2. 空队列 + `get()`

1. 调用 `get()`

   - 第一行 precondition：`!empty()`
   - 此时 `empty()` 为 `true`，所以 `!empty()` 为 `false`，**立刻抛异常**（或 `assert` 失败，取决于你的实现）。

2. 由于 precondition 在方法入口就失败：

   👉 **`invariant()` 完全不会执行**，`get()` 直接异常结束。

**小结：**

- “空 + put”：
  - put 前的 invariant：不检查任何元素（走了个空流程）；
  - put 后的 invariant：完整检查。
- “空 + get”：
  - 被 precondition 拦截，invariant **一次都不跑**。

------

## 二、起始状态：满队列（in == out && wrapped）

### 3. 满队列 + `put()`

1. 调用 `put()`
   - `item != null` 通过；
   - `precondition(!full(), ...)`：
     - 此时 `full()` 为 `true`，所以 `!full()` 为 `false`；
     - precondition 直接抛异常 / 触发断言。

👉 **`invariant()` 仍然完全不执行**，方法在入口就结束。

------

### 4. 满队列 + `get()`

这条路径是合法的。

1. 调用 `get()`

   - `precondition(!empty())`：
     - 满队列显然不是 empty，所以通过。

2. 第一次 `assert invariant()`（**get 前**，状态是“满”）

   此时 `in == out && wrapped == true`：

   - 第一段 `for(i = out; i != in; ...)`
     - 因为 `in == out`，循环 **0 次**，不会检查元素区间；
   - `full()` 为 `true`，所以直接 `return true`；
   - 第二段根本不会执行。

   👉 “**满队列** 的情况下，这次 invariant 调用也**啥都没检查**。”

3. 中间逻辑：

   - 读出 `data[out]`；
   - 把 `data[out]` 置为 `null`；
   - `out++`（或 `% data.length` 回绕），同时 `wrapped = false`；
   - 因为刚刚从满队列删了一个元素，队列变成**“部分有元素”**（至少还有一个元素，除非容量是 1，这个特殊情况我们先不展开，按一般容量 > 1 看）。

4. 第二次 `assert invariant()`（**get 后**）

   现在的状态是“部分有元素”：

   - 第一段 `for(out → in)`：遍历所有元素位置，确保 `data[i] != null`；
   - 其次 `full()` 为 `false`，继续；
   - 第二段 `for(in → out)`：遍历外侧位置，确保 `data[i] == null`。

   👉 这一轮是**完整执行不变式 1 + 2** 的检查。

**小结：**

- “满 + put”：precondition 先挡住，invariant 0 次；
- “满 + get”：
  - get 前的 invariant：由于 `in == out` 且 `full() == true`，两段循环都不跑 → **不检查任何元素**；
  - get 后的 invariant：队列变成“部分有元素”，完整检查一次。

------

## 三、起始状态：不空不满（`in != out`）

这一类比较有意思，因为调用之后的状态可能变成“空”或者“满”。

### 5. 部分有元素 + `put()`

前提：当前**还没满**（否则属于上一节“满 + put”的情况）。

#### 5.1 put 前的 invariant

状态是“部分有元素”，所以：

- 第一段 `for(out → in)`：
  - 访问所有元素下标，检查 `data[i] != null`；
- `full()` 为 `false`；
- 第二段 `for(in → out)`：
  - 访问外侧区间，检查 `data[i] == null`；

👉 这是一个“**完整体检**”。

#### 5.2 执行 put

- 在 `in` 位置写入一个非 null 元素；
- `in` 按规则自增 & 回绕，必要时 `wrapped = true`；
- 此时有两种可能：
  1. 仍然**不满不空**；
  2. 刚好写到了“最后一个空位”，队列变成**满队列**：`in == out && wrapped == true`。

#### 5.3 put 后的 invariant

**情况 A：仍然不空不满**

- 同 5.1，一样是完整执行两段循环。

**情况 B：刚好变成满队列**

- 这时再次调用 `invariant()`，状态是“满”：
  - 第一段因 `in == out` 循环 0 次；
  - `full()` 为 `true`，提前 `return true`；
  - 第二段不执行。

👉 也就是说：

> 「**从部分有元素 put 到刚好满**」这个路径上，
>
> - put 前做了一次全面体检；
> - put 后那次 invariant 在“满队列”状态下其实**什么都没检查**。

------

### 6. 部分有元素 + `get()`

前提：当前**不是空**（否则属于前面的“空 + get”）。

#### 6.1 get 前的 invariant

和 5.1 一样：
 因为 `in != out` 且不是 full：

- 第一段 `for(out → in)`：检查所有元素位置非 null；
- `full()` 为 `false`；
- 第二段 `for(in → out)`：检查所有非元素位置为 null；

👉 完整体检一次。

#### 6.2 执行 get

- 取出 `data[out]`，然后把 `data[out] = null`；
- `out++`（取模，必要时 `wrapped = false`）；

这之后可能有两种结果：

1. **仍然不空不满**；
2. 刚好删掉最后一个元素 → 变成**空队列**，`in == out && !wrapped`。

#### 6.3 get 后的 invariant

**情况 A：仍然不空不满**

- 同 6.1，再做一次完整体检。

**情况 B：刚好变成空队列**

- 这时 `invariant()`：
  - 第一段 `for(out → in)`：`in == out`，循环 0 次；
  - `full()` 为 `false`，继续往下；
  - 第二段 `for(in → out)`：同样 `in == out`，循环 0 次；
  - 直接返回 `true`。

👉 所以：

> 「**从部分有元素 get 到刚好空**」这条路径上，
>
> - get 前做了一次完整体检；
> - get 后再次调用 invariant 时，因为状态变成“空队列”，两段循环都不进，**这一轮其实也完全没检查数组内容**。

------

## 全部 6 条路径的汇总表

| 起始状态   | 调用方法 | precondition | 方法前 `invariant()`                | 方法后 `invariant()`                                 |
| ---------- | -------- | ------------ | ----------------------------------- | ---------------------------------------------------- |
| 空         | `put()`  | 通过         | 空跑（0 次循环）                    | 若变部分有元素→完整检查                              |
| 空         | `get()`  | 失败，抛异常 | 不执行                              | 不执行                                               |
| 满         | `put()`  | 失败，抛异常 | 不执行                              | 不执行                                               |
| 满         | `get()`  | 通过         | 空跑（0 次循环，`full()` 立刻返回） | 变成部分有元素→完整检查                              |
| 部分有元素 | `put()`  | 通过         | 完整检查                            | 若仍部分有元素→完整检查；若刚好变满→空跑（0 次循环） |
| 部分有元素 | `get()`  | 通过         | 完整检查                            | 若仍部分有元素→完整检查；若刚好变空→空跑（0 次循环） |

你前面提的两个疑问其实都可以从这张表里看出来：

1. **空队列时**：两段 `for` 循环都不进，invariant 不会检查“所有位置是否都是 `null`”；
2. **满队列时**：第一段不进循环，`full()` 直接返回，invariant 不会检查“所有位置是否都是非 `null`”。

所以从“数学上严格实现不变式”的角度，**这版 `invariant()` 的确对“空”和“满”两种状态检查不够严**，你之前红框注释的担心完全是对的；只是因为 `put()` / `get()` 自己写得较规范，一般路径不会构造出“空但有脏数据”或者“满但有 null”，所以示例代码跑起来看不出问题而已。