package top.lucency.utils;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author 86181
 */
public class AnimationUtil {
    
    /**
     * 位移动画
     *
     * @param node       节点
     * @param cycleCount 持续次数
     * @param time       持续时间
     * @param delay      延时
     * @param fromX      X 轴起始位置
     * @param toX        X 轴结束位置
     * @param fromY      Y 轴起始位置
     * @param toY        Y 轴结束位置
     */
    public static void translate(Node node, int cycleCount, double time, double delay, double fromX, double toX, double fromY, double toY) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(time), node);
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setDelay(Duration.seconds(delay));
        translateTransition.setFromX(fromX);
        translateTransition.setToX(toX);
        translateTransition.setFromY(fromY);
        translateTransition.setToY(toY);
        translateTransition.setCycleCount(cycleCount);
        translateTransition.play();
    }
    
    /**
     * 旋转动画
     *
     * @param node  节点
     * @param time  持续时间
     * @param delay 延时
     * @param from  开始角度
     * @param to    结束角度
     */
    public static RotateTransition rotate(Node node, double time, double delay, double from, double to) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(time), node);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setDelay(Duration.seconds(delay));
        rotateTransition.setFromAngle(from);
        rotateTransition.setToAngle(to);
        rotateTransition.setCycleCount(2);
        rotateTransition.play();
        return rotateTransition;
    }
    
}
