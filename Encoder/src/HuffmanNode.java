/**
 * @author 86181
 */
class HuffmanNode {
    
    /**
     * 字节的二进制整型形式，由于字节无论如何表示也无法作为根节点的标识，因此放弃性能优化
     */
    int b;
    
    int weight;
    
    HuffmanNode leftNode;
    
    HuffmanNode rightNode;
    
    public HuffmanNode(int b, int weight) {
        this.b = b;
        this.weight = weight;
    }
    
    @Override
    public String toString() {
        return "HuffmanNode{" +
                "byte = " + b +
                ", weight = " + weight +
                '}';
    }
    
    /**
     * 前序遍历
     */
    public void preOrder() {
        if (this.b != -1){
            System.out.println(this);
        }
        if (this.leftNode != null) {
            this.leftNode.preOrder();
        }
        if (this.rightNode != null) {
            this.rightNode.preOrder();
        }
    }
    
}
