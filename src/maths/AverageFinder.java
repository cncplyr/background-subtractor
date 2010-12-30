package maths;

import java.util.Collections;
import java.util.List;

public class AverageFinder {

	public int findMedian(List<Integer> numbers) {
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
		int mean = 0;
		for (int number : numbers) {
			mean += number;
		}
		return mean / numbers.size();
	}
	
	public int findMode(List<Integer> numbers){
		// TODO: Can't be bothered...
		return 0;
	}

}
