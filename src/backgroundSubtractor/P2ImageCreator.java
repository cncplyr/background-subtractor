package backgroundSubtractor;

import imageProcessing.ImageMirror;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import fileHandling.FileHandler;

public class P2ImageCreator {
	FileHandler fh;
	ImageMirror im;

	public P2ImageCreator() {
		fh = new FileHandler();
		fh.setInputFolder("output" + File.separator + "p1");
		fh.setOutputFolder("output" + File.separator + "p2");
		im = new ImageMirror();
	}

	public void generateImages() {
		List<String> frames = fh.getAllImageNamesMatching("frame");

		for (String frame : frames) {
			BufferedImage currentFrame = fh.loadImage(frame);
			BufferedImage mirroredImage = im.genFrame(currentFrame);
			fh.saveImage(mirroredImage, frame);
		}
	}
}
