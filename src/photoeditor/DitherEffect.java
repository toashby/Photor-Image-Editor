/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoeditor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author tom
 */
public class DitherEffect {
    int factor;
    public Image dither(Image image, int factor){

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        
        for(int j = 0; j < bufferedImage.getHeight() - 1; j++){
            for(int i = 1; i < bufferedImage.getWidth() - 1; i++){
                Color pixel = new Color(bufferedImage.getRGB(i, j));
                float pixelR = pixel.getRed();
                float pixelG = pixel.getGreen();
                float pixelB = pixel.getBlue();
                this.factor = factor;
                int newPixelR = Math.round(factor * pixelR / 255) * (255 / factor);
                int newPixelG = Math.round(factor * pixelG / 255) * (255 / factor);
                int newPixelB = Math.round(factor * pixelB / 255) * (255 / factor);
        
                bufferedImage.setRGB(i, j, new Color(newPixelR, newPixelG, newPixelB).getRGB());
                
                float errorR = pixelR - newPixelR;
                float errorG = pixelG - newPixelG;
                float errorB = pixelB - newPixelB;
                
                Color color = new Color(bufferedImage.getRGB(i + 1, j));
                float r = color.getRed();
                float g = color.getGreen();
                float b = color.getBlue();
                r = r + errorR * 7 / 16f;
                g = g + errorG * 7 / 16f;
                b = b + errorB * 7 / 16f;
                if(r < 0){r = 0;}
                if(g < 0){g = 0;}
                if(b < 0){b = 0;}
                if(r > 255){r = 255;}
                if(g > 255){g = 255;}
                if(b > 255){b = 255;}
                Color newC = new Color(Math.round(r), Math.round(g), Math.round(b));
                bufferedImage.setRGB(i + 1, j, newC.getRGB());
                
                color = new Color(bufferedImage.getRGB(i - 1, j + 1));
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                r = r + errorR * 3 / 16f;
                g = g + errorG * 3 / 16f;
                b = b + errorB * 3 / 16f;
                if(r < 0){r = 0;}
                if(g < 0){g = 0;}
                if(b < 0){b = 0;}
                if(r > 255){r = 255;}
                if(g > 255){g = 255;}
                if(b > 255){b = 255;}
                Color newC2 = new Color(Math.round(r), Math.round(g), Math.round(b));
                bufferedImage.setRGB(i - 1, j + 1, newC2.getRGB());
                
                color = new Color(bufferedImage.getRGB(i, j + 1));
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                r = r + errorR * 5 / 16f;
                g = g + errorG * 5 / 16f;
                b = b + errorB * 5 / 16f;
                if(r < 0){r = 0;}
                if(g < 0){g = 0;}
                if(b < 0){b = 0;}
                if(r > 255){r = 255;}
                if(g > 255){g = 255;}
                if(b > 255){b = 255;}
                Color newC3 = new Color(Math.round(r), Math.round(g), Math.round(b));
                bufferedImage.setRGB(i, j + 1, newC3.getRGB());
               
                color = new Color(bufferedImage.getRGB(i + 1, j + 1));
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                r = r + errorR * 1 / 16f;
                g = g + errorG * 1 / 16f;
                b = b + errorB * 1 / 16f;
                if(r < 0){r = 0;}
                if(g < 0){g = 0;}
                if(b < 0){b = 0;}
                if(r > 255){r = 255;}
                if(g > 255){g = 255;}
                if(b > 255){b = 255;}
                Color newC4 = new Color(Math.round(r), Math.round(g), Math.round(b));
                bufferedImage.setRGB(i + 1, j + 1, newC4.getRGB());
            }
        }
         
        Image newImage = SwingFXUtils.toFXImage(bufferedImage, null);
        return newImage;
    }
}
