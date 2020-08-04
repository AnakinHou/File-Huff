package top.lucency.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import top.lucency.utils.AnimationUtil;
import top.lucency.utils.DecoderUtil;

import java.io.File;

/**
 * @author 86181
 */
public class MainController {
    
    public static String outDir;
    
    public static String transferFileName = "\\out.";
    
    public static String huffPath;
    
    public static Stage stage;
    
    public static MainController controller;
    
    public static boolean isFinished = false;
    
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Button huff;
    @FXML
    public Button out;
    @FXML
    public Button start;
    @FXML
    public Label outPath;
    @FXML
    public SVGPath logo;
    @FXML
    public SVGPath logoWhite;
    @FXML
    public SVGPath logoBlack;
    @FXML
    public Button startWhite;
    @FXML
    public Button startBlack;
    @FXML
    public Button outWhite;
    @FXML
    public Button outBlack;
    @FXML
    public Button huffWhite;
    @FXML
    public Button huffBlack;
    
    @FXML
    public void chooseHuffEnter() {
        mouseEnterEvent(huffBlack, huffWhite, huff);
    }
    
    @FXML
    public void chooseOutEnter() {
        mouseEnterEvent(outBlack, outWhite, out);
    }
    
    @FXML
    public void startEnter() {
        mouseEnterEvent(startBlack, startWhite, start);
    }
    
    @FXML
    public void chooseHuffLeave() {
        mouseLeaveEvent(huffBlack, huffWhite, huff);
    }
    
    @FXML
    public void chooseOutLeave() {
        mouseLeaveEvent(outBlack, outWhite, out);
    }
    
    @FXML
    public void startLeave() {
        mouseLeaveEvent(startBlack, startWhite, start);
    }
    
    @FXML
    public void chooseHuff() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("HUFF类型", "*.huf"));
        File file = fc.showOpenDialog(new Stage());
        if (file == null) {
            outPath.setText("PLEASE CHOOSE A HUFF !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        if (!DecoderUtil.getHeader(file.getAbsolutePath())) {
            outPath.setText("ERROR FILE !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        huffPath = file.getAbsolutePath();
    }
    
    @FXML
    public void chooseOut() {
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(new Stage());
        if (file == null) {
            outPath.setText("PLEASE CHOOSE A OUTPUT DIR !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        outDir = file.getAbsolutePath() + transferFileName + DecoderUtil.header.getSuffix();
        outPath.setText(outDir);
    }
    
    @FXML
    public void start() {
        if (huffPath == null) {
            outPath.setText("PLEASE CHOOSE A HUFF !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        if (outDir == null) {
            outPath.setText("PLEASE CHOOSE A OUTPUT DIR !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        outPath.setText("DECODING…… PLEASE WAIT A SEC");
        
        isFinished = false;
        new Thread(new Roll()).start();
        new Thread(new Start()).start();
    }
    
    public void init(Stage stage, MainController controller) {
        MainController.stage = stage;
        MainController.controller = controller;
    }
    
    /**
     * 鼠标移入阴影变化
     *
     * @param black 黑色外阴影
     * @param white 白色外阴影
     * @param self  自身内阴影
     */
    public void mouseEnterEvent(Node black, Node white, Node self) {
        setDropShadow(black, 0);
        setDropShadow(white, 0);
        setInnerShadow(self, 0.15, 10, 10);
    }
    
    /**
     * 设置外阴影
     *
     * @param node  node
     * @param alpha 透明度
     */
    public void setDropShadow(Node node, int alpha) {
        node.setOpacity(alpha);
    }
    
    /**
     * 设置内阴影
     *
     * @param node node
     */
    public void setInnerShadow(Node node, double alpha, int offX, int offY) {
        InnerShadow shadow = new InnerShadow();
        shadow.setColor(Color.color(0, 0, 0, alpha));
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setOffsetX(offX);
        shadow.setOffsetY(offY);
        node.setEffect(shadow);
    }
    
    /**
     * 鼠标移出阴影变化
     *
     * @param black 黑色外阴影
     * @param white 白色外阴影
     * @param self  自身内阴影
     */
    public void mouseLeaveEvent(Node black, Node white, Node self) {
        setDropShadow(black, 1);
        setDropShadow(white, 1);
        setInnerShadow(self, 0.05, 8, 8);
    }
    
}

class Start extends Thread {
    
    @Override
    public void run() {
        MainController.controller.start.setDisable(true);
        MainController.controller.huff.setDisable(true);
        MainController.controller.out.setDisable(true);
        
        DecoderUtil.start(MainController.huffPath, MainController.outDir);
        MainController.isFinished = true;
        Platform.runLater(() -> MainController.controller.outPath.setText("SUCCEED !"));
        AnimationUtil.translate(MainController.controller.outPath, 3, 0.1, 0, 0, 8, 0, 0);
        
        MainController.outDir = null;
        MainController.huffPath = null;
        MainController.controller.start.setDisable(false);
        MainController.controller.huff.setDisable(false);
        MainController.controller.out.setDisable(false);
    }
    
}

class Roll extends Thread {
    
    @Override
    public void run() {
        DropShadow blackShadow = setShadow(10, 10, Color.color(0, 0, 0, 0.1));
        DropShadow whiteShadow = setShadow(-10, -10, Color.color(1, 1, 1, 1));
        InnerShadow selfShadow = new InnerShadow();
        selfShadow.setWidth(80);
        selfShadow.setHeight(80);
        selfShadow.setOffsetX(10);
        selfShadow.setOffsetY(10);
        selfShadow.setColor(Color.color(0, 0, 0, 0.05));
        // 从黑色向左转开始
        right2Left(blackShadow, whiteShadow, selfShadow);
    }
    
    /**
     * 设置外阴影
     *
     * @param offX  offX
     * @param offY  offY
     * @param color color
     * @return 外阴影
     */
    public DropShadow setShadow(int offX, int offY, Color color) {
        DropShadow shadow = new DropShadow();
        shadow.setSpread(0.4);
        shadow.setOffsetX(offX);
        shadow.setOffsetY(offY);
        shadow.setWidth(70);
        shadow.setHeight(70);
        shadow.setColor(color);
        return shadow;
    }
    
    public void right2Left(DropShadow blackShadow, DropShadow whiteShadow, InnerShadow selfShadow) {
        for (int i = 10; i >= -10; i--) {
            if (MainController.isFinished) {
                return;
            }
            blackShadow.setOffsetX(i);
            blackShadow.setOffsetY(10);
            selfShadow.setOffsetX(i);
            selfShadow.setOffsetY(10);
            whiteShadow.setOffsetX(-i);
            whiteShadow.setOffsetY(-10);
            MainController.controller.logoBlack.setEffect(blackShadow);
            MainController.controller.logo.setEffect(selfShadow);
            MainController.controller.logoWhite.setEffect(whiteShadow);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        down2Top(blackShadow, whiteShadow, selfShadow);
    }
    
    public void down2Top(DropShadow blackShadow, DropShadow whiteShadow, InnerShadow selfShadow) {
        for (int i = 10; i >= -10; i--) {
            if (MainController.isFinished) {
                return;
            }
            blackShadow.setOffsetX(-10);
            blackShadow.setOffsetY(i);
            selfShadow.setOffsetX(-10);
            selfShadow.setOffsetY(i);
            whiteShadow.setOffsetX(10);
            whiteShadow.setOffsetY(-i);
            MainController.controller.logoBlack.setEffect(blackShadow);
            MainController.controller.logo.setEffect(selfShadow);
            MainController.controller.logoWhite.setEffect(whiteShadow);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        left2Right(blackShadow, whiteShadow, selfShadow);
    }
    
    public void left2Right(DropShadow blackShadow, DropShadow whiteShadow, InnerShadow selfShadow) {
        for (int i = -10; i <= 10; i++) {
            if (MainController.isFinished) {
                return;
            }
            blackShadow.setOffsetX(i);
            blackShadow.setOffsetY(-10);
            selfShadow.setOffsetX(i);
            selfShadow.setOffsetY(-10);
            whiteShadow.setOffsetX(-i);
            whiteShadow.setOffsetY(10);
            MainController.controller.logoBlack.setEffect(blackShadow);
            MainController.controller.logo.setEffect(selfShadow);
            MainController.controller.logoWhite.setEffect(whiteShadow);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        top2Down(blackShadow, whiteShadow, selfShadow);
    }
    
    public void top2Down(DropShadow blackShadow, DropShadow whiteShadow, InnerShadow selfShadow) {
        for (int i = -10; i <= 10; i++) {
            if (MainController.isFinished) {
                return;
            }
            blackShadow.setOffsetX(10);
            blackShadow.setOffsetY(i);
            selfShadow.setOffsetX(10);
            selfShadow.setOffsetY(i);
            whiteShadow.setOffsetX(-10);
            whiteShadow.setOffsetY(-i);
            MainController.controller.logoBlack.setEffect(blackShadow);
            MainController.controller.logo.setEffect(selfShadow);
            MainController.controller.logoWhite.setEffect(whiteShadow);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        right2Left(blackShadow, whiteShadow, selfShadow);
    }
    
}