public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        String[] keys = {"key1", "key2", "abc", "hello", "GitHub", "java", "spring", "mysql", "redis", "linux"};
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], i);
        }

        System.out.println("size = " + map.size());
        System.out.println("get(\"abc\")      = " + map.get("abc"));
        System.out.println("get(\"GitHub\")   = " + map.get("GitHub"));
        System.out.println("get(\"不存在的key\") = " + map.get("不存在的key"));

        System.out.println("\n--- 覆盖测试 ---");
        Integer old = map.put("abc", 999);
        System.out.println("put(\"abc\", 999) 返回旧值 = " + old);
        System.out.println("现在 get(\"abc\") = " + map.get("abc"));
        System.out.println("size 仍然是 " + map.size() + "（覆盖不算新增）");

        System.out.println("\n--- 桶分布（看冲突）---");
        map.printBuckets();

        System.out.println("\n--- 故意制造冲突：容量 16，只放 8 个 key，看哪些撞在一个桶 ---");
        MyHashMap<Integer, String> m2 = new MyHashMap<>();
        for (int i = 0; i < 8; i++) {
            m2.put(i, "v" + i);
        }
        m2.printBuckets();
    }
}
