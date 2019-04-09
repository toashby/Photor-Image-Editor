package photoeditor;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ta428 on 28/11/17.
 */
public class ColorizeEffect {

    public Image main(Image image){
        BufferedImage bufimage = SwingFXUtils.fromFXImage(image, null);

        System.out.println("ran");

        for(int i = 0; i < bufimage.getWidth(); i++){
            for(int j = 0; j < bufimage.getHeight(); j++){
                Color color = new Color(bufimage.getRGB(i, j));
                int invRed = 255 - color.getRed();
                int invGreen = 255 - color.getGreen();
                int invBlue = 255 - color.getBlue();

                Color newColor = new Color(invRed, invGreen, invBlue);

                bufimage.setRGB(i, j, newColor.getRGB());
            }
        }


        File outputfile = new File("image.png");
        try {
            ImageIO.write(bufimage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("saved");

        image = SwingFXUtils.toFXImage(bufimage, null);
        return image;
    }

}
