/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoeditor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author tom
 */
public class SelectHandler {
    int x1,y1,x2,y2;
    Image oldImage;
    boolean selectActive = false;
    boolean x1isLower;
    boolean y1isLower;
    int width;
    int height;
    int startX;
    int startY;
    
    public Image main(Image image) {
        Image newImage;
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        newImage = SwingFXUtils.toFXImage(bufferedImage, null);
        return newImage;
    }
    
    public void setPoint1(int x1, int y1){
        this.x1 = x1;
        this.y1 = y1;
    }
     public void setPoint2(int x2, int y2){
        this.x2 = x2;
        this.y2 = y2;
    }
     public void setOldImage(Image image){
         oldImage = image;
     }
     public Image getOldImage(){
         return oldImage;
     }
     
     public void setSelectActive(boolean selectActive){
         this.selectActive = selectActive;
     }
     
     public boolean getSelectActive(){
         return selectActive;
     }
     
     public Image getSelection(){
        BufferedImage oldBufferedImage = SwingFXUtils.fromFXImage(oldImage, null);
        BufferedImage newBufImage = new BufferedImage(Math.abs(y2 - y1), Math.abs(x2 - x1), TYPE_INT_ARGB);
        
        //down right
        if(x1 < x2 & y1 < y2){
            newBufImage = extractSelection(oldBufferedImage, x1,y1,x2,y2);
        }
        //up right
        if(x1 < x2 & y2 < y1){
             newBufImage = extractSelection(oldBufferedImage, x1,y2,x2,y1);
        }
        //up left
        if(x2 < x1 & y2 < y1){
             newBufImage = extractSelection(oldBufferedImage, x2,y2,x1,y1);
        }
        //down left
        if(x2 < x1 & y1 < y2){
             newBufImage = extractSelection(oldBufferedImage, x2,y1,x1,y2);
        }
        
        Image newImage = SwingFXUtils.toFXImage(newBufImage, null);
        return newImage;
     }
     
     public BufferedImage extractSelection(BufferedImage oldBufImage, int x1, int y1, int x2, int y2){
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        BufferedImage newBufImage = new BufferedImage(width, height, TYPE_INT_ARGB);
          for(int i = 0; i < newBufImage.getWidth(); i++){
            for(int j = 0; j < newBufImage.getHeight(); j++){
                newBufImage.setRGB(i, j, oldBufImage.getRGB(x1 + i, y1 + j));
            }
        }
         return newBufImage;
     }
    
    public Image drawBox(Image image){
        Image newImage;
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        
        Color newColor = new Color(0, 0, 0, 255);
        int wormColor = 0x000000FF;
        //bufferedImage.setRGB(x, y, newColor.getRGB());
        if(x2 > x1){
            x1isLower = true;
            for(int i = x1; i < x2; i++){
                bufferedImage.setRGB(i, y1, invertPixel(bufferedImage.getRGB(i, y1)));
                bufferedImage.setRGB(i, y2, invertPixel(bufferedImage.getRGB(i, y2)));
          
            }
        }else{
            x1isLower = false;
            for(int i = x2; i < x1; i++){
                bufferedImage.setRGB(i, y1, invertPixel(bufferedImage.getRGB(i, y1)));
                bufferedImage.setRGB(i, y2, invertPixel(bufferedImage.getRGB(i, y2)));
            }
        }
        
        if(y2 > y1){
            y1isLower = true;
            for(int j = y1; j < y2; j++){
                bufferedImage.setRGB(x1, j, invertPixel(bufferedImage.getRGB(x1, j)));
                bufferedImage.setRGB(x2, j, invertPixel(bufferedImage.getRGB(x2, j)));
            }
        }else{
            y1isLower = false;
            for(int j = y2; j < y1; j++){
                bufferedImage.setRGB(x1, j, invertPixel(bufferedImage.getRGB(x1, j)));
                bufferedImage.setRGB(x2, j, invertPixel(bufferedImage.getRGB(x2, j)));
            }
        }
        
        newImage = SwingFXUtils.toFXImage(bufferedImage, null);
        return newImage;
    }
    
    public int invertPixel(int inputColor){
       
        Color color = new Color(inputColor);
        int invRed = 255 - color.getRed();
        int invGreen = 255 - color.getGreen();
        int invBlue = 255 - color.getBlue();

        Color newColor = new Color(invRed, invGreen, invBlue);
       
       return newColor.getRGB();
    }
    
    public int getHeight(){
         return height;   
    }
    public int getWidth(){
        return width;
    }
    
    public int getStartX(){
        if(x1isLower == true){
            return x1;
        }else{
            return x2;
        }
    }
    public int getStartY(){
        if(y1isLower == true){
            return y1;
        }else{
            return y2;
        }
    }
}
