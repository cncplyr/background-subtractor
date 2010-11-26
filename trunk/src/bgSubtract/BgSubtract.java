package bgSubtract;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BgSubtract {

	public static void main(String[] args) {
		BufferedImage bgImg = null;
		try {
			bgImg = ImageIO.read(new File("input1.jpg"));
			System.out.println("File found!");
		} catch (IOException e) {
			System.out.println("File not found!");
		}

		if (bgImg != null) {
			System.out.println("Editing file...");
			saveImage(vflipImage(bgImg));
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

	public static BufferedImage removeBackground(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// do something here
			}
		}

		return newImage;
	}

	public static void saveImage(BufferedImage img) {
		File saveFile = new File("output1.jpg");
		try {
			ImageIO.write(img, "jpg", saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
