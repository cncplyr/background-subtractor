package backgroundSubtractor;

import imageProcessing.ImageMirror;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import fileHandling.FileHandler;

public class P2ImageCreator {
	private FileHandler fh;
	private ImageMirror im;

	public P2ImageCreator(FileHandler fh) {
		this.fh = fh;
		im = new ImageMirror();
	}

	public void generateImages() {
		fh.setInputFolder("output" + File.separator + "p1");
		fh.setOutputFolder("output" + File.separator + "p2");

		List<String> frames = fh.getAllImageNamesMatching("frame");

		for (String frame : frames) {
			BufferedImage currentFrame = fh.loadImage(frame);
			BufferedImage mirroredImage = im.genFrame(currentFrame);
			fh.saveImage(mirroredImage, frame.substring(0, frame.length() - 4));
		}
	}
}
