import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 控制台打印树形结构
 * 徐亚雄
 * 2023.10.27
 */
public class TreeUtil {
    // 键的长度
    private static final int KEY_LEN = 3;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    static class InnerNode {
        String key;
        String val;
        boolean color;
        InnerNode left, right;
        int midPos;
        boolean isVirtual; // 是否为虚拟节点

        public InnerNode(String key, boolean isVirtual) {
            this.key = key;
            this.isVirtual = isVirtual;
        }

        public InnerNode(String key, boolean isVirtual, boolean color) {
            this(key, isVirtual);
            this.color = color;
        }

        public boolean isRed() {
            return color == RED;
        }
    }

    // 获取树的高度
    private static int getTreeHeight(int size) {
        return (int) Math.ceil(Math.log(size + 1) / Math.log(2));
    }

    // 获取行首到最左边的距离
    private static int getLeftOffset(int h, int l) {
        if (h == l) return 0;
        return (int) Math.pow(2, h - l - 1) * (KEY_LEN + 1) - (KEY_LEN + 1) / 2;
    }

    // 获取层级，从1开始
    private static int getLevel(int i) {
        return (int) Math.floor(Math.log(i + 1) / Math.log(2) + 1);
    }

    private static String repeatChar(int n, char c) {
        return String.valueOf(c).repeat(Math.max(0, n));
    }

    private static String formatKey(String key) {
        String s = repeatChar(KEY_LEN, '0') + key;
        return s.substring(s.length() - KEY_LEN);
    }

    private static String formatVirtualKey() {
        return repeatChar(KEY_LEN, ' ');
    }

    // 判断该位置是否为行首
    private static boolean isFirst(int i) {
        int l = getLevel(i);
        return i == Math.pow(2, l - 1) - 1;
    }

    // 判断该位置是否为行尾
    private static boolean isLast(int i) {
        int l = getLevel(i);
        return i == Math.pow(2, l) - 2;
    }

    // 获取某层级两节点之间的间隔长度
    private static int getGap(int h, int i) {
        int l = getLevel(i);
        return getGapFromLevel(h, l);
    }

    private static int getGapFromLevel(int h, int l) {
        if (l == h) return 1;
        return (int) (Math.pow(2, h - l) * (KEY_LEN + 1) - KEY_LEN);
    }

    // 通过索引计算出某节点是所在列的第几个元素
    private static int getRankOfLevel(int i) {
        int l = getLevel(i);
        int first = (int) (Math.pow(2, l - 1) - 1);
        return i - first + 1;
    }

    private static int getPosFromRank(int level, int rank, List<InnerNode> nodes) {
        int childPos = (int) (Math.pow(2, level) - 1 + rank);
        if (childPos >= nodes.size()) return -1;
        int parentPos;
        parentPos = rank % 2 == 0 ? (childPos - 1) / 2 : (childPos - 2) / 2;
        InnerNode parentNode = nodes.get(parentPos);
        InnerNode childNode = nodes.get(childPos);
        return (parentNode.midPos + childNode.midPos) / 2;
    }

    private static int getMidPos(int height, int index) {
        int level = getLevel(index);
        int leftDis = getLeftOffset(height, level);
        int rank = getRankOfLevel(index);
        return leftDis + (KEY_LEN + getGap(height, index)) * (rank - 1) + (KEY_LEN + 1) / 2;
    }

    public static List<InnerNode> getExampleTree(int N) {
        ArrayList<InnerNode> nodes = new ArrayList<>();
        int height = getTreeHeight(N);
        for (int i = 0; i < N; ++i) {
            InnerNode node = new InnerNode(i + "", false);
            node.midPos = getMidPos(height, i);
            if (i % 3 == 1) node.color = RED;
            nodes.add(node);
        }
        for (int i = 0; i < N; ++i) {
            InnerNode node = nodes.get(i);
            int indexOfLeftChild = 2 * i + 1;
            int indexOfRightChild = indexOfLeftChild + 1;
            if (indexOfLeftChild < nodes.size()) node.left = nodes.get(indexOfLeftChild);
            if (indexOfRightChild < nodes.size()) node.right = nodes.get(indexOfRightChild);
        }
        return nodes;
    }

    public static List<InnerNode> treeToList(IST<String, String> tree) {
        ArrayList<InnerNode> nodes = new ArrayList<>();
        if (tree.getRoot() == null) return nodes;
        int sizeOfReal = tree.size();
        int currCount = 0;
        Queue<Node<String, String>> q = new LinkedList<>();
        InnerNode tmp;
        tmp = new InnerNode(tree.getRoot().key, false, false);
        nodes.add(tmp);
        currCount++;
        if (currCount == sizeOfReal) return nodes;
        q.offer(tree.getRoot());
        while (!q.isEmpty()) {
            Node<String, String> t = q.poll();
            if (t.left == null) {
                tmp = new InnerNode("", true);
                nodes.add(tmp);
                q.offer(new Node<>("", "", 0));
            } else {
                tmp = new InnerNode(t.left.key, false, t.left.color);
                nodes.add(tmp);
                q.offer(t.left);
                currCount++;
                if (currCount == sizeOfReal) break;
            }
            if (t.right == null) {
                tmp = new InnerNode("", true);
                nodes.add(tmp);
                q.offer(new Node<>("", "", 0));
            } else {
                tmp = new InnerNode(t.right.key, false, t.right.color);
                nodes.add(tmp);
                q.offer(t.right);
                currCount++;
                if (currCount == sizeOfReal) break;
            }
        }
        int height = getTreeHeight(nodes.size());
        for (int i = 0; i < nodes.size(); ++i) {
            InnerNode node = nodes.get(i);
            node.midPos = getMidPos(height, i);
            int indexOfLeftChild = 2 * i + 1;
            int indexOfRightChild = indexOfLeftChild + 1;
            if (indexOfLeftChild < nodes.size()) node.left = nodes.get(indexOfLeftChild);
            if (indexOfRightChild < nodes.size()) node.right = nodes.get(indexOfRightChild);
        }
        return nodes;
    }

    public static void printTree(List<InnerNode> list) {
        int h = getTreeHeight(list.size());
        for (int i = 0; i < list.size(); ++i) {
            InnerNode node = list.get(i);
            int level = getLevel(i);
            if (isFirst(i)) {
                int leftDis = getLeftOffset(h, level);
                System.out.print(repeatChar(leftDis, ' '));
            } else {
                int gap = getGapFromLevel(h, level);
                System.out.print(repeatChar(gap, ' '));
            }
            if (!node.isVirtual) {
                String key = formatKey(node.key);
//                if (node.isRed()) key = toRedStr(key);
                System.out.print(key);
            } else System.out.print(formatVirtualKey());
            if (isLast(i)) {
                System.out.println();
                // 打印连接线
                if (level == h) continue;
                int lastPos = 0;
                int total = (int) Math.pow(2, level);
                for (int j = 0; j < total; ++j) {
                    int pos = getPosFromRank(level, j, list);
                    if (pos == -1) break;
                    System.out.print(repeatChar(pos - lastPos - 1, ' '));
                    int childPos = (int) (Math.pow(2, level) - 1 + j);
                    InnerNode childNode = list.get(childPos);
                    if (!childNode.isVirtual) {
                        String s;
                        if (j % 2 == 0) {
                            s = "/";
                        } else {
                            s = "\\";
                        }
                        if (childNode.isRed()) s = toRedStr(s);
                        System.out.print(s);
                    } else {
                        System.out.print(" ");
                    }
                    lastPos = pos;
                }
                System.out.println();
            }
        }
    }

    public static void printTree(IST<String, String> tree) {
        List<InnerNode> nodes = treeToList(tree);
        printTree(nodes);
    }

    private static String toRedStr(String s) {
        String redColor = "\u001B[31m";
        String resetColor = "\u001B[0m"; // 重置颜色为默认颜色
        return redColor + s + resetColor;
    }

    public static void main(String[] args) {
        List<InnerNode> list = getExampleTree(1);
        printTree(list);
    }
}
