package top.lucency.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 86181
 */
public class DecoderUtil {
    
    private static Map<Integer, Integer> map = new HashMap<>() {
        {
            for (int i = 0; i < 16; i++) {
                String binary = Integer.toBinaryString(i);
                int Int = Integer.parseInt(binary);
                put(Int & 0xFF, Int);
            }
        }
    };
    
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
                binaries.add(map.get(rgb & 0xFF));
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
        for (int i = 0; i < binaries.size() - 1; i += 2) {
            int b = (binaries.get(i) * 10000) + binaries.get(i + 1);
            bytes[i / 2] = (byte) Integer.parseInt(b + "", 2);
        }
        return bytes;
    }
    
    /**
     * 写入文件
     *
     * @param bytes byte 数组
     * @param path  写入的文件路径
     */
    private static void writeFile(byte[] bytes, String path) {
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
