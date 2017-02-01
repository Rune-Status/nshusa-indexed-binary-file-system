package com.softgate.test;
import java.io.File;
import java.io.IOException;

import com.softgate.fs.Index;
import com.softgate.fs.IndexedFileSystem;

public class App {

	public static void main(String[] args) throws Exception {	
		create();
		
		decode();
	}
	
	public static void decode() throws IOException {
		try(IndexedFileSystem fs = IndexedFileSystem.decode("./cache.dat")) {
			
			System.out.println("There are " + fs.getIndexes().size() + " indexes in this file system\n");
			
			for (Index index : fs.getIndexes()) {
				System.out.println(String.format("index=[%d, %s]", index.getId(), index.getName()));				
				index.getFiles().stream().forEach(it -> System.out.println(String.format("\tfile=[%d, %s]", it.getHeader().getId(), it.getHeader().getName())));
			}
			
		}
	}
	
	public static void create() throws IOException {
		try(IndexedFileSystem fs = IndexedFileSystem.create()) {

			fs.add(Index.create(0, "settings"))
			.add("item.dat", "data inside item.dat".getBytes())
			.add("npc.dat", "data inside npc.dat".getBytes())
			.add("obj.dat", "data inside obj.dat".getBytes());
			
			fs.add(Index.create(1, "model"))
			.add(0, "0.dat")
			.add(1, "1.dat")
			.add(2, "2.dat");
			
			fs.add(Index.create(2, "animation"))
			.add(0, "0.dat")
			.add(1, "1.dat")
			.add(2, "2.dat");
			
			fs.add(Index.create(3, "music"))
			.add("soundtrack.midi")
			.add("login_music.midi");
			
			fs.add(Index.create(4, "map"))
			.add(0, "0.dat")
			.add(1, "1.dat")
			.add(2, "2.dat");
			
			fs.add(Index.create(5, "sound"))
			.add("low_alch.wav")
			.add("high_alch.wav")
			.add("telekenitic_grab.wav")
			.add("entangle.wav");
			
			fs.add(Index.create(6, "sprites"))
			.add("sprites.dat");
			
			fs.write(new File("./cache.dat"));
			
		}	
		
	}
	

}
