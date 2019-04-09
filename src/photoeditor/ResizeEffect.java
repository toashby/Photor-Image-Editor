package photoeditor;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Created by ta428 on 28/11/17.
 */
public class ResizeEffect {

    public Image main(Image image, int newWidth, int newHeight){

        BufferedImage bufimage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage newBufimage = new BufferedImage(newWidth, newHeight, TYPE_INT_ARGB);

        int oldHeight = bufimage.getHeight();
        int oldWidth = bufimage.getWidth();

        for(int i = 0; i < newBufimage.getWidth(); i++){
            for(int j = 0; j < newBufimage.getHeight(); j++){

                int oldX = (i * oldWidth);
                int oldY = (j * oldHeight);
                oldX = oldX / newWidth;
                oldY = oldY / newHeight;


                Color color = new Color(bufimage.getRGB(oldX, oldY));

                newBufimage.setRGB(i, j, color.getRGB());
            }
        }


        File outputfile = new File("imageresized.png");
        try {
            ImageIO.write(newBufimage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("saved");

        image = SwingFXUtils.toFXImage(newBufimage, null);
        return image;
    }

}
