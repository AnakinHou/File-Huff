import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

/**
 * @author 86181
 */
public class Encoder {
    
    /**
     * 加密原文件路径
     */
    public static final String ORIGIN_FILE_PATH = "src/0.ncm";
    
    /**
     * 画笔
     */
    static BufferedImage bi;
    
    static long drawStart = System.currentTimeMillis();
    
    /**
     * 文件总字节长度
     */
    static int size;
    
    public static void main(String[] args) {
        System.out.println("正在加密文件");
        size = getPicInfo(new File(ORIGIN_FILE_PATH));
        getBi();
        analysisRgb();
        Compress.compress(Compress.transferBytes(Objects.requireNonNull(getPicBytes())));
        System.out.println("文件生成完毕！");
    }
    
    /**
     * 获得文件字节总长度以及后缀名
     *
     * @return 字节总长度
     */
    public static int getPicInfo(File pic) {
        try (FileInputStream fis = new FileInputStream(pic)) {
            String name = pic.getName();
            String suffix = name.substring(name.lastIndexOf(".") + 1);
            if (suffix.length() > 10) {
                System.out.println("文件后缀超过十个字符，不符合加密条件！");
                System.exit(0);
            } else {
                suffix = " ".repeat(10 - suffix.length()) + suffix;
            }
            Compress.header.setSuffix(suffix);
            return (int) Math.ceil(Math.sqrt(fis.readAllBytes().length));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * 获得 bi
     */
    public static void getBi() {
        try {
            bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 分析 rgb 填充进画笔
     */
    public static void analysisRgb() {
        try (FileInputStream fis = new FileInputStream(new File(ORIGIN_FILE_PATH))) {
            byte[] tmpB = new byte[1024];
            // 字节计数
            int counts = 0;
            while (fis.read(tmpB) != -1) {
                for (byte b : tmpB) {
                    int binary = Integer.parseInt(byte2Binary(b));
                    int red = (binary >> 16) & 0xff;
                    int green = (binary >> 8) & 0xff;
                    int blue = binary & 0xff;
                    setBi(counts % size, counts / size, new Color(red, green, blue).getRGB());
                    counts++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 设置画笔
     *
     * @param x   x
     * @param y   y
     * @param rgb rgb
     */
    public static void setBi(int x, int y, int rgb) {
        if (x > size - 1) {
            x = 0;
            y++;
        }
        if (y > size - 1) {
            return;
        }
        bi.setRGB(x, y, rgb);
    }
    
    /**
     * 绘图
     */
    public static byte[] getPicBytes() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", bos);
            System.out.println("加密耗时：" + (System.currentTimeMillis() - drawStart));
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将 byte 转为二进制字串
     *
     * @return 转换后的字符串
     */
    public static String byte2Binary(byte b) {
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }
    
}
