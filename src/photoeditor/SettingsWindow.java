/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoeditor;

/**
 *
 * @author ta428
 */
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsWindow {
    Stage stage = new Stage();
    BorderPane window = new BorderPane();
    final ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList("Default", "Dark", "Blue", "LargeText"));
    final Label label = new Label();
    Button button = new Button("OK");
    Number theme;

    public void showStage(){
        
        label.setText("Change Theme");
        
        Scene scene = new Scene(window,200,200);
        window.setTop(label);
        window.setCenter(choiceBox);
        window.setBottom(button);

        stage.setTitle("Settings");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();
        
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                theme = newValue.intValue();
                System.out.println("Number" + theme.toString());
            }
        });
    }

}