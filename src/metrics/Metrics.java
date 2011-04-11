package metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a data class which contains various metrics used for computing
 * animations.
 * 
 * @author Richard Jenkin
 */
public class Metrics {
	private int absStartX;
	private int absStartY;
	private int absEndX;
	private int absEndY;
	private int relCentroidX;
	private int relEccentricityX;
	private int relVelocityX;

	/**
	 * Constructor. Relative metrics all default to 0.
	 * 
	 * @param absStartX
	 *            x-coordinate of top-left corner of bounding box.
	 * @param absStartY
	 *            y-coordinate of top-left corner of bounding box.
	 * @param absEndX
	 *            x-coordinate of bottom-right corner of bounding box.
	 * @param absEndY
	 *            y-coordinate of bottom-right corner of bounding box.
	 */
	public Metrics(int absStartX, int absStartY, int absEndX, int absEndY) {
		this.setAbsStartX(absStartX);
		this.setAbsStartY(absStartY);
		this.setAbsEndX(absEndX);
		this.setAbsEndY(absEndY);
		this.setRelCentroidX(0);
		this.setRelEccentricityX(0);
		this.setRelVelocityX(0);
	}

	public Metrics(String[] data) {
		this.setAbsStartX(Integer.parseInt(data[0]));
		this.setAbsStartY(Integer.parseInt(data[1]));
		this.setAbsEndX(Integer.parseInt(data[2]));
		this.setAbsEndY(Integer.parseInt(data[3]));
		this.setRelCentroidX(Integer.parseInt(data[4]));
		this.setRelEccentricityX(Integer.parseInt(data[5]));
		this.setRelVelocityX(Integer.parseInt(data[6]));
	}

	/**
	 * Get all metrics in the form of a list. Here only for compatibility with
	 * our CSV-writer.
	 * 
	 * @return
	 */
	public List<Integer> getMetrics() {
		List<Integer> metrics = new ArrayList<Integer>(7);
		metrics.add(absStartX);
		metrics.add(absStartY);
		metrics.add(absEndX);
		metrics.add(absEndY);
		metrics.add(relCentroidX);
		metrics.add(relEccentricityX);
		metrics.add(relVelocityX);
		return metrics;
	}

	public int getAbsStartX() {
		return absStartX;
	}

	public int getAbsStartY() {
		return absStartY;
	}

	public int getAbsEndY() {
		return absEndY;
	}

	public int getAbsEndX() {
		return absEndX;
	}

	public int getRelCentroidX() {
		return relCentroidX;
	}

	public int getRelEccentricityX() {
		return relEccentricityX;
	}

	public int getRelVelocityX() {
		return relVelocityX;
	}

	public void setAbsStartX(int absStartX) {
		this.absStartX = absStartX;
	}

	public void setAbsStartY(int absStartY) {
		this.absStartY = absStartY;
	}

	public void setAbsEndY(int absEndY) {
		this.absEndY = absEndY;
	}

	public void setAbsEndX(int absEndX) {
		this.absEndX = absEndX;
	}

	public void setRelCentroidX(int relCentroidX) {
		this.relCentroidX = relCentroidX;
	}

	public void setRelEccentricityX(int relEccentricityX) {
		this.relEccentricityX = relEccentricityX;
	}

	public void setRelVelocityX(int absVelocityX) {
		this.relVelocityX = absVelocityX;
	}

}
