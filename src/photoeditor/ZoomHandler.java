package photoeditor;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ZoomHandler {

    private Timeline timeline;

    public ZoomHandler() {         
         this.timeline = new Timeline(144);
    }
    
        public void defaultZoomInstant(StackPane stack){
        //double trueOldScaleX = 1;
        //double scaleSaved = scaleX;
        //Bounds bounds = node.localToScene(node.getBoundsInLocal());
        //double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        //double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

                 //new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - trueOldScaleX * dx)),
                 //new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node2.getTranslateX() - trueOldScaleX * dx)),
        //node.setScaleX(x);
        //node.setScaleY(y);
        //node.setScaleX(scaleX);
        //node.setScaleY(scaleY);
            //timeline.getKeyFrames().clear();
            //timeline.getKeyFrames().addAll(
            //new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - trueOldScaleX * dx)),
            //new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node2.getTranslateX() - trueOldScaleX * dx)),
            //new KeyFrame(Duration.millis(1000), new KeyValue(node.scaleXProperty(), trueOldScaleX)),
            //new KeyFrame(Duration.millis(1000), new KeyValue(node.scaleYProperty(), trueOldScaleX)),
                //new KeyFrame(Duration.millis(1000), new KeyValue(node.scaleXProperty(), scaleSaved)),  
                //new KeyFrame(Duration.millis(1000), new KeyValue(node.scaleYProperty(), scaleSaved)) 
        //);
        //timeline.play();
        
        stack.setScaleX(1);
        stack.setScaleY(1);
    }
        
    public void applyZoomInstant(StackPane stack, double scaleX) {
        stack.setScaleX(scaleX);
        stack.setScaleY(scaleX);
    }
    
    
    public void defaultZoom(StackPane stack, double x, double y){
        double trueOldScaleX = 1;
        //Bounds bounds = node.localToScene(node.getBoundsInLocal());
        //double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        //double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                 //new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - trueOldScaleX * dx)),
                 //new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node2.getTranslateX() - trueOldScaleX * dx)),
                 new KeyFrame(Duration.millis(1), new KeyValue(stack.scaleXProperty(), trueOldScaleX)),
                new KeyFrame(Duration.millis(1), new KeyValue(stack.scaleYProperty(), trueOldScaleX))
        );
        timeline.play();
        
    }

    public void zoom(StackPane stack, double factor, double x, double y) {    
        double oldScale = stack.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        Bounds bounds = stack.localToScene(stack.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX())); // Get the boundary of the image to know where to scale.
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(200), new KeyValue(stack.translateXProperty(), stack.getTranslateX() - f * dx)),
            new KeyFrame(Duration.millis(200), new KeyValue(stack.translateYProperty(), stack.getTranslateY() - f * dy)),
            new KeyFrame(Duration.millis(200), new KeyValue(stack.scaleXProperty(), scale)),
            new KeyFrame(Duration.millis(200), new KeyValue(stack.scaleYProperty(), scale))
        );
        
       
        timeline.play();
    }
    
  
}