package maths;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author cncplyr
 * @version 0.2
 */
public class AverageFinder {

	public int findMedian(List<Integer> numbers) {
		if (numbers.size() < 2) {
			throw new IllegalArgumentException(
					"Cannot find average(median) of less than 2 numbers!");
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

	public int findMean(List<Integer> numbers) {
		if (numbers.size() < 1) {
			throw new IllegalArgumentException(
					"Cannot find average(mean) of an empty list of numbers!");
		}
		int mean = 0;
		for (int number : numbers) {
			mean += number;
		}
		return mean / numbers.size();
	}

	public int findMode(List<Integer> numbers) {
		if (numbers.size() < 1) {
			throw new IllegalArgumentException(
					"Cannot find average(mode) of an empty list of numbers!");
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
