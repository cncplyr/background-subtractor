package bgSubtract;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BgSubtract {
	private static BufferedImage bgImg = null;
	private static BufferedImage currentImg = null;
	private static int threshold = 10;

	public static void main(String[] args) {
		try {
			BufferedImage bgImg1 = averageBlur(ImageIO.read(new File(
					"background1.jpg")));
			BufferedImage bgImg2 = averageBlur(ImageIO.read(new File(
					"background2.jpg")));
			BufferedImage[] zomg = { bgImg1, bgImg2 };
			bgImg = combineImages(zomg);
		} catch (IOException e) {
			System.out.println("Background image not found.");
		}
		try {
			currentImg = averageBlur(ImageIO.read(new File("input1.jpg")));
		} catch (IOException e) {
			System.out.println("Input image not found!");
		}

		if (currentImg != null && bgImg != null) {
			if (currentImg.getWidth() != bgImg.getWidth()) {
				System.out.println("something went wrong");
				// TODO: throw an exception
			}
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

				int red = (c0.getRed() + c1.getRed()) / 2;
				int green = (c0.getGreen() + c1.getGreen()) / 2;
				int blue = (c0.getBlue() + c1.getBlue()) / 2;

				Color blah = new Color(red, green, blue);

				// for crazy colours, jsut do newImage.setRGB(x, y,
				// img[0].getRGB - img[1].getRGB);
				newImage.setRGB(x, y, blah.getRGB());
			}
		}
		saveImage(newImage, "newbackground");
		return newImage;
	}

	public static BufferedImage averageBlur(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Color[] colours = new Color[25];
		int red, green, blue;

		for (int x = 2; x < width - 2; x++) {
			for (int y = 2; y < height - 2; y++) {
				// reset colours
				red = 0;
				green = 0;
				blue = 0;

				// top row
				colours[0] = new Color(img.getRGB(x - 2, y - 2));
				colours[1] = new Color(img.getRGB(x - 1, y - 2));
				colours[2] = new Color(img.getRGB(x, y - 2));
				colours[3] = new Color(img.getRGB(x + 1, y - 2));
				colours[4] = new Color(img.getRGB(x + 2, y - 2));
				// 2nd row
				colours[5] = new Color(img.getRGB(x - 2, y - 1));
				colours[6] = new Color(img.getRGB(x - 1, y - 1));
				colours[7] = new Color(img.getRGB(x, y - 1));
				colours[8] = new Color(img.getRGB(x + 1, y - 1));
				colours[9] = new Color(img.getRGB(x + 2, y - 1));
				// middle row
				colours[10] = new Color(img.getRGB(x - 2, y));
				colours[11] = new Color(img.getRGB(x - 1, y));
				colours[12] = new Color(img.getRGB(x, y));
				colours[13] = new Color(img.getRGB(x + 1, y));
				colours[14] = new Color(img.getRGB(x + 2, y));
				// 4th row
				colours[15] = new Color(img.getRGB(x - 2, y + 1));
				colours[16] = new Color(img.getRGB(x - 1, y + 1));
				colours[17] = new Color(img.getRGB(x, y + 1));
				colours[18] = new Color(img.getRGB(x + 1, y + 1));
				colours[19] = new Color(img.getRGB(x + 2, y + 1));
				// 5th row
				colours[20] = new Color(img.getRGB(x - 2, y + 2));
				colours[21] = new Color(img.getRGB(x - 1, y + 2));
				colours[22] = new Color(img.getRGB(x, y + 2));
				colours[23] = new Color(img.getRGB(x + 1, y + 2));
				colours[24] = new Color(img.getRGB(x + 2, y + 2));

				// get average colour
				for (Color cellColour : colours) {
					red += cellColour.getRed();
					green += cellColour.getGreen();
					blue += cellColour.getBlue();
				}
				red = red / 25;
				green = green / 25;
				blue = blue / 25;
				Color newColour = new Color(red, green, blue);

				newImage.setRGB(x, y, newColour.getRGB());
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
				Color bgColour = new Color(bgImg.getRGB(x, y));
				Color imgColour = new Color(img.getRGB(x, y));

				// newImage.setRGB(x, y, imgColour - bgColour);

				// int red = imgColour.getRed() - bgColour.getRed();
				// int green = imgColour.getGreen() - bgColour.getGreen();
				// int blue = imgColour.getBlue() - bgColour.getBlue();
				//
				// Color argh = new
				// Color(fixColoursInRange(red),fixColoursInRange(green),fixColoursInRange(blue));
				//
				// newImage.setRGB(x, y, argh.getRGB());

				if ((imgColour.getRed() >= bgColour.getRed() - threshold && imgColour
						.getRed() <= bgColour.getRed() + threshold)
						&& (imgColour.getGreen() >= bgColour.getGreen()
								- threshold && imgColour.getGreen() <= bgColour
								.getGreen() + threshold)
						&& (imgColour.getBlue() >= bgColour.getBlue()
								- threshold && imgColour.getBlue() <= bgColour
								.getBlue() + threshold)) {
					// if colours match, remove it
					int red = imgColour.getRed() - bgColour.getRed();
					int green = imgColour.getGreen() - bgColour.getGreen();
					int blue = imgColour.getBlue() - bgColour.getBlue();

					Color argh = new Color(fixColoursInRange(red),
							fixColoursInRange(green), fixColoursInRange(blue));

					newImage.setRGB(x, y, argh.getRGB());
					// newImage.setRGB(x, y, 0);
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

	public static int fixColoursInRange(int colour) {
		if (colour < 0) {
			colour = 0;
		}
		if (colour > 255) {
			colour = 255;
		}
		return colour;
	}
}
