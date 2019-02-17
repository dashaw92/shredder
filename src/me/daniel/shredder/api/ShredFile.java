package me.daniel.shredder.api;

/**
 * Represents one shred of a file.
 * See {@link Shredder} for information
 */
final class ShredFile {
	
	private byte[] buffer = new byte[16]; //The buffer for this shred.
	private int offset = 0; //Where we are in the buffer
	
	/**
	 * Pushes a byte into the buffer of this shred.
	 * If this push would exceed the size of the backing buffer,
	 * the buffer is expanded by a factor of 2.
	 * @param value The byte to push
	 */
	protected void push(byte value) {
		//Check if the buffer needs to be reallocated.
		if(offset >= buffer.length) {
			byte[] copy = buffer;
			buffer = new byte[copy.length * 2];
			System.arraycopy(copy, 0, buffer, 0, offset);
		}
		
		//Set the current offset in the buffer to `value` and increment
		buffer[offset++] = value;
	}
	
	/**
	 * Fit the backing buffer of this shred to the minimal size required.
	 * Call this before saving.
	 * @return This shred's buffer shrunk to fit the data contained.
	 */
	protected byte[] shrink() {
		byte[] fit = new byte[offset];
		System.arraycopy(buffer, 0, fit, 0, offset);
		return fit;
	}
	
}
