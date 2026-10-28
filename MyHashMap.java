/**
 * 手写简化版 HashMap：数组 + 链表 + 扩容
 * resize() 是学生版本，仅修正了 3 个编译错误
 */
public class MyHashMap<K, V> {

    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    private int hash(Object key) {
        if (key == null) return 0;      // null key 固定在 0 号桶
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int index(int hash) {
        return hash & (capacity - 1);
    }

    public V put(K key, V value) {
        int h = hash(key);
        int i = index(h);

        Node<K, V> p = table[i];
        while (p != null) {
            if (p.hash == h && (p.key == key || (key != null && key.equals(p.key)))) {
                V oldValue = p.value;
                p.value = value;
                return oldValue;
            }
            p = p.next;
        }

        table[i] = new Node<>(h, key, value, table[i]);
        size++;

        if (size > threshold) {
            resize();
        }
        return null;
    }

    public V get(Object key) {
        int h = hash(key);
        int i = index(h);

        Node<K, V> p = table[i];
        while (p != null) {
            if (p.hash == h && (p.key == key || (key != null && key.equals(p.key)))) {
                return p.value;
            }
            p = p.next;
        }
        return null;
    }

    /** 删除 key，返回被删的值；不存在返回 null */
    public V remove(Object key) {
        int h = hash(key);
        int i = index(h);

        Node<K, V> node = table[i];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.hash == h && (node.key == key || (key != null && key.equals(node.key)))) {
                if (prev == null) {
                    table[i] = node.next;    // 删头：桶直接指向下一个
                } else {
                    prev.next = node.next;   // 删中间/尾巴：前一个跳过当前
                }
                size--;
                return node.value;           // node 本身不能改
            }
            prev = node;
            node = node.next;
        }
        return null;
    }

    /** 扩容：容量翻倍，所有节点重新分配桶 */
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCap = (oldTable == null) ? 0 : oldTable.length;
        int newCap = oldCap * 2;

        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCap];

        table = newTable;
        capacity = newCap;
        threshold = (int) (newCap * LOAD_FACTOR);

        if (oldTable != null) {
            for (int i = 0; i < oldCap; i++) {
                Node<K, V> node = oldTable[i];
                while (node != null) {
                    Node<K, V> next = node.next;
                    int idx = node.hash & (newCap - 1);
                    node.next = newTable[idx];
                    newTable[idx] = node;
                    node = next;
                }
            }
        }
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public void printBuckets() {
        for (int i = 0; i < capacity; i++) {
            if (table[i] == null) continue;
            StringBuilder sb = new StringBuilder("bucket[" + i + "]: ");
            Node<K, V> p = table[i];
            while (p != null) {
                sb.append(p.key).append("=").append(p.value);
                if (p.next != null) sb.append(" -> ");
                p = p.next;
            }
            System.out.println(sb);
        }
    }
}
