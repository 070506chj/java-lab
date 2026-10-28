import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class CollectionLab {

    static int arrayListCapacity(ArrayList<?> list) throws Exception {
        Field f = ArrayList.class.getDeclaredField("elementData");
        f.setAccessible(true);
        return ((Object[]) f.get(list)).length;
    }

    static int hashMapTableLength(HashMap<?, ?> map) throws Exception {
        Field f = HashMap.class.getDeclaredField("table");
        f.setAccessible(true);
        Object[] table = (Object[]) f.get(map);
        return table == null ? 0 : table.length;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("========== ArrayList 扩容 ==========");
        ArrayList<Integer> list = new ArrayList<>();
        int prevCap = arrayListCapacity(list);
        System.out.println("刚 new 出来时，容量 = " + prevCap + "（懒加载，还没分配数组）");
        for (int i = 1; i <= 34; i++) {
            list.add(i);
            int cap = arrayListCapacity(list);
            if (cap != prevCap) {
                System.out.printf("add 第 %2d 个元素 -> 容量 %d%n", i, cap);
                prevCap = cap;
            }
        }

        System.out.println();
        System.out.println("========== HashMap 扩容 ==========");
        HashMap<String, Integer> map = new HashMap<>();
        System.out.println("刚 new 出来时，table 长度 = " + hashMapTableLength(map) + "（懒加载）");
        for (int i = 1; i <= 20; i++) {
            map.put("key" + i, i);
            System.out.printf("put 第 %2d 个元素 -> size=%2d, table 长度=%2d%n",
                    i, map.size(), hashMapTableLength(map));
        }

        System.out.println();
        System.out.println("========== hash 扰动 + 下标计算 ==========");
        String[] keys = {"key1", "abc", "hello", "GitHub"};
        for (String k : keys) {
            int h = k.hashCode();
            int spread = h ^ (h >>> 16);
            System.out.printf("key=%-8s hashCode=%12d  扰动后=%12d  下标(16槽)=%2d%n",
                    k, h, spread, spread & 15);
        }
    }
}