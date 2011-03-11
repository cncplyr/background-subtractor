package maths;

import java.util.Collections;
import java.util.List;

/**
 * Provides methods to find different types of averages.
 * 
 * @author cncplyr
 * @version 0.3
 */
public class AverageFinder {

	/**
	 * Finds the median integer.
	 * 
	 * @param numbers
	 *            A <code>List</code> of <code>Integer</code>s.
	 * @return The middle number if there are an odd number of input integers,
	 *         otherwise the sum of the two middle integers divided by two.
	 */
	public int findMedian(List<Integer> numbers) {
		if (numbers.size() < 2) {
			throw new IllegalArgumentException("Cannot find average(median) of less than 2 numbers!");
		}
		int size = numbers.size();
		Collections.sort(numbers);
		if (size % 2 == 1) {
			// odd
			return numbers.get((size / 2) + 1);
		} else {
			// even
			return ((numbers.get(size / 2) + numbers.get((size / 2) + 1)) / 2);
		}
	}

	/**
	 * Finds the mean integer.
	 * 
	 * @param numbers
	 *            A <code>List</code> of <code>Integer</code>s.
	 * @return The mean integer, i.e. the sum of the input integers divided by
	 *         the number of input integers.
	 */
	public int findMean(List<Integer> numbers) {
		if (numbers.size() < 1) {
			throw new IllegalArgumentException("Cannot find average(mean) of an empty list of numbers!");
		}
		int mean = 0;
		for (int number : numbers) {
			mean += number;
		}
		return mean / numbers.size();
	}

	/**
	 * Finds the mode integer.
	 * 
	 * @param numbers
	 *            A <code>List</code> of <code>Integer</code>s.
	 * @return The mode integer.
	 */
	public int findMode(List<Integer> numbers) {
		if (numbers.size() < 1) {
			throw new IllegalArgumentException("Cannot find average(mode) of an empty list of numbers!");
		}
		Collections.sort(numbers);
		int currentMode = 0;
		int currentModeCount = 0;
		int previous = 0; // may cause a problem

		for (int currentNumber : numbers) {
			if (currentNumber != previous) {
				int frequency = Collections.frequency(numbers, currentNumber);
				if (frequency > currentModeCount) {
					currentMode = currentNumber;
					currentModeCount = frequency;
				}
				previous = currentNumber;
			}
		}
		return currentMode;
	}
}