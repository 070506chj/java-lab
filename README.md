# java-lab

学习 Java 集合源码过程中的手写实现与小实验。

目标不是"能跑"，而是搞懂 `ArrayList` 和 `HashMap` 内部到底怎么工作，
以及面试官问的那些"为什么"背后的原因。

## 文件说明

| 文件 | 内容 |
|---|---|
| `MyHashMap.java` | 手写 HashMap：数组 + 链表，支持 hash 扰动、桶定位、冲突处理、2 倍扩容、删除、null key |
| `CollectionLab.java` | 通过反射观察 `ArrayList` 与 `HashMap` 的真实容量变化 |
| `Main.java` | 测试入口 |

## 运行

```bash
javac *.java
java Main

# CollectionLab 用了反射读私有字段，需要放开模块
java --add-opens java.base/java.util=ALL-UNNAMED CollectionLab
```

## 实验观察到的结论

**ArrayList 扩容**

```
new 出来时容量 0（懒加载，第一次 add 才分配）
add 第 1 个  -> 10
add 第 11 个 -> 15
add 第 16 个 -> 22
add 第 23 个 -> 33
```
每次扩 1.5 倍（`oldCap + (oldCap >> 1)`），均摊 O(1)。

**HashMap 扩容**

```
new 出来时 table 为 null（懒加载）
put 第 1 个  -> table 长度 16
put 第 13 个 -> table 长度 32
```
阈值 = 容量 × 0.75，16 × 0.75 = 12，所以第 13 个元素触发扩容。

## 几个关键结论

- 下标用 `(n - 1) & hash`，比取模快，但要求容量是 2 的幂，否则桶用不满。
- 扰动函数 `h ^ (h >>> 16)` 让高位参与下标计算，减少冲突。
- 扩容后元素的新下标只有两种可能：原下标 `i`，或 `i + oldCap`。
  因为容量翻倍时 mask 只多了一个高位。
- 删除的本质是让"前驱"绕过当前节点。头节点的前驱不是 `prev`，而是 `table[i]`。
- `null` key 的 hash 固定为 0，所以永远落在 0 号桶；比较时必须先判 `key != null` 再调 `equals`。

## 为什么写这些

面试题背下来的和真正写过一遍的，回答时的底气完全不同。
这个仓库记录的是后者。