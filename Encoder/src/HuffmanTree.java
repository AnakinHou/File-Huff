import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 86181
 */
public class HuffmanTree {
    
    static StringBuilder stringBuilder = new StringBuilder();
    
    static Map<Integer, String> huffmanCodes = new HashMap<>();
    
    /**
     * 创建 Huffman 二叉树
     *
     * @param nodes 节点列表
     * @return 二叉树
     */
    public static HuffmanNode createHuffmanTree(List<HuffmanNode> nodes) {
        while (nodes.size() > 1) {
            // 根据权值升序排序
            nodes.sort(Comparator.comparingInt(o -> o.weight));
            
            // 取出前两个节点合并为一棵无数据的树
            HuffmanNode leftNode = nodes.get(0);
            HuffmanNode rightNode = nodes.get(1);
            HuffmanNode parent = new HuffmanNode(-1, leftNode.weight + rightNode.weight);
            parent.leftNode = leftNode;
            parent.rightNode = rightNode;
            
            // 删除前两个节点，添加无数据的父节点
            nodes.remove(leftNode);
            nodes.remove(rightNode);
            nodes.add(parent);
        }
        // 返回根节点
        return nodes.get(0);
    }
    
    /**
     * 获得 Huffman 编码
     *
     * @param root 根节点
     */
    public static void getHuffmanCode(HuffmanNode root) {
        analysisBranch(root.leftNode, "0", stringBuilder);
        analysisBranch(root.rightNode, "1", stringBuilder);
    }
    
    private static void analysisBranch(HuffmanNode node, String code, StringBuilder builder) {
        StringBuilder tmpBuilder = new StringBuilder(builder);
        tmpBuilder.append(code);
        if (node != null) {
            // 存在子节点
            if (node.b == -1) {
                analysisBranch(node.leftNode, "0", tmpBuilder);
                analysisBranch(node.rightNode, "1", tmpBuilder);
            } else {
                // 叶子节点
                huffmanCodes.put(node.b, tmpBuilder.toString());
            }
        }
    }
}
