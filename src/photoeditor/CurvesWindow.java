package photoeditor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ta428
 */
public class CurvesWindow {
    javafx.scene.paint.Color bgColor = new javafx.scene.paint.Color(1.0f, 1.0f, 1.0f, 1.0f);
    javafx.scene.paint.Color histoRColor = new javafx.scene.paint.Color(.5f, .2f, .2f, 1.0f);
    javafx.scene.paint.Color histoGColor = new javafx.scene.paint.Color(.2, .5f, .2f, 1.0f);
    javafx.scene.paint.Color histoBColor = new javafx.scene.paint.Color(.2f, .2f, .5f, 1.0f);
    javafx.scene.paint.Color histoCColor = new javafx.scene.paint.Color(.2f, .2f, .2f, 1.0f);
    javafx.scene.paint.Color curveColor = new javafx.scene.paint.Color(.0f, .0f, .0f, 1.0f);
    Stage stage = new Stage();
    BorderPane window = new BorderPane();
    final Label label = new Label();
    Button button = new Button("OK");
    ComboBox comboBox = new ComboBox();
    Canvas canvas = new Canvas(256, 256);
    Pane wrapperPane = new Pane();
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    int x,y;
    String newVal;
    int[] curve = new int[256];
    int[] CLevels = new int[256];
    int[] RLevels = new int[256];
    int[] GLevels = new int[256];
    int[] BLevels = new int[256];
    float maxR, maxG, maxB, maxC;
    BufferedImage bufferedImage = new BufferedImage(256, 256, TYPE_INT_ARGB);
    BufferedImage newImage;
    BufferedImage originalImage;
    int width, height;
    Color color = new Color(0,0,0,1);
    
    
    public void showStage(Image image){
        originalImage = SwingFXUtils.fromFXImage(image, null);
        width = originalImage.getWidth();
        height = originalImage.getHeight();
        
        newImage = new BufferedImage(width, height, TYPE_INT_ARGB);
                
        comboBox.getItems().add("Combined");
        comboBox.getItems().add("Red");
        comboBox.getItems().add("Green");
        comboBox.getItems().add("Blue");
        comboBox.getSelectionModel().selectFirst();
        wrapperPane.setOnMousePressed(OnMousePressedEventHandler);
        wrapperPane.setOnMouseDragged(OnMouseDraggedEventHandler);
        wrapperPane.getChildren().add(canvas);
        graphicsContext.setFill(bgColor);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        //tally up pixels of each brightness into 4 sets of 256 buckets
        //one for each channel + combined
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                Color c = new Color(originalImage.getRGB(i, j));
                RLevels[c.getRed()]++;
                GLevels[c.getGreen()]++;
                BLevels[c.getBlue()]++;
                
                CLevels[c.getRed()]++;
                CLevels[c.getGreen()]++;
                CLevels[c.getBlue()]++;
            }
        }
        
        //find the maximum value of each channel for scaling correctly on the canvas
        for(int counter = 0; counter < RLevels.length; counter++){
            if (RLevels[counter] > maxR){
                maxR = RLevels[counter];    
            }
            if (GLevels[counter] > maxG){
                maxG = GLevels[counter];    
            }
            if (BLevels[counter] > maxB){
                maxB = BLevels[counter];    
            }
            if (CLevels[counter] > maxC){
                maxC = CLevels[counter];    
            }
        }
        
        //scale the value in each bucket based on the max value (normalised 0-255)
        for(int i  = 0; i < RLevels.length; i++){
            RLevels[i] = (int)((RLevels[i] / maxR) * 255); 
        }
        for(int i  = 0; i < GLevels.length; i++){
            GLevels[i] = (int)((GLevels[i] / maxG) * 255);
        }
        for(int i  = 0; i < BLevels.length; i++){
            BLevels[i] = (int)((BLevels[i] / maxB) * 255);  
        }
        for(int i  = 0; i < CLevels.length; i++){
            CLevels[i] = (int)((CLevels[i] / maxC) * 255);  
        }
        
        //set up the scene
        Image initImage = SwingFXUtils.toFXImage(bufferedImage, null);
        graphicsContext.drawImage(initImage, 0, 0);
        Scene scene = new Scene(window,260,320);
        window.setTop(comboBox);
        window.setCenter(wrapperPane);
        window.setBottom(button);
        stage.setTitle("Curves");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();
        
        newVal = "Combined";
        
        drawHistogram();
        
        //draw initial curve from bottom left to top right
        for(int i = 0; i < curve.length; i++){
                curve[i] = i;
                drawCurve(255 - i,i);
        }
     
        //listen for changes in the combobox
        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                newVal = newValue;
                drawHistogram();
                //drawCurve();
            }
        });

    }
    
    EventHandler<MouseEvent> OnMousePressedEventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                x = (int)t.getX();
                y = (int)t.getY();
                
                drawCurve(x, y);
            }
    };
    EventHandler<MouseEvent> OnMouseDraggedEventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                x = (int)t.getX();
                y = (int)t.getY();
                
                drawCurve(x, y);
            }
    };
    
    public void drawCurve(int cx, int cy){
        curve[cx] = 255 - cy;
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                       
        drawHistogram();
        
        graphicsContext.setFill(curveColor);
        
        for(int i = 0; i < curve.length; i++){
            graphicsContext.fillRect(i, 255 - curve[i], 1, 1);
        }   
        
    }
    
    
    //TODO
    public Image applyCurve(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int newBrightness;
                int brightness;
                Color oldColor = new Color(originalImage.getRGB(i, j));
                
                
                switch(newVal){
                    case "Red":
                        brightness = oldColor.getRed();
                        newBrightness = curve[brightness];            
                        Color newRColor = new Color(newBrightness, (int)oldColor.getGreen(), (int)oldColor.getBlue());
                        newImage.setRGB(i, j, newRColor.getRGB());
                        break;
                    
                    case "Blue":
                        brightness = oldColor.getBlue();   
                        newBrightness = curve[brightness];
                        Color newBColor = new Color((int)oldColor.getRed(), (int)oldColor.getGreen(), newBrightness);
                        newImage.setRGB(i, j, newBColor.getRGB());
                        break;
                    case "Green":
                        brightness = oldColor.getGreen();
                        newBrightness = curve[brightness];
                        Color newGColor = new Color((int)oldColor.getRed(), newBrightness, (int)oldColor.getBlue());
                        newImage.setRGB(i, j, newGColor.getRGB());
                        break;
               
                    case "Combined":
                        brightness = oldColor.getRed() + oldColor.getGreen() + oldColor.getBlue();
                        newBrightness = curve[brightness];
                        newBrightness = newBrightness * 3;
                        Color newCColor = new Color((int)(oldColor.getRed() * newBrightness), (int)(oldColor.getGreen() * newBrightness), (int)(oldColor.getBlue() * newBrightness));
                        newImage.setRGB(i, j, newCColor.getRGB());
                        break;
                }
            }
        }
        Image image = SwingFXUtils.toFXImage(newImage, null);
        return image;
    }
    
    public void drawHistogram(){
        graphicsContext.clearRect(0, 0, 255, 255);
        switch (newVal) {
            case "Red":
                graphicsContext.setStroke(histoRColor);
                for(int i  = 0; i < RLevels.length; i++){
                    graphicsContext.strokeLine(i, 255, i, 255 - RLevels[i]);
                    //drawCurve(i, RLevels[i]);
                }
                break;
            case "Green":
                graphicsContext.setStroke(histoGColor);
                for(int i  = 0; i < GLevels.length; i++){
                    graphicsContext.strokeLine(i, 255, i, 255 - GLevels[i]);
                    //drawCurve(i, GLevels[i]);
                }
                break;
            case "Blue":
                graphicsContext.setStroke(histoBColor);
                for(int i  = 0; i < BLevels.length; i++){
                    graphicsContext.strokeLine(i, 255, i, 255 - BLevels[i]);
                    //drawCurve(i, BLevels[i]);
                }
                break;
            default:
                graphicsContext.setStroke(histoCColor);  
                for(int i  = 0; i < CLevels.length; i++){
                    graphicsContext.strokeLine(i, 255, i, 255 - CLevels[i]);
                    //drawCurve(i, CLevels[i]);
                }
                break;
        }
    }
}
