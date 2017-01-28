package com.softgate.test;
import java.io.IOException;

import com.softgate.fs.Index;
import com.softgate.fs.IndexedFileSystem;

public class App {

	public static void main(String[] args) throws Exception {
		encode();
		
		decode();	
	}
	
	public static void encode() throws Exception {
		try(IndexedFileSystem fs = IndexedFileSystem.create()) {
			
		}
	}
	
	public static void decode() throws IOException {
		try(IndexedFileSystem fs = IndexedFileSystem.decode("./cache.dat")) {

			System.out.println("There are " + fs.getFiles().size() + " indexes in this file system\n");
			
			for (Index index : fs.getFiles()) {
				System.out.println(String.format("store: id=[%d] name=[%s]", index.getId(), index.getName()));
				
				index.getFiles().forEach(it -> System.out.println("\t" + new String(it.getData())));
			}
		}
	}

}
