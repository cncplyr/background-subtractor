# Overview #
Takes frames in the form of .jpg or .png, and some background frames. It then subtracts the background pixels from the frames, and saves the new frames. Also generates a set of metrics taken from the image for analysis, and saves them as a .csv file.

## Readme ##
  1. Place the JAR file into a folder for your current project.
  1. Put the image files into a sub-folder called 'input'.
  1. If you have additional images to use for background-image creation, put these into a separate sub-folder called 'input-bg'.
  1. Create another sub-folder called 'output'.
  1. Run the background-subtraction by using the command 'java -jar background-subtractor-v1.jar'. This will output the images in the folder output/p1 and output/p2, along with the image metrics in output/metrics.csv.