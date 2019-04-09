package photoeditor;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class SeamCarveEffect {

    BufferedImage originalImage;
    BufferedImage bufimage;
    BufferedImage seamedBufImage; //new
    float[][] contrast;
    int[][] fullSeams;



    public Image main(Image originalimage) {
        originalImage = SwingFXUtils.fromFXImage(originalimage, null);
        bufimage = SwingFXUtils.fromFXImage(originalimage, null);
        seamedBufImage = new BufferedImage(originalImage.getWidth() - 1,originalImage.getHeight(), TYPE_INT_ARGB); //new
        contrast = new float[bufimage.getWidth()][bufimage.getHeight()];
        fullSeams = new int[originalImage.getWidth()][originalImage.getHeight()];

        float[][] Rcontrast = new float[bufimage.getWidth()][bufimage.getHeight()];
        float[][] Gcontrast = new float[bufimage.getWidth()][bufimage.getHeight()];
        float[][] Bcontrast = new float[bufimage.getWidth()][bufimage.getHeight()];

        //VERTICAL CONTRAST
        for(int i = 0; i < bufimage.getWidth(); i++) {
            for (int j = 0; j < bufimage.getHeight() - 1; j++) {
                Color color = new Color(bufimage.getRGB(i, j));
                Color nextColor = new Color(bufimage.getRGB(i, j + 1));

                //refined attempt
                float red = color.getRed();
                red = red / 255;
                float green = color.getGreen();
                green = green / 255;
                float blue = color.getBlue();
                blue = blue /255;

                float nextRed = nextColor.getRed();
                nextRed = nextRed / 255;
                float nextGreen = nextColor.getGreen();
                nextGreen = nextGreen / 255;
                float nextBlue = nextColor.getBlue();
                nextBlue = nextBlue / 255;

                float redContrast = red - nextRed;
                redContrast = Math.abs(redContrast);
                float greenContrast = blue - nextBlue;
                greenContrast = Math.abs(greenContrast);
                float blueContrast = green - nextGreen;
                blueContrast = Math.abs(blueContrast);

                Rcontrast[i][j] = redContrast;
                Gcontrast[i][j] = greenContrast;
                Bcontrast[i][j] = blueContrast;
            }
        }

        //HORIZONTAL CONTRAST
        for(int i = 0; i < bufimage.getHeight(); i++){
            for(int j = 0; j < bufimage.getWidth() - 1; j++){
                Color color = new Color(bufimage.getRGB(j, i));
                Color nextColor = new Color(bufimage.getRGB(j + 1, i));

                //refined attempt
                float red = color.getRed();
                red = red / 255;
                float green = color.getGreen();
                green = green / 255;
                float blue = color.getBlue();
                blue = blue /255;

                float nextRed = nextColor.getRed();
                nextRed = nextRed / 255;
                float nextGreen = nextColor.getGreen();
                nextGreen = nextGreen / 255;
                float nextBlue = nextColor.getBlue();
                nextBlue = nextBlue / 255;

                float redContrast = red - nextRed;
                redContrast = Math.abs(redContrast);
                float greenContrast = blue - nextBlue;
                greenContrast = Math.abs(greenContrast);
                float blueContrast = green - nextGreen;
                blueContrast = Math.abs(blueContrast);

                Rcontrast[j][i] = Rcontrast[j][i] + redContrast;
                Gcontrast[j][i] = Gcontrast[j][i] + greenContrast;
                Bcontrast[j][i] = Bcontrast[j][i] + blueContrast;

                Rcontrast[j][i] = Rcontrast[j][i] /2;
                Gcontrast[j][i] = Gcontrast[j][i] /2;
                Bcontrast[j][i] = Bcontrast[j][i] /2;
            }
        }

        //combine r g b contrasts
        for(int i = 0; i < bufimage.getWidth(); i++) {
            for (int j = 0; j < bufimage.getHeight(); j++) {
                float combinedColour = Rcontrast[i][j] + Gcontrast[i][j] + Bcontrast[i][j];
                combinedColour = combinedColour/3;
                contrast[i][j] = combinedColour;
                Color finalColour = new Color(combinedColour, combinedColour, combinedColour);
                bufimage.setRGB(i, j, finalColour.getRGB());
            }
        }
        ///////////////////////////////////////////////////////////calculate the seams etc

        float[] energies = new float[bufimage.getWidth()];
        for(int i = 0; i < contrast.length; i++) {
            energies[i] = calculateSeam(bufimage, i);
        }


            int lowestEnergyIndex = 0;
            float lowestEnergy = energies[0];
            //System.out.println("length of energies: " + energies.length);

            for(int ind = 0; ind < energies.length; ind++){ //work out the seam of lowest energy
                if(lowestEnergy >= energies[ind]){
                    lowestEnergy = energies[ind];
                    lowestEnergyIndex = ind;
                }
            }
            removeSeam(bufimage, lowestEnergyIndex);
            energies[lowestEnergyIndex] = 2147483647;


        Image image = SwingFXUtils.toFXImage(seamedBufImage, null);
        return image;

    }

    //////////////////////////////////////////////////////////end of main

    public float calculateSeam(BufferedImage bufferedImage, int i){ //run for every horizontal pixel
        float energy = 0;
        int currentColumn;
            currentColumn = i;

            BufferedImage seamedBufImage = bufferedImage; //new
            for(int j = 0; j < bufferedImage.getHeight() - 1; j++) { //j = bottom to top
                float left;
                float middle;
                float right;

                //check left
                if(currentColumn < 1){
                    left = 100;
                }else{
                        left = contrast[currentColumn - 1][j + 1];
                }
                //check middle
                if(currentColumn > contrast.length - 2){
                    middle = 100;
                }else {
                    middle = contrast[currentColumn][j + 1]; //oob!!!!!!!
                }
                //check right
                if(currentColumn > (contrast.length - 2)){
                    right = 100;
                }else{
                    right = contrast[currentColumn + 1][j + 1];
                }

                ///
                if(left<middle && left<right){ //smallest = left;
                    energy = energy + left;
                    //seamedBufImage.setRGB(currentColumn - 1, j + 1, Color.red.getRGB()); //new
                    fullSeams[i][j] = 0;
                    currentColumn = currentColumn - 1;
                }else if(middle<=right && middle<=left){ //smallest = middle;
                    //System.out.println("middle is smallest");
                    energy = energy + middle;
                    fullSeams[i][j] = 1;
                    //seamedBufImage.setRGB(currentColumn, j + 1, Color.red.getRGB()); //new
                }else{ //smallest = right;
                    energy = energy + right;
                    fullSeams[i][j] = 2;
                    //seamedBufImage.setRGB(currentColumn + 1, j + 1, Color.red.getRGB()); //new
                    currentColumn = currentColumn + 1;
                }
            //System.out.println("energy of " + i + " : " + energy);
        }
        return energy;
    }


    public void removeSeam(BufferedImage bufferedImage, int i){
        int currentColumn;
        currentColumn = i;

        for(int j = 0; j < bufferedImage.getHeight() - 1; j++) { //j = bottom to top
            float left;
            float middle;
            float right;

            //check left
            if (currentColumn < 1) {
                left = 100;
            } else {
                left = contrast[currentColumn - 1][j + 1];
            }
            //check middle
            if (currentColumn > contrast.length - 2) {
                middle = 100;
            } else {
                middle = contrast[currentColumn][j + 1]; //oob!!!!!!!
            }
            //check right
            if (currentColumn > (contrast.length - 2)) {
                right = 100;
            } else {
                right = contrast[currentColumn + 1][j + 1];
            }
            /////////////////////

            if (left < middle && left < right) { //smallest = left;
                currentColumn = currentColumn - 1;
            } else if (middle <= right && middle <= left) { //smallest = middle;
            } else { //smallest = right;
                currentColumn = currentColumn + 1;
            }

            removepixel(j + 1, currentColumn);


        }

    }

    public BufferedImage removepixel(int j, int currentColumn) { //these are y and x of pixel to remove
        int curcol = currentColumn;
        for (int index = 0; index < curcol; index++) {
                seamedBufImage.setRGB(index, j, originalImage.getRGB(index, j));
        }
        //after current column
        for (int index = curcol + 1; index < seamedBufImage.getWidth(); index++) {
                seamedBufImage.setRGB(index -1, j, originalImage.getRGB(index, j));
        }
        return  seamedBufImage;
    }

}
