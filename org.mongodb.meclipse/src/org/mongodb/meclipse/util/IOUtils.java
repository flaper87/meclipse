package org.mongodb.meclipse.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 
 * @author Naoki Takezoe
 */
public class IOUtils {
	
	public static String readFile(File file) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] buf = new byte[in.available()];
			in.read(buf);
			return new String(buf, "UTF-8");
		} finally {
			closeQuietly(in);
		}
	}
	
	public static void closeQuietly(Closeable closeable){
		if(closeable != null){
			try {
				closeable.close();
			} catch(Exception ex){
			}
		}
	}
	
}
