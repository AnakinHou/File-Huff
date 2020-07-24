package top.lucency.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author 86181
 */
public class DecoderUtil {
    
    /**
     * 分析 RGB 获得对应的半个字节编码
     *
     * @param file 原始文件
     */
    private static ArrayList<Integer> analysisRgb(String file) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ArrayList<Integer> binaries = new ArrayList<>();
        int size = Objects.requireNonNull(bi).getWidth();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // 逐行获取像素 rgb
                int rgb = bi.getRGB(j, i);
                int b = (rgb & 0xFF);
                binaries.add(getBinaryByBlue(b));
            }
        }
        return binaries;
    }
    
    /**
     * 获得 byte 数组
     *
     * @return byte 数组
     */
    private static byte[] getBytes(ArrayList<Integer> binaries) {
        byte[] bytes = new byte[binaries.size() / 2];
        // byte[] 下标计数
        int index = 0;
        for (int i = 0; i < binaries.size() - 1; i += 2) {
            String left = String.format("%04d", binaries.get(i));
            String right = String.format("%04d", binaries.get(i + 1));
            String b = left + right;
            bytes[index] = binary2Byte(b);
            index++;
        }
        return bytes;
    }
    
    /**
     * 通过 blue 分量反推出原二进制（其实是十进制）
     *
     * @param blue 蓝色分量
     * @return 十进制
     */
    private static int getBinaryByBlue(int blue) {
        int[] binaries = {0, 1, 10, 11, 100, 101, 110, 111, 1000, 1001, 1010, 1011, 1100, 1101, 1110, 1111};
        for (int binary : binaries) {
            if ((binary & 0xFF) == blue) {
                return binary;
            }
        }
        return 0;
    }
    
    /**
     * 二进制字串转字节
     *
     * @param binary 二进制字串
     * @return 字节
     */
    private static byte binary2Byte(String binary) {
        byte b = 0;
        for (int i = binary.length() - 1, j = 0; i >= 0; i--, j++) {
            b += (Byte.parseByte(binary.charAt(i) + "") * Math.pow(2, j));
        }
        return b;
    }
    
    /**
     * 写入文件
     *
     * @param bytes byte 数组
     * @param path  写入的文件路径
     */
    public static void writeFile(byte[] bytes, String path) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void start(String picPath, String outPath) {
        writeFile(getBytes(analysisRgb(picPath)), outPath);
    }
    
}
