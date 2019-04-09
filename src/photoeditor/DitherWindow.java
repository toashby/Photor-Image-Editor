/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoeditor;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ta428
 */
public class DitherWindow {
    Stage stage = new Stage();
    VBox window = new VBox();
    BorderPane sliderPane = new BorderPane();
    final Slider slider = new Slider(2, 128, 0);
    final Label label = new Label();
    final CheckBox checkBox = new CheckBox("Monochrome");
    Button button = new Button("OK");

    public void showStage(){
        
        Scene scene = new Scene(window,200,200);
      
        window.setSpacing(10);
        sliderPane.setLeft(slider);
        sliderPane.setRight(label);
        window.getChildren().add(sliderPane);
        window.getChildren().add(checkBox);
        window.getChildren().add(button);

        stage.setTitle("Dither");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();

    }
    
    
}
