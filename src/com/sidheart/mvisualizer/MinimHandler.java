package com.sidheart.mvisualizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MinimHandler {
	public String sketchPath(String fileName) {
		return fileName;
	}

	public InputStream createInput( String fileName ) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
		} catch(IOException e) {
			System.out.println("Error creating file stream");
		}
		return in;
	}
}
