package com.softgate.fs;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

/**
 * Represents a file system based on indexes.
 * 
 * @author Chad Adams
 */
public class IndexedFileSystem implements Closeable {
	
	/**
	 * The collection of indexes in this file system.
	 */
	private final List<Index> indexes = new ArrayList<>(255);	
	
	/**
	 * The private constructor
	 */
	private IndexedFileSystem() {
		
	}
	
	/**
	 * The method to create a new {@link IndexedFileSystem}.
	 * 
	 * @return The indexed file system.
	 */
	public static IndexedFileSystem create() {
		return new IndexedFileSystem();
	}
	
	/**
	 * The method that will decode an already encoded {@link IndexedFileSystem} back into memory.
	 * 
	 * @param path
	 * 		The path of the file to decode.
	 * 
	 * @throws IOException, if the file cannot be decoded or does not exist.
	 * @return The indexed file system.
	 */
	public static IndexedFileSystem decode(String path) throws IOException {		
		IndexedFileSystem fs = IndexedFileSystem.create();
		
		try(DataInputStream dis = new DataInputStream(new XZCompressorInputStream(new FileInputStream(Paths.get(path).toFile())))) {
			
			int indexes = dis.readInt();
			
			for (int index = 0; index < indexes; index++) {
				
				int id = dis.readByte();
				
				String name = dis.readUTF();
				
				int files = dis.readInt();
				
				fs.add(Index.create(id, name));
				
				for (int file = 0; file < files; file++) {
					
					int fileId = dis.readInt();					
					
					String fileName = dis.readUTF();					
					
					int fileLength = dis.readInt();					
					
					byte[] data = new byte[fileLength];
					
					for (int i = 0; i < fileLength; i++) {
						data[i] = dis.readByte();
					}
					
					Index idx = fs.getIndex(index);
					
					idx.add(fileId, fileName, data);

				}
				
			}
			
		}
		
		return fs;
	}
	
	/**
	 * The method that encodes this {@IndexedFileSystem} into a single binary file.
	 * 
	 * @param output
	 * 		The directory where the file will be created.
	 * 
	 * @throws IOException
	 * 		The exception being thrown if data cannot be written to a file.	 * 		
	 */
	public void encode(String output) throws IOException {
		try(DataOutputStream out = new DataOutputStream(new XZCompressorOutputStream(new FileOutputStream(new File(output))))) {			
			out.writeInt(indexes.size());
			
			for(Index idx : indexes) {	
				
				int files = idx.getFiles().size();
				
				out.writeByte(idx.getId());
				out.writeUTF(idx.getName());
				out.writeInt(files);				
				
				for (int i = 0; i < files; i++) {
					
					IndexedFile file = idx.getFiles().get(i);
					
					int fileId = file.getHeader().getId();
					String name = file.getHeader().getName();
					int length = file.getPayload().length;
					
					out.writeInt(fileId);
					out.writeUTF(name);
					out.writeInt(length);
					
					byte[] payload = file.getPayload();					
					
					out.write(payload);
					
				}
				
			}
		}
	}
	
	/**
	 * The method that adds an {@link Index} to this {@link IndexedFileSystem}.
	 * 
	 * @param index
	 * 		The index to add.
	 * 
	 * @return The index that was added.
	 */
	public Index add(Index index) {
		
		if (indexes.isEmpty()) {
			indexes.add(index);
			return index;
		}
		
		if (index.getId() > indexes.size()) {
			for (int i = indexes.size(); i < index.getId(); i++) {
				indexes.add(Index.create(i, "empty"));
			}
		}
		
		indexes.add(index.getId(), index);
		
		changeIndexId(index.getId() + 1);
		
		return index;
	}
	
	/**
	 * The method that removes an {@link Index} based on its identifier.
	 * 
	 * @param id
	 * 		The identifier or position in the collection.
	 */
	public void remove(int id) {
		
		if (id < 0) {
			throw new IllegalArgumentException(String.format("id=%d cannot be negative.", id));
		}
		
		if (id > indexes.size()) {
			throw new IllegalArgumentException(String.format("id=%d is greater than size=%d", id, indexes.size()));
		}
		
		indexes.remove(id);
		
		changeIndexId(id);
		
	}
	
	/**
	 * The method that will change all {@link Index}'s after the index added.
	 * 
	 * @param start
	 * 		The starting index
	 * 
	 * @param index
	 * 		The index that was added
	 */
	private void changeIndexId(int start) {
		for (int i = start; i < indexes.size(); i++) {
			
			Index idx = indexes.get(i);
			
			if (idx == null) {
				continue;
			}
			
			idx.setId(i);
			
		}
	}
	
	/**
	 * Gets the data from an {@link IndexedFile} by its name.
	 * 
	 * @param id
	 * 		The id of the index to read from.
	 * 
	 * @param fileName
	 * 		The name of the indexed file.
	 * 
	 * @return The data.
	 */
	public byte[] read(int id, String fileName) {
		
		if (id < 0) {
			throw new IllegalArgumentException(String.format("id=%d cannot be negative.", id));
		}
		
		if (id > indexes.size()) {
			throw new IllegalArgumentException(String.format("id=%d is out of range: %d", id, indexes.size()));
		}
		
		for (IndexedFile file : indexes.get(id).getFiles()) {
			if (file.getHeader().getName().equalsIgnoreCase(fileName)) {
				return file.getPayload();
			}
		}
		
		return null;
	}
	
	/**
	 * The method that will read bytes at a specific {@link IndexedFile}.
	 * 
	 * @param id
	 * 		The id of the index to read from.
	 * 
	 * @param file
	 * 		The id of the file to read.
	 * 
	 * @return The bytes read at the specified file.
	 */
	public byte[] read(int id, int file) {		
		
		if (id > indexes.size() || id < 0) {
			throw new IllegalArgumentException(String.format("index=[%d] out of range.", id));
		}		
		
		return indexes.get(id).getFiles().get(file).getPayload();
	}
	
	/**
	 * The method that retrieves an {@link Index} by its id.
	 * 
	 * @param id
	 * 		The id of the index to get.
	 * 
	 * @return The index retrieved.
	 */
	public Index getIndex(int id) {
		return indexes.get(id);
	}
	
	/**
	 * Gets the collection of {@link Index}'s in this {@link IndexedFileSystem}.
	 */
	public List<Index> getIndexes() {
		return indexes;
	}

	@Override
	public void close() throws IOException {
		indexes.clear();
	}

}
