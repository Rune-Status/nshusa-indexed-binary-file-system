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

public class IndexedFileSystem implements Closeable {
	
	private List<Index> indexes = new ArrayList<>(255);	
	
	private IndexedFileSystem() {
		
	}
	
	public static IndexedFileSystem create() {
		return new IndexedFileSystem();
	}
	
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
					
					int length = dis.readInt();
					
					byte[] data = new byte[length];
					
					for (int i = 0; i < length; i++) {
						data[i] = dis.readByte();
					}
					
					Index idx = fs.getIndex(index);
					
					idx.add(new IndexedFile(data));

				}
				
			}
			
		}
		
		return fs;
	}
	
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
					
					int length = file.getData().length;
					
					out.writeInt(length);
					
					byte[] data = file.getData();
					
					out.write(data);
					
				}
				
			}
		}
	}
	
	public void add(Index index) {
		
		if (indexes.isEmpty()) {
			indexes.add(index);
			return;
		}
		
		indexes.add(index.getId() - 1, index);
	}

	public void remove(Index index) {
		indexes.remove(index.getId());
	}
	
	public Index getIndex(int id) {
		return indexes.get(id);
	}
	
	public List<Index> getFiles() {
		return indexes;
	}

	@Override
	public void close() throws IOException {
		indexes.clear();
	}

}
