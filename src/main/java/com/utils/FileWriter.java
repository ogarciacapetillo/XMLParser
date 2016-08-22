package main.java.com.utils;

import static java.nio.file.StandardOpenOption.*;

import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

public class FileWriter {
	protected final String filePath="/kohls/prop/kcm/es/testreport/tctd/json/";
	//protected final String filePath="C:\\temporal\\";
	
	public void writeToFile(String line){
		Set<OpenOption> options = new HashSet<OpenOption>();
		options.add(APPEND);
		options.add(CREATE);
		
		// Create the custom permissions attribute.
		
	    Set<PosixFilePermission> perms =
	      PosixFilePermissions.fromString("rw-r-----");
	    FileAttribute<Set<PosixFilePermission>> attr =
	      PosixFilePermissions.asFileAttribute(perms);
	      

	    byte data[] = line.getBytes();
	    ByteBuffer bb = ByteBuffer.wrap(data);
	    
	    
	    //Path file = Paths.get("./filePath.csv");
	    // Get the job name to be added as part of the file name
	    String jobName = null;
	    jobName = line.substring(0, line.indexOf(':'));	    
	    Path file = Paths.get(filePath+jobName+"_tcDetails.csv");
	    try (SeekableByteChannel sbc =
	      // Windows system
	      //Files.newByteChannel(file, options)) {
	      // UNIX
	      Files.newByteChannel(file, options, attr)) {
	      sbc.write(bb);
	    } catch (IOException x) {
	      System.out.println("Exception thrown: " + x);
	    }
		
	}

	public void writeToFile(String line, String jobName){
		Set<OpenOption> options = new HashSet<OpenOption>();
		options.add(APPEND);
		options.add(CREATE);

		// Create the custom permissions attribute.

		Set<PosixFilePermission> perms =
				PosixFilePermissions.fromString("rw-r-----");
		FileAttribute<Set<PosixFilePermission>> attr =
				PosixFilePermissions.asFileAttribute(perms);


		byte data[] = line.getBytes();
		ByteBuffer bb = ByteBuffer.wrap(data);


		//Path file = Paths.get("./filePath.csv");
		// Get the job name to be added as part of the file name
		String _jobName = jobName;
		Path file = Paths.get(filePath+jobName+"_tcDetails.csv");
		try (SeekableByteChannel sbc =
					 // Windows system
					 //Files.newByteChannel(file, options)) {
					 // UNIX
					 Files.newByteChannel(file, options, attr)) {
			sbc.write(bb);
		} catch (IOException x) {
			System.out.println("Exception thrown: " + x);
		}

	}

}
