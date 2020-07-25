package top.lucency.controller;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    
    public static final String TRANSFER_SOUND_NAME = "\\out.";
    
    public static String picPath;
    
    public static Stage stage;
    
    public static MainController controller;
    
    public static RotateTransition rotateTransition;
    
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Button pic;
    @FXML
    public Button out;
    @FXML
    public Button start;
    @FXML
    public Label outPath;
    @FXML
    public Button gear;
    @FXML
    public TextField suffix;
    
    @FXML
    public void choosePic() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("图片类型", "*.png"));
        File file = fc.showOpenDialog(new Stage());
        if (file == null) {
            outPath.setText("PLEASE CHOOSE A PIC !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        picPath = file.getAbsolutePath();
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
        if (suffix.getText().length() == 0) {
            outPath.setText("PLEASE ENTER A SUFFIX !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        outDir = file.getAbsolutePath();
        outDir += TRANSFER_SOUND_NAME + suffix.getText();
        outPath.setText(outDir);
    }
    
    @FXML
    public void start() {
        if (outDir == null) {
            outPath.setText("PLEASE CHOOSE A OUTPUT DIR !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        if (picPath == null) {
            outPath.setText("PLEASE CHOOSE A PIC !");
            AnimationUtil.translate(outPath, 3, 0.1, 0, 0, 8, 0, 0);
            return;
        }
        outPath.setText("DECODING…… PLEASE WAIT A SEC");
        rotateTransition = AnimationUtil.rotate(gear, 10, 0, 0, 360);
        rotateTransition.setOnFinished(actionEvent -> rotateTransition.play());
        new Thread(new Start()).start();
    }
    
    public void init(Stage stage, MainController controller) {
        MainController.stage = stage;
        MainController.controller = controller;
    }
    
}

class Start extends Thread {
    
    @Override
    public void run() {
        MainController.controller.start.setDisable(true);
        MainController.controller.pic.setDisable(true);
        MainController.controller.out.setDisable(true);
        MainController.controller.suffix.setDisable(true);
        
        DecoderUtil.start(MainController.picPath, MainController.outDir);
        MainController.rotateTransition.stop();
        Platform.runLater(() -> MainController.controller.outPath.setText("SUCCEED !"));
        AnimationUtil.translate(MainController.controller.outPath, 3, 0.1, 0, 0, 8, 0, 0);
        
        MainController.outDir = null;
        MainController.picPath = null;
        MainController.controller.suffix.setText("");
        MainController.controller.start.setDisable(false);
        MainController.controller.pic.setDisable(false);
        MainController.controller.out.setDisable(false);
        MainController.controller.suffix.setDisable(false);
    }
    
}