import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author 86181
 */
public class Encoder {
    
    public static final String ORIGIN_SOUND_PATH = "src/0.aac";
    
    public static final String PIC_PATH = "C:\\Users\\86181\\Desktop\\out.png";
    
    static BufferedImage bi;
    
    static int size;
    
    public static void main(String[] args) {
        System.out.println("正在处理音频生成图像……");
        size = getPicSize(ORIGIN_SOUND_PATH);
        createPic(size);
        getBi();
        fillBi(ORIGIN_SOUND_PATH);
        drawPic();
        System.out.println("图像生成完毕！");
    }
    
    /**
     * 创建默认图片
     *
     * @param size 图片大小
     */
    public static void createPic(int size) {
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        try {
            ImageIO.write(bufferedImage, "png", new File(PIC_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获得文件字节总长度
     *
     * @param filePath 文件路径
     * @return 字节总长度
     */
    public static int getPicSize(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            return (int) Math.ceil(Math.sqrt(fis.readAllBytes().length * 2));
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
            bi = ImageIO.read(new File(PIC_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 填充 bi 指定位置颜色
     *
     * @param path 文件路径
     */
    public static void fillBi(String path) {
        try (FileInputStream fis = new FileInputStream(new File(path))) {
            byte[] tmpB = new byte[1024];
            // 像素计数
            int count = 0;
            while (fis.read(tmpB) != -1) {
                // 生成每个字节的像素
                for (byte b : tmpB) {
                    String binary = binary(b);
                    // 半个字节作为一个像素，因此一个字节占横向两个像素
                    int left = Integer.parseInt(binary.substring(0, 4));
                    int right = Integer.parseInt(binary.substring(4, 8));
                    
                    // 构建一个字节的两个像素 rgb
                    int r = 0;
                    int leftG = (left & 0xFF00) >> 3;
                    int leftB = left & 0xFF;
                    int leftRgb = new Color(r, leftG, leftB).getRGB();
                    
                    int rightG = (right & 0xFF00) >> 3;
                    int rightB = right & 0xFF;
                    int rightRgb = new Color(r, rightG, rightB).getRGB();
                    
                    // 获得一个字节的两个像素坐标
                    int leftX = count % size;
                    int leftY = count / size;
                    
                    int rightX = leftX + 1;
                    int rightY = leftY;
                    
                    // 像素 X 轴越界
                    if (rightX > size - 1) {
                        rightX = 0;
                        rightY += 1;
                    }
                    if (leftX > size - 1) {
                        leftX = 0;
                        leftY += 1;
                    }
                    
                    bi.setRGB(leftX, leftY, leftRgb);
                    bi.setRGB(rightX, rightY, rightRgb);
                    count += 2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 绘图
     */
    public static void drawPic() {
        try {
            ImageIO.write(bi, "png", new File(PIC_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 将 byte 转为二进制字串
     *
     * @return 转换后的字符串
     */
    public static String binary(byte b) {
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }
    
}
