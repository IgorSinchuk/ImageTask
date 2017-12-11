package com.igorsinchuk.imagetask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
       Main compare = new Main("./src/main/resources/Images/img1.png",
               "./src/main/resources/Images/img2.png");
       compare.setParameters(10,10 );
        compare.compare();
        if (!compare.isIdentic()) {
            System.out.println("Images do not match.");
            System.out.println("See the result in the 'result' folder.");
            Main.savePNG(compare.getImageResult(),
                    "./src/main/resources/Result/result.png");
        } else {
            System.out.println("Images are the same");
        }
    }

    private static boolean isIdentic;
    private static int compareX, compareY;
    private static BufferedImage img1, img2, imgRslt;

    private static double sensitivity = 0.10;

    public Main(String file1, String file2) throws IOException {
        img1 = loadPNG(file1);
        img2 = loadPNG(file2);
    }

    public void setParameters(int compareX, int compareY) {
        Main.compareX = compareX;
        Main.compareY = compareY;
    }

    public void compare() {
        imgRslt = new BufferedImage(img2.getWidth(null), img2.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imgRslt.createGraphics();
        g2.drawImage(img2, null, null);
        g2.setColor(Color.RED);
        int blocksX = (int)(img1.getWidth()/compareX);
        int blocksY = (int)(img1.getHeight()/compareY);
        Main.isIdentic = true;
        for (int y = 0; y < compareY; y++) {
            for (int x = 0; x < compareX; x++) {
                int result1 [][] = convertTo2D(img1.getSubimage(x*blocksX, y*blocksY, blocksX - 1, blocksY - 1));
                int result2 [][] = convertTo2D(img2.getSubimage(x*blocksX, y*blocksY, blocksX - 1, blocksY - 1));
                for (int i = 0; i < result1.length; i++) {
                    for (int j = 0; j < result1[0].length; j++) {
                        int diff = Math.abs(result1[i][j] - result2[i][j]);
                        if (diff/Math.abs(result1[i][j]) > sensitivity) {
                            g2.drawRect(x*blocksX, y*blocksY, blocksX - 1, blocksY - 1);
                            isIdentic = false;
                        }
                    }
                }
            }
        }
    }

    public BufferedImage getImageResult() {
        return imgRslt;
    }

    public int[][] convertTo2D(BufferedImage subImage) {
        int width = subImage.getWidth();
        int height = subImage.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = subImage.getRGB(col, row);
            }
        }
        return result;
    }

    public static BufferedImage loadPNG(String filename) throws IOException {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;

    }

    public static void savePNG(BufferedImage bimg, String filename) {
        try {
            File outputfile = new File(filename);
            ImageIO.write(bimg, "png", outputfile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isIdentic() {
        return isIdentic;
    }
}
