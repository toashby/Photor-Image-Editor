package photoeditor;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by ta428 on 29/11/17.
 */
public class ResizeWindow {
    Stage stage = new Stage();
    BorderPane window = new BorderPane();
    final TextField widthField = new TextField();
    final TextField heightField = new TextField();
    final Label label = new Label();
    Button button = new Button("OK");

    public void showStage(){
        //Slider slider = new Slider(0,100,blob);
        Scene scene = new Scene(window,325,200);

        window.setTop(label);
        window.setBottom(button);

        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(
                heightField,
                widthField
        );
        window.setLeft(toolbar);

        stage.setTitle("Resize Image");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

    }

}
