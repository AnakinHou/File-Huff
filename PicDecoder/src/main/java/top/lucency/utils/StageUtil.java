package top.lucency.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author 86181
 */
public class StageUtil {
    
    /**
     * 包括任务栏的电脑屏幕对象
     */
    public static Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    
    /**
     * 任务栏 logo 图标路径
     */
    public static final String LOGO_DIR = "statics/logo/logo.png";
    
    /**
     * 任务栏图标
     */
    public static Image image = new Image(LOGO_DIR, 100, 100, true, false);
    
    /**
     * 自定义窗体
     *
     * @param stage         窗体
     * @param root          根节点
     * @param isAlwaysOnTop 是否总是置顶
     * @param width         窗体宽度
     * @param height        窗体高度
     */
    public static void diyStage(Stage stage, Parent root, boolean isAlwaysOnTop, double width, double height) {
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);
        
        stage.getIcons().add(image);
        stage.setAlwaysOnTop(isAlwaysOnTop);
        
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
    }
    
}
