package com.softgate.fs;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a wrapper for a slot in an array.
 * 
 * @author Chad Adams
 */
public class Index {
	
	/**
	 * The collection of files in this index.
	 */
	private final List<IndexedFile> files = new ArrayList<>();
	
	/**
	 * The identifier of this index.
	 */
	private int id;
	
	/**
	 * The name of this index.
	 */
	private String name;
	
	/**
	 * The private constructor to prevent instantiation.
	 */
	private Index() {
	}
	
	/**
	 * The method that will copy an existing {@link Index}.
	 * 
	 * @return The copied {@link Index}.
	 */
	public Index copy() {
		Index copy = Index.create(id, name);		
		
		files.forEach(it -> copy.getFiles().add(it));
		
		return copy;
	}
	
	/**
	 * The method to create an {@link Index}.
	 * 
	 * @param id
	 * 		The id of the index to create.
	 * 
	 * @return The created index.
	 */
	public static Index create(int id) {		
		Index idx = new Index();
		idx.id = id;
		idx.name = "index" + id;
		return idx;
	}
	
	/**
	 * The method to create an {@link Index}.
	 * 
	 * @param id
	 * 		The id of the index to create.
	 * 
	 * @param name
	 * 		The name of the index to create.
	 * 
	 * @return The created index.
	 */
	public static Index create(int id, String name) {
		Index idx = new Index();
		idx.id = id;
		idx.name = name;
		return idx;
	}
	
	/**
	 * The method to create an {@link Index}.
	 * 
	 * @param id
	 * 		The id of the index to create.
	 * 
	 * @param file
	 * 		The file to add to this index.
	 * 
	 * @return The created index.
	 */
	public static Index create(int id, IndexedFile file) {
		Index idx = new Index();
		idx.files.add(file);
		idx.id = id;
		idx.name = "index" + idx.id;
		return idx;
	}
	
	/**
	 * The method to create an {@link Index}.
	 * 
	 * @param id
	 * 		The id of the index to create.
	 * 
	 * @param name
	 * 		The name of the index to create.
	 * 
	 * @param file
	 * 		The file to add to this index.
	 * 
	 * @return The created index.
	 */
	public static Index create(int id, String name, IndexedFile file) {
		Index idx = new Index();
		idx.files.add(file);
		idx.id = id;
		idx.name = name;
		return idx;
	}
	
	/**
	 * The method to create an {@link Index}.
	 * 
	 * @param id
	 * 		The id of the index to create.
	 * 
	 * @param files
	 * 		The array of files to add to this index.
	 * 
	 * @return The created index.
	 */
	public static Index create(int id, IndexedFile... files) {
		Index idx = new Index();
		idx.files.addAll(Arrays.asList(files));
		idx.id = id;
		idx.name = "index" + idx.id;
		return idx;
	}
	
	/**
	 * The method to create an {@link Index}.
	 * 
	 * @param id
	 * 		The id of the index to create.
	 * 
	 * @param name
	 * 		The name of the index to create.
	 * 
	 * @param files
	 * 		The array of files to add to this index.
	 * 
	 * @return The created index.
	 */
	public static Index create(int id, String name, IndexedFile... files) {
		Index idx = new Index();
		idx.files.addAll(Arrays.asList(files));
		idx.id = id;
		idx.name = name;
		return idx;
	}
	
	/**
	 * The method that adds a {@link File}'s data to this index.
	 * 
	 * @param file
	 * 		The file to add.
	 * 
	 * @throws IOException
	 * 		The exception thrown if the file does not exist, or the bytes could not be read.
	 * 
	 * @return The index the file was added to.
	 */
	public Index add(File file) throws IOException {
		return add(file.getName(), Files.readAllBytes(file.toPath()));
	}
	
	/**
	 * The method that adds an in-memory data file with no payload to a specified {@link Index}.
	 * 
	 * @param name
	 * 		The name of the file to add.
	 * 
	 * @return The index of the data that was added.
	 */
	public Index add(String name) {
		return add(name, new byte[1]);
	}
	
	/**
	 * The method that adds an in-memory data file with no payload to a specified {@link Index}.
	 * 
	 * @param id
	 * 		The id of the file to add.
	 * 
	 * @param name
	 * 		The name of the file to add.
	 * 
	 * @return The index of the data that was added.
	 */
	public Index add(int id, String name) {
		return add(id, name, new byte[1]);
	}
	
	/**
	 * The method that adds an in-memory data file to a specified {@link Index}.
	 * 
	 * @param name
	 * 		The name of the file to add.
	 * 
	 * @param data
	 * 		The data to add.
	 * 
	 * @return The index of the data that was added.
	 */
	public Index add(String name, byte[] data) {
		return add(files.size(), name, data);
	}
	
	/**
	 * The method that adds an in-memory data file to a specified {@link Index}.
	 * 
	 * @param id
	 * 		The id of the file to add.
	 * 
	 * @param name
	 * 		The name of the file to add.
	 * 
	 * @param data
	 * 		The data to add.
	 * 
	 * @return The index of the data that was added.
	 */
	public Index add(int id, String name, byte[] data) {
		
		if (files.isEmpty()) {
			files.add(new IndexedFile(new IndexedFileHeader(0, name), data));
			return this;
		}
		
		files.add(id, new IndexedFile(new IndexedFileHeader(id, name), data));
		return this;
	}
	
	/**
	 * The method that removes a file in this index with the given id.
	 * 
	 * @param id
	 * 		The id of the file to remove.
	 */
	public void remove(int id) {		
		files.stream().filter(it -> it.getHeader().getId() == id).findFirst().ifPresent(it -> files.remove(it.getHeader().getId()));		
	}
	
	/**
	 * The method that removes a file in this index with the given name.
	 * 
	 * @param name
	 * 		The name of the file to remove.
	 */
	public void remove(String name) {
		files.stream().filter(it -> it.getHeader().getName().equalsIgnoreCase(name)).findFirst().ifPresent(it -> files.remove(it.getHeader().getId()));		
	}
	
	/**
	 * The method that replaces an in-memory data file with the specified file id.
	 * 
	 * @param id
	 * 		The id of the file to replace.
	 * 
	 * @param file
	 * 		The file to replace with.
	 * 
	 * @throws IOException
	 * 	The exception thrown if the file does not exist.
	 * 
	 */
	public void replace(int id, File file) throws IOException {		
		replace(id, Files.readAllBytes(file.toPath()));
	}
	
	/**
	 * The method that replaces an in-memory data file with the specified file name.
	 * 
	 * @param name
	 * 		The name of the file to replace.
	 * 
	 * @param file
	 * 		The file to replace with.
	 * 
	 * @throws IOException
	 * 	The exception thrown if the file does not exist.
	 * 
	 */
	public void replace(String name, File file) throws IOException {
		replace(name, Files.readAllBytes(file.toPath()));
	}
	
	/**
	 * The method that replaces an in-memory data file with the specified file name.
	 * 
	 * @param name
	 * 		The name of the file to replace.
	 * 
	 * @param data
	 * 		The data to replace with.
	 * 
	 */
	public void replace(String name, byte[] data) {
		files.stream().filter(it -> it.getHeader().getName().equals(name)).findFirst().ifPresent(it -> it.setPayload(data));
	}
	
	/**
	 * The method that replaces an in-memory data file with the specified file id.
	 * 
	 * @param id
	 * 		The id of the file to replace.
	 * 
	 * @param data
	 * 		The data to replace with.
	 * 
	 */
	public void replace(int id, byte[] data) {
		files.stream().filter(it -> it.getHeader().getId() == id).findFirst().ifPresent(it -> it.getHeader().setId(id));
	}	
	
	/**
	 * Gets the id of this index.
	 * 
	 * @return The id of this index.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name of this index.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}	

	/**
	 * Sets the id of this index.
	 * 
	 * @param id
	 * 		The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the name of this index.
	 * 
	 * @param name
	 * 		The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the in-memory files in this index.
	 * 
	 * @return The in-memory files.
	 */
	public List<IndexedFile> getFiles() {
		return files;
	}

}
