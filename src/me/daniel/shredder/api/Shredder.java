package me.daniel.shredder.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Oversees the process of splitting ("shredding") one file into many other files ("shreds").<br>
 * Shredding a file means taking the input file and extracting multiples of bytes into each<br>
 * shred.<br>
 * <br>
 * Given an example file with the string "Hello world", shredding this file into 2 shreds would<br>
 * result in the following files and contents:<br>
 * 
 * Shred 1: "Hlowrd"<br>
 * Shred 2: "el ol"<br>
 * 
 * The more shreds there are, the more obscure each shred will be. The minimum data per shred is<br>
 * one byte.<br>
 * <br>
 * Optionally, zip compression may be applied to the data (see {@link Shredder#shredFile(File, boolean, int)}<br>
 */
public final class Shredder {
	
	private static Manager mgr; //Internal state, used for storing the data between shredding and saving.
	
	/**
	 * Shred a file into {@code count} shreds
	 * @param input The input file to shred
	 * @param compress Should the file be compressed (zipped) before being shredded?
	 * @param count The amount of shreds to create. The more shreds, the more secure.
	 * @return The list of shreds in order
	 * @throws IOException 
	 */
	public static void shredFile(File input, boolean compress, int count) throws IOException {
		byte[] in_data = Files.readAllBytes(input.toPath());
		byte[] obf_data = in_data;
		if(compress) obf_data = zip(in_data);
		mgr = new Manager(count, obf_data);
		mgr.shred();
	}
	
	/**
	 * Saves the shredded file
	 * @param directory The directory to save the shreds to
	 * @param prefix Used in naming
	 * @return The list of files saved as a result of this method
	 */
	public static List<String> save(File directory, String prefix) {
		List<String> outputs = new ArrayList<>();
		
		//make sure the directory exists and other checks
		if(!directory.exists() || directory.isFile() || !directory.canWrite()) return outputs;
		
		//convert the shreds to byte arrays
		List<byte[]> shreds = mgr.dump();
		
		//write each index of the shreds list to an individual file
		for(int i = 0; i < shreds.size(); i++) {
			File out = new File(directory, prefix + "." + i + ".shred");
			
			writeFile(out, shreds.get(i));
			outputs.add(out.getAbsolutePath());
		}
		
		//return the list of file names saved
		return outputs;
	}
	
	//Helper method to save files
	private static void writeFile(File out, byte[] data) {
		try(FileOutputStream writer = new FileOutputStream(out)) {
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Compresses the input byte array using Zip compression
	private static byte[] zip(byte[] in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try(ZipOutputStream ozip = new ZipOutputStream(out)) {
			ZipEntry file = new ZipEntry("file");
			ozip.putNextEntry(file);
			ozip.write(in);
			ozip.closeEntry();
			
			//Add a little credit to zip files
			ZipEntry magic = new ZipEntry("readme.txt");
			ozip.putNextEntry(magic);
			ozip.write("Shredded by File Shredder 1.0 by dashaw92\nHello world!".getBytes());
			ozip.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
}
