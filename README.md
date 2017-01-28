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

* Index ("configs")
   * IndexedFile("settings.dat")
   * IndexedFile("item.dat")
   * IndexedFile("npc.dat")
   * IndexedFile("obj.dat")
   * IndexedFile("idk.dat")
* Index ("model")
   * IndexedFile("1.dat")
   * IndexedFile("2.dat")
   * IndexedFile("3.dat")
   * IndexedFile("4.dat")
* Index ("animation")
    * IndexedFile("1.dat")
   * IndexedFile("2.dat")
   * IndexedFile("3.dat")
   * IndexedFile("4.dat")
* Index ("music")
   * IndexedFile("soundtrack.midi")
   * IndexedFile("themesong.midi")
   * IndexedFile("arena.midi")
   * IndexedFile("shortclip.midi")
* Index ("sound .wav")
   * IndexedFile("whip_attack.wav")
   * IndexedFile("whip_deflect.wav")
   * IndexedFile("humanoid_block.wav")
   * IndexedFile("shield_block.wav")
* Index("textures")
    * IndexedFile("0.dat")
    * IndexedFile("1.dat")
    * IndexedFile("2.dat")
* Index("fonts")
    * IndexedFile("default.ttf")
    * IndexedFile("calibri.ttf")
* Index("sprites")
    * IndexedFile("binary_sprites.dat")

### Libraries used
* [Commons Compress 1.13](https://mvnrepository.com/artifact/org.apache.commons/commons-compress)
* [XZ Utils 1.6](https://mvnrepository.com/artifact/org.tukaani/xz)



