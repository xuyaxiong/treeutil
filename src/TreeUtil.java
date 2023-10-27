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
    static class InnerNode {
        String key;
        InnerNode left, right;
        int midPos;
        boolean isVirtual; // 是否为虚拟节点

        public InnerNode(String key, boolean isVirtual) {
            this.key = key;
            this.isVirtual = isVirtual;
        }

        @Override
        public String toString() {
            return this.key;
        }
    }

    // 键的长度
    private static final int KEY_LEN = 3;

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
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(c).repeat(Math.max(0, n)));
        return sb.toString();
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

    public static List<InnerNode> treeToList(BST<String, Integer> tree) {
        int sizeOfReal = tree.size();
        int currCount = 0;
        Queue<Node> q = new LinkedList<>();
        ArrayList<InnerNode> nodes = new ArrayList<>();
        InnerNode root = new InnerNode(tree.root.key, false);
        nodes.add(root);
        currCount++;
        q.offer(tree.root);
        while (!q.isEmpty()) {
            Node t = q.poll();
            InnerNode tmp;
            if (t.left == null) {
                tmp = new InnerNode("", true);
                nodes.add(tmp);
                q.offer(new Node("", -1, 0));
            } else {
                tmp = new InnerNode((String) t.left.key, false);
                nodes.add(tmp);
                q.offer(t.left);
                currCount++;
                if (currCount == sizeOfReal) break;
            }
            if (t.right == null) {
                tmp = new InnerNode("", true);
                nodes.add(tmp);
                q.offer(new Node("", -1, 0));
            } else {
                tmp = new InnerNode((String) t.right.key, false);
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
            if (!node.isVirtual)
                System.out.print(formatKey(node.key));
            else System.out.print(formatVirtualKey());
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
                    if (!list.get(childPos).isVirtual) {
                        if (j % 2 == 0) {
                            System.out.print("/");
                        } else {
                            System.out.print("\\");
                        }
                    } else {
                        System.out.print(" ");
                    }
                    lastPos = pos;
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        List<InnerNode> list = getExampleTree(50);
        printTree(list);
    }
}
