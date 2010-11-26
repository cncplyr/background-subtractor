package bgSubtract;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BgSubtract {
	private static BufferedImage bgImg = null;
	private static BufferedImage currentImg = null;

	public static void main(String[] args) {
		try {
			BufferedImage bgImg1 = ImageIO.read(new File("background1.jpg"));
			BufferedImage bgImg2 = ImageIO.read(new File("background2.jpg"));
			BufferedImage[] zomg = {bgImg1, bgImg2};
			bgImg = combineImages(zomg);
		} catch (IOException e) {
			System.out.println("Background image not found.");
		}
		try {
			currentImg = ImageIO.read(new File("input1.jpg"));
		} catch (IOException e) {
			System.out.println("Input image not found!");
		}

		if(currentImg.getWidth() != bgImg.getWidth()){
			
		}
		
		if (currentImg != null && bgImg != null) {
			System.out.println("Editing file...");
			saveImage(removeBackground(currentImg), "output1");
			System.out.println("File saved!");
		}
	}

	public static BufferedImage vflipImage(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				newImage.setRGB(x, height - y - 1, img.getRGB(x, y));
			}
		}
		return newImage;
	}

	public static BufferedImage hflipImage(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				newImage.setRGB(width - x - 1, y, img.getRGB(x, y));
			}
		}
		return newImage;
	}

	public static BufferedImage combineImages(BufferedImage[] img) {
		int height0 = img[0].getHeight();
		int width0 = img[0].getWidth();

		BufferedImage newImage = new BufferedImage(width0, height0,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width0; x++) {
			for (int y = 0; y < height0; y++) {
				Color c0 = new Color(img[0].getRGB(x, y));
				Color c1 = new Color(img[1].getRGB(x, y));		
				
				int red = (c0.getRed() + c1.getRed())/2;
				int green = (c0.getGreen() + c1.getGreen())/2;
				int blue = (c0.getBlue() + c1.getBlue())/2;
				
				Color blah = new Color(red, green, blue);
				
				// for crazy colours, jsut do newImage.setRGB(x, y, img[0].getRGB - img[1].getRGB);
				newImage.setRGB(x, y, blah.getRGB());
			}
		}
		saveImage(newImage, "newbackground");
		return newImage;
	}
	
	public static BufferedImage removeBackground(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color bgColour = new Color(bgImg.getRGB(x, y));
				Color imgColour = new Color(img.getRGB(x, y));

				// newImage.setRGB(x, y, imgColour - bgColour);

				if ((imgColour.getRed() >= bgColour.getRed() - 1 && imgColour.getRed() <= bgColour.getRed() + 1)
					&& (imgColour.getGreen() >= bgColour.getGreen() - 1 && imgColour.getGreen() <= bgColour.getGreen() + 1)
					&& (imgColour.getBlue() >= bgColour.getBlue() - 1 && imgColour.getBlue() <= bgColour.getBlue() + 1)
						) {
					// if colours match, remove it
					newImage.setRGB(x, y, 0);
				} else {
					// else copy it over
					newImage.setRGB(x, y, imgColour.getRGB());
				}
			}
		}

		return newImage;
	}

	public static void saveImage(BufferedImage img, String name) {
		File saveFile = new File(name + ".jpg");
		try {
			ImageIO.write(img, "jpg", saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
