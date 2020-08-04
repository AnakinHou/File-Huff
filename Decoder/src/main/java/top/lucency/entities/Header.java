package top.lucency.entities;

/**
 * @author 86181
 */
public class Header {
    
    /**
     * 文件头字节数
     */
    private int headerSize;
    
    /**
     * 加密文件标识符
     */
    private String key;
    
    /**
     * 源文件后缀
     */
    private String suffix;
    
    /**
     * 字典字符个数
     */
    private int dicSize;
    
    /**
     * 哈夫曼编码最大长度
     */
    private int huffCodeMaxLen;
    
    /**
     * 加密文件总字节数
     */
    private int originFileLen;
    
    /**
     * 字典
     */
    private String dic;
    
    public int getOriginFileLen() {
        return originFileLen;
    }
    
    public void setOriginFileLen(int originFileLen) {
        this.originFileLen = originFileLen;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public int getHeaderSize() {
        return headerSize;
    }
    
    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    public int getDicSize() {
        return dicSize;
    }
    
    public void setDicSize(int dicSize) {
        this.dicSize = dicSize;
    }
    
    public int getHuffCodeMaxLen() {
        return huffCodeMaxLen;
    }
    
    public void setHuffCodeMaxLen(int huffCodeMaxLen) {
        this.huffCodeMaxLen = huffCodeMaxLen;
    }
    
    public String getDic() {
        return dic;
    }
    
    public void setDic(String dic) {
        this.dic = dic;
    }
    
    @Override
    public String toString() {
        return "Header{" +
                "headerSize=" + headerSize +
                ", key='" + key + '\'' +
                ", suffix='" + suffix + '\'' +
                ", dicSize=" + dicSize +
                ", huffCodeMaxLen=" + huffCodeMaxLen +
                ", originFileLen=" + originFileLen +
                ", dic='" + dic + '\'' +
                '}';
    }
}
