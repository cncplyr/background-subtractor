NOTE: This file is out of date!!! - 24/1/2011

# Introduction #

This program will take a set of background images, and a set of foreground images, and remove the background from the foreground images, leaving transparent alpha behind.

If the input image files are not in the correct place, or referenced properly, the program will fail. You have been warned!!

# Usage #
Background images must be called "backgroundxxxxx.png" where xxxxx is the id number, starting from 00000, with 5 digits total (leading zeros).

Foreground images must be called "imagexxxxx.png" where xxxxx is the id number, starting from 00000, with 5 digits total (leading zeros).

All input images must be in a subfolder called "input", unless modified.

All output images will be placed in a subfolder called "output", unless modified.

# Arguments #
|Argument|Result|
|:-------|:-----|
|-outFormat| Image Output format: png OR jpg|
|-inFolder| Image Input folder override<br>
<tr><td>-outFolderImage</td><td> Output folder override<br></td></tr>
<tr><td>-threshold</td><td> Threshold of colours for subtraction<br></td></tr>
<tr><td>-blurRadius</td><td> Radius of Average Blur applied<br></td></tr>
<tr><td>--help  </td><td> Displays this message<br></td></tr>