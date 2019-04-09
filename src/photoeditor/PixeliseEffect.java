
package photoeditor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author ta428
 */
public class PixeliseEffect {
    Color current;
    
    public Image main(Image image, int scale) {
        Image newImage;
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        for (int i = 0; i < bufferedImage.getWidth(); i = i + scale){
            for(int j = 0; j < bufferedImage.getHeight(); j = j + scale){
                int r = 0;
                int g = 0;
                int b = 0; 
                
                int pixelCounter = 0;
        
                for (int x = 0; x < scale; x++){
                    for(int y = 0; y < scale; y++){ 
                        if(i + x < bufferedImage.getWidth()){
                            if(j + y < bufferedImage.getHeight()){
                                current = new Color(bufferedImage.getRGB(i + x, j + y));
           
                                r += current.getRed();
                                g += current.getGreen();
                                b += current.getBlue();
                                
                                pixelCounter++;
                            }
                        }
                    }
                }
                r = r/pixelCounter;
                g = g/pixelCounter;
                b = b/pixelCounter;
                Color newColor = new Color(r,g,b);
               
                for (int x = 0; x < scale; x++){
                    for(int y = 0; y < scale; y++){ 
                       if(i + x < bufferedImage.getWidth()){
                            if(j + y < bufferedImage.getHeight()){
                                bufferedImage.setRGB(i + x, j + y, newColor.getRGB());
                            }
                       }
                    }
                }
            }
        }
        newImage = SwingFXUtils.toFXImage(bufferedImage, null);
        return newImage;
    }
}
