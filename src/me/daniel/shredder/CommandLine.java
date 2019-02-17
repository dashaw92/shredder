package me.daniel.shredder;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import me.daniel.shredder.api.Shredder;

//Runs the shredder via command-line arguments
final class CommandLine {
	
	public static void run(String[] args) {
		File input = null, output = null;
		boolean compress = false;
		int shreds = 3;
		
		//parse the args
		for(String arg : args) {
			switch(arg.toLowerCase()) {
				case "--help":
				case "-h":
					printHelp();
					break;
				case "--compress":
				case "-z":
				case "--zip":
				case "-c":
					compress = true;
					break;
				default:
					//Parse shred count
					if(arg.startsWith("-s=")) {
						shreds = getShreds(arg.split("=")[1]);
						continue;
					}
					
					//Get the input and output files
					File file = getFile(arg);
					System.out.println(file.getAbsolutePath());
					if(!file.exists()) {
						printErrorFile();
					}
					
					//Folders for output, files for input
					if(file.isDirectory()) output = file;
					else input = file;
			}
		}
		
		//Ensure the input and output files are set
		if(input == null || output == null) {
			System.err.println("Missing input or output files. Try running with --help for help.");
			return;
		}
		
		//Do the shredding
		System.out.printf("Shredding file %s with options: Compressed: %s, Shred count: %d\n", input.getAbsolutePath(), compress, shreds);
		Shredder.shredFile(input, compress, shreds);
		System.out.println("Finished shredding. Output files: ");
		Shredder.save(output, input.getName()).stream().map(o -> "->\t" + o).forEach(System.out::println); //print the output files
		System.exit(0); //The program wouldn't exit without this here? I'm confused
	}
	
	//Parse the argument as an integer or print the error for shreds
	private static int getShreds(String in) {
		try {
			return Integer.parseInt(in);
		} catch(NumberFormatException e) {
			printErrorShreds();
			System.exit(-3);
			return -1; //Java is silly
		}
	}
	
	//Print usage message
	private static void printHelp() {
		String[] help = { 
				"File Shredder by dashaw92",
				"Usage:",
				"\tjava -jar Shredder.jar [flags...] <input file> <output folder>",
				"",
				"Flags:",
				"\t--help",
				"\t-h\t Print this help message and quit",
				"",
				"\t--compress",
				"\t-c",
				"\t--zip",
				"\t-z\t Compress the input file",
				"",
				"\t-s=x\t Shred the input into this many shreds. Requires an",
				"\t\t integer following the argument.",
				"",
		};
		Arrays.stream(help).forEach(System.out::println);
		System.exit(0);
	}
	
	//Print the error for missing parameter to argument 
	private static void printErrorShreds() {
		System.err.println("You must pass an integer to the shreds argument.");
		System.exit(-1);
	}
	
	//Print the error for file not existing
	private static void printErrorFile() {
		System.err.println("The input file, output folder, or both do not exist.");
		System.exit(-2);
	}
	
	//Try to return an absolute path, and if that doesn't exist,
	//try relative
	private static File getFile(String arg) {
		File file = new File(arg);
		if(file.exists()) return file;
		return new File(Paths.get("").toAbsolutePath().toFile(), arg);
	}
}
