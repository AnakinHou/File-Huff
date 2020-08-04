import java.io.*;
import java.util.*;

/**
 * @author 86181
 */
public class Compress {
    
    /**
     * 加密并压缩过的文件输出路径
     */
    public static final String PIC_OUT_PATH = "C:\\Users\\86181\\Desktop\\out.huf";
    
    /**
     * 文件头
     */
    public static Header header = new Header();
    
    static long comStart = System.currentTimeMillis();
    
    /**
     * 写入压缩文件
     *
     * @param bytes 原数组
     */
    public static void compress(byte[] bytes) {
        // 加密图片过大，解压缩过程中将出错，禁止压缩并退出程序
        if (bytes.length >= 2147483647) {
            System.out.println("非常抱歉，加密图片过大！");
        } else {
            String originFileLen = String.valueOf(bytes.length);
            header.setOriginFileLen("0".repeat(10 - originFileLen.length()) + originFileLen);
        }
        System.out.println("正在压缩文件");
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(PIC_OUT_PATH)))) {
            System.out.println("正在添加文件头");
            bos.write(Header.getHeaderContent().getBytes());
            System.out.println(header);
            System.out.println("文件头添加完毕");
            // 不定长缓存区，规定长度为 8 的倍数
            StringBuilder tmpSb = new StringBuilder();
            for (byte b : bytes) {
                // 添加每个字节对应的哈夫曼编码
                tmpSb.append(HuffmanTree.huffmanCodes.get(byte2Binary(b)));
                // 输出缓存并清空
                if (tmpSb.length() % 8 == 0) {
                    bos.write(stringBinaries2Bytes(tmpSb.toString()));
                    tmpSb.setLength(0);
                }
            }
            // 求出不足八的倍数的位数并补零
            tmpSb.append("0".repeat(Math.max(0, 8 - tmpSb.length() / 8)));
            bos.write(stringBinaries2Bytes(tmpSb.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("文件压缩完毕");
        System.out.println("压缩耗时：" + (System.currentTimeMillis() - comStart));
    }
    
    /**
     * 字符串二进制形式转字节数组
     *
     * @param binaries 二进制字串
     * @return 字节数组
     */
    public static byte[] stringBinaries2Bytes(String binaries) {
        byte[] bytes = binaries.getBytes();
        ArrayList<Byte> tmpBytes = new ArrayList<>();
        int len = binaries.length() >> 3;
        for (int i = 0; i < len; i++) {
            int b = 0;
            for (int j = 0; j < 8; j++) {
                if ((bytes[(i << 3) + j] & 1) == 0) {
                    continue;
                }
                b = b | (1 << (7 - j));
            }
            tmpBytes.add((byte) b);
        }
        byte[] outBytes = new byte[tmpBytes.size()];
        for (int i = 0; i < tmpBytes.size(); i++) {
            outBytes[i] = tmpBytes.get(i);
        }
        return outBytes;
    }
    
    /**
     * 转化原字节为 Huffman 编码后的字节数组
     */
    public static byte[] transferBytes(byte[] bytes) {
        Map<Integer, Integer> byteWeights = new LinkedHashMap<>();
        // 计算原字节每个字节出现次数
        for (byte b : bytes) {
            addTimes(byteWeights, byte2Binary(b));
        }
        
        // 生成哈夫曼编码并给字典赋值
        List<HuffmanNode> nodes = new ArrayList<>();
        for (Integer key : byteWeights.keySet()) {
            nodes.add(new HuffmanNode(key, byteWeights.get(key)));
        }
        byteWeights.clear();
        HuffmanNode root = HuffmanTree.createHuffmanTree(nodes);
        HuffmanTree.getHuffmanCode(root);
        Map<Integer, String> huffmanCodes = HuffmanTree.huffmanCodes;
        StringBuilder stringBuilder = new StringBuilder();
        // 获得哈夫曼编码最长长度
        int huffCodeMaxLen = 0;
        for (int key : huffmanCodes.keySet()) {
            int codeLen = String.valueOf(huffmanCodes.get(key)).length();
            if (codeLen > huffCodeMaxLen) {
                huffCodeMaxLen = codeLen;
            }
        }
        header.setHuffCodeMaxLen(String.format("%02d", huffCodeMaxLen));
        // 不足位前端补零或空格给解压缩使用，确保有固定的长度可以分割
        for (int key : huffmanCodes.keySet()) {
            String tmp = huffmanCodes.get(key);
            String huffmanCode = " ".repeat(huffCodeMaxLen - tmp.length()) + tmp;
            stringBuilder.append(String.format("%08d", key)).append(huffmanCode);
        }
        
        // 填充字典信息
        header.setDic(stringBuilder.toString());
        header.setDicSize(String.format("%04d", huffmanCodes.size() * (8 + huffCodeMaxLen)));
        System.out.println("哈夫曼编码为: " + huffmanCodes);
        
        return bytes;
    }
    
    /**
     * 增加一个字节出现的次数
     *
     * @param b 字节
     */
    public static void addTimes(Map<Integer, Integer> byteWeights, int b) {
        byteWeights.merge(b, 1, Integer::sum);
    }
    
    /**
     * 将 byte 转为二进制形式 int
     *
     * @return 二进制形式 int
     */
    public static int byte2Binary(byte b) {
        return Integer.parseInt(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1));
    }
    
}
