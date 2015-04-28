package com.raymcbride.neural;

import java.io.*;
import java.util.*;

/**
 * The OutputFile class creates log files
 *
 * @author Ray McBride
 */
public class OutputFile{

	private PrintWriter printWriter;

	/**
	 * Constructor for the <code>OutputFile</code>
	 *
	 * @throws IOException e
	 */
	public OutputFile(String fileName){
		try{
	    	printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
	    }
		catch(IOException e){
			System.out.println(e.toString());
		}
	}

	/**
	 * Writes data to the file
	 *
	 * @param data The data to be written
	 */
	public void writeToFile(String data){
		printWriter.println(data);
	}

	/**
	 * Closes the file
	 */
	public void closeFile(){
		printWriter.close();
    }
}