package top.lucency.utils;

import top.lucency.entities.Header;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * @author 86181
 */
public class DecoderUtil {
    
    /**
     * 文件头
     */
    public static Header header = new Header();
    
    /**
     * 哈夫曼编码字典，哈夫曼编码对应原码
     */
    public static Map<String, Integer> huffmanCodes = new HashMap<>(1);
    
    /**
     * 像素 rgb 对应的原字节
     */
    public static Map<String, Byte> kvs = new HashMap<>() {
        {
            for (int i = 0; i < 256; i++) {
                int originByte = Integer.parseInt(Integer.toBinaryString(i));
                int red = (originByte >> 16) & 0xff;
                int green = (originByte >> 8) & 0xff;
                int blue = originByte & 0xff;
                int[] rgb = {red, green, blue};
                put(Arrays.toString(rgb), binary2Byte(originByte));
            }
        }
    };
    
    /**
     * 获得文件头
     *
     * @param path 文件路径
     */
    public static boolean getHeader(String path) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path)))) {
            byte[] bytes = bis.readAllBytes();
            
            byte[] tmp1 = new byte[16];
            System.arraycopy(bytes, 0, tmp1, 0, 16);
            if (!"DeepDark Fantasy".equals(new String(tmp1, "GBK"))) {
                return false;
            }
            
            byte[] tmp2 = new byte[10];
            System.arraycopy(bytes, 16, tmp2, 0, 10);
            header.setSuffix(new String(tmp2, "GBK").strip());
            
            byte[] tmp3 = new byte[4];
            System.arraycopy(bytes, 26, tmp3, 0, 4);
            int dicSize = Integer.parseInt(new String(tmp3, "GBK"));
            header.setDicSize(dicSize);
            
            byte[] tmp4 = new byte[2];
            System.arraycopy(bytes, 30, tmp4, 0, 2);
            int huffCodeMaxLen = Integer.parseInt(new String(tmp4, "GBK"));
            header.setHuffCodeMaxLen(huffCodeMaxLen);
            
            byte[] tmp5 = new byte[dicSize];
            System.arraycopy(bytes, 32, tmp5, 0, dicSize);
            header.setDic(new String(tmp5, "GBK"));
            
            byte[] tmp6 = new byte[10];
            System.arraycopy(bytes, 32 + dicSize, tmp6, 0, 10);
            header.setOriginFileLen(Integer.parseInt(new String(tmp6, "GBK")));
            
            header.setHeaderSize(42 + dicSize);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(header);
        return true;
    }
    
    /**
     * 解析出字典
     *
     * @param huffLen 哈夫曼编码最长长度
     * @param binary  二进制字串
     */
    public static void getHuffmanCodes(int huffLen, String binary) {
        huffmanCodes.clear();
        // 每隔 8 + huffLen 个长度截取字串，截取的字串分割为 8，huffLen 两部分长度，由于长度不大且处理较复杂，不做性能考虑
        int period = 8 + huffLen;
        for (int i = 0; i < binary.length(); i += period) {
            String kv = binary.substring(i, i + period);
            int origin = Integer.parseInt(kv.substring(0, 8));
            huffmanCodes.put(kv.substring(8).strip(), origin);
        }
        System.out.println("哈夫曼编码为: " + huffmanCodes);
    }
    
    /**
     * 解压缩获得原图片
     *
     * @param huffPath huff 文件路径
     */
    public static byte[] getOriginPic(String huffPath) {
        long start = System.currentTimeMillis();
        System.out.println("正在解压文件");
        byte[] out = new byte[header.getOriginFileLen()];
        // 跳过文件头，读取加密部分
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(huffPath)))) {
            // 缓存每个字节二进制字串
            StringBuilder tmpBinary = new StringBuilder();
            // 缓存未被字典识别的二进制字串
            StringBuilder tmpUnAccBinary = new StringBuilder();
            byte[] bytes = bis.readAllBytes();
            // 文件有效字节计数
            int count = 0;
            for (int i = header.getHeaderSize(); i < bytes.length; i++) {
                // 前端补零到八位
                String intBinary = String.valueOf(byte2Binary(bytes[i]));
                tmpBinary.append("0".repeat(8 - intBinary.length())).append(intBinary);
                // tmpBinary 逐个字符取出添加进 tmpUnAccBinary 判断是否被字典识别，识别则缓存中去除相应字串
                for (char c : tmpBinary.toString().toCharArray()) {
                    String tmp0 = tmpBinary.toString().replaceFirst(String.valueOf(c), "");
                    tmpBinary.setLength(0);
                    tmpBinary.append(tmp0);
                    tmpUnAccBinary.append(c);
                    if (huffmanCodes.get(tmpUnAccBinary.toString()) != null) {
                        // 只读取原文件有效字节
                        if (count > header.getOriginFileLen() - 1) {
                            System.out.println("文件解压完成");
                            System.out.println("解压缩耗时：" + (System.currentTimeMillis() - start));
                            return out;
                        }
                        out[count] = binary2Byte(huffmanCodes.get(tmpUnAccBinary.toString()));
                        tmpUnAccBinary.setLength(0);
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
    
    /**
     * 解码
     *
     * @param bytes   加密图片字节数组
     * @param outPath 输出路径
     */
    public static void decode(byte[] bytes, String outPath) {
        long start = System.currentTimeMillis();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(outPath)))) {
            BufferedImage bi = ImageIO.read(bis);
            int size = bi.getWidth();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    // 逐行获取像素 rgb
                    Object rgb = bi.getRaster().getDataElements(j, i, null);
                    int r = bi.getColorModel().getRed(rgb);
                    int g = bi.getColorModel().getGreen(rgb);
                    int b = bi.getColorModel().getBlue(rgb);
                    bos.write(kvs.get(Arrays.toString(new int[]{r, g, b})));
                }
            }
            bi.getGraphics().dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解密像素耗时：" + (System.currentTimeMillis() - start));
    }
    
    /**
     * 二进制整型转字节
     *
     * @param binary 二进制整形
     * @return 字节
     */
    public static byte binary2Byte(int binary) {
        return (byte) Integer.parseInt(binary + "", 2);
    }
    
    /**
     * 将 byte 转为二进制形式 int
     *
     * @return 二进制形式 int
     */
    public static int byte2Binary(byte b) {
        return Integer.parseInt(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1));
    }
    
    public static void start(String huffPath, String outPath) {
        long start = System.currentTimeMillis();
        getHuffmanCodes(header.getHuffCodeMaxLen(), header.getDic());
        System.out.println("正在解密像素");
        decode(getOriginPic(huffPath), outPath);
        System.out.println("解密完成！");
        System.out.println("总耗时：" + (System.currentTimeMillis() - start));
    }
    
}
