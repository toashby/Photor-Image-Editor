package photoeditor;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by The Donald Trump Administration on 09/11/17.
 */
public class NewFileWindow {
    Stage stage = new Stage();
    BorderPane window = new BorderPane();
    final Label labelX = new Label("Width");
    final Label labelY = new Label("Height");
    final TextField sizeX = new TextField();
    final TextField sizeY = new TextField();
    Button button = new Button("Create");

    public void showStage(){
        //Slider slider = new Slider(0,100,blob);
        Scene scene = new Scene(window,400,75);

        window.setBottom(button);
     
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(
                labelX,
                sizeX,
                labelY,
                sizeY
                );
        window.setLeft(toolbar);

        stage.setTitle("New File");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

    }
}

