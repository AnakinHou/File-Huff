/**
 * @author 86181
 */
public class Header {
    
    /**
     * 加密文件标识符
     */
    public String key;
    
    /**
     * 源文件后缀
     */
    private String suffix;
    
    /**
     * 字典字符个数
     */
    private String dicSize;
    
    /**
     * 哈夫曼编码最大长度
     */
    private String huffCodeMaxLen;
    
    /**
     * 加密图片总字节数
     */
    private String originFileLen;
    
    /**
     * 字典
     */
    private String dic;
    
    public Header() {
        this.key = "DeepDark Fantasy";
    }
    
    public String getKey() {
        return key;
    }
    
    public String getOriginFileLen() {
        return originFileLen;
    }
    
    public void setOriginFileLen(String originFileLen) {
        this.originFileLen = originFileLen;
    }
    
    public String getDicSize() {
        return dicSize;
    }
    
    public void setDicSize(String dicSize) {
        this.dicSize = dicSize;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    public String getHuffCodeMaxLen() {
        return huffCodeMaxLen;
    }
    
    public void setHuffCodeMaxLen(String huffCodeMaxLen) {
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
                "key='" + key + '\'' +
                ", suffix='" + suffix + '\'' +
                ", dicSize='" + dicSize + '\'' +
                ", huffCodeMaxLen='" + huffCodeMaxLen + '\'' +
                ", originFileLen='" + originFileLen + '\'' +
                ", dic='" + dic + '\'' +
                '}';
    }
    
    /**
     * 获得文件头字串
     *
     * @return 文件头字串
     */
    public static String getHeaderContent() {
        Header header = Compress.header;
        return header.getKey() +
                header.getSuffix() +
                header.getDicSize() +
                header.getHuffCodeMaxLen() +
                header.getDic() +
                header.getOriginFileLen();
    }
    
}
