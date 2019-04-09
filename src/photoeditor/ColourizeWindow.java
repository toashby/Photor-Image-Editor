package photoeditor;

import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ColourizeWindow {
    Stage stage = new Stage();
    BorderPane window = new BorderPane();
    final Slider slider = new Slider(-100, 100, 0);
    final Label label = new Label();
    Button button = new Button("OK");

    public void showStage(){
        Scene scene = new Scene(window,200,200);

        window.setTop(label);
        window.setCenter(slider);
        window.setBottom(button);

        stage.setTitle("Colourize");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

    }

}

