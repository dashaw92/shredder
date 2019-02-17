package me.daniel.shredder.api;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Manages the process of shredding a file.
 * Contains an array of {@link ShredFile} to store intermediate data.
 */
final class Manager {
	
	private final ShredFile[] shreds; //Intermediate shredded data
	private final byte[] input_data; //The bytes from the input file
	
	/**
	 * Constructs a Manager object that will shred the data.
	 * @param count How many shreds to create
	 * @param input_data The bytes to shred 
	 */
	protected Manager(int count, byte[] input_data) {
		Objects.requireNonNull(input_data);
		this.input_data = input_data;

		if(count <= 0 || count >= input_data.length) count = 3; //Default to 3 shreds in case count is invalid
		
		//Construct our ShredFile array
		shreds = new ShredFile[count];
		IntStream.range(0, count).forEach(i -> shreds[i] = new ShredFile());
	}
	
	/**
	 * Proceed to shred the data.
	 */
	protected void shred() {
		for(int i = 0; i < input_data.length; i++) {
			//This will cycle from 0 to the length of the
			//shreds array until we reach the end of the
			//input data.
			shreds[i % shreds.length].push(input_data[i]);
			//This results in each shred containing only
			//certain multiples of bytes from the original
			//array.
		}
	}
	
	/**
	 * Converts the shredded files into byte arrays, ready for writing to files.
	 * This should be the final method called in the process of shredding.
	 * @return The shreds as byte arrays
	 */
	protected List<byte[]> dump() {
		return Arrays.stream(shreds).map(ShredFile::shrink).collect(Collectors.toList());
	}
}
