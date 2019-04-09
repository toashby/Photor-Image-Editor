package photoeditor;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ta428
 */
public class BrightnessWindow {
    Stage stage = new Stage();
    BorderPane window = new BorderPane();
    final Slider slider = new Slider(-100, 100, 0);
    final Label label = new Label();
    Button button = new Button("OK");

    /**
     *
     */
    public void showStage(){
        Scene scene = new Scene(window,200,200);

        window.setTop(label);
        window.setCenter(slider);
        window.setBottom(button);

        stage.setTitle("Brightness");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

    }

}

