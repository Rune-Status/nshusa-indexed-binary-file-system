# Indexed Binary File System (IBFS) ![alt tag](https://travis-ci.org/chadalen/indexed-binary-file-system.svg?branch=master)
Is an in-memory file system that allows you to pack multiple files into a single highly compressed binary file.
### Features
* Super high compression uses LZMA2 algorithm aka [Lempel–Ziv–Markov chain algorithm](https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Markov_chain_algorithm)
* Super fast decompression (Since all the data is in one file, decompression only needs to happen once)
* **96.40%** higher compression than an uncompressed binary file
* **31.51%** higher compression than **GZip**
* **6.23%** higher compresson than **BZip2**

### How it works
The file system stores index files called an **Index**, which can be named. An Index is like a directory they can contain a list of files within it called an **"IndexedFile"**. An IndexedFile contains the actual data of a file. An IndexedFile like an Index can also be named.

* Index ("archive")
   * IndexedFile("settings.dat")
   * IndexedFile("item.dat")
   * IndexedFile("npc.dat")
   * IndexedFile("obj.dat")
   * IndexedFile("idk.dat")
* Index ("model")
   * IndexedFile("1.dat")
   * IndexedFile("2.dat")
* Index ("animation")
   * IndexedFile("1.dat")
   * IndexedFile("2.dat")
* Index ("music")
   * IndexedFile("soundtrack.midi")
   * IndexedFile("themesong.midi")
   * IndexedFile("arena.midi")
   * IndexedFile("shortclip.midi")
* Index ("sound")
   * IndexedFile("whip_attack.wav")
   * IndexedFile("whip_deflect.wav")
   * IndexedFile("humanoid_block.wav")
   * IndexedFile("shield_block.wav")
* Index("textures")
   * IndexedFile("0.dat")
   * IndexedFile("1.dat")
* Index("sprites")
   * IndexedFile("binary_sprites.dat")
   
### Code examples
### Creating an IndexedFileSystem
```java

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
```


### Reading from an encoded IndexedFileSystem
```java

			try(IndexedFileSystem fs = IndexedFileSystem.decode("./cache.dat")) {
			
			System.out.println("There are " + fs.getIndexes().size() + " indexes in this file system\n");
			
			for (Index index : fs.getIndexes()) {
				System.out.println(String.format("index=[%d, %s]", index.getId(), index.getName()));				
				index.getFiles().stream().forEach(it -> System.out.println(String.format("\tfile=[%d, %s]", it.getHeader().getId(), it.getHeader().getName())));
			}
			
		}
```

### Libraries used
* [Commons Compress 1.13](https://mvnrepository.com/artifact/org.apache.commons/commons-compress)
* [XZ Utils 1.6](https://mvnrepository.com/artifact/org.tukaani/xz)



