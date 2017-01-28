package com.softgate.fs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Index {
	
	private List<IndexedFile> files = new ArrayList<>();
	
	private int id;
	
	private String name;
	
	private Index() {
	}
	
	public static Index create(int id) {		
		Index idx = new Index();		
		idx.name = "index" + id;
		return idx;
	}
	
	public static Index create(int id, String name) {
		Index idx = new Index();
		idx.id = id;
		idx.name = name;
		return idx;
	}
	
	public static Index create(int id, IndexedFile file) {
		Index idx = new Index();
		idx.files.add(file);
		idx.id = id;
		idx.name = "index" + idx.id;
		return idx;
	}
	
	public static Index create(int id, String name, IndexedFile file) {
		Index idx = new Index();
		idx.files.add(file);
		idx.id = id;
		idx.name = name;
		return idx;
	}
	
	public static Index create(int id, IndexedFile... files) {
		Index idx = new Index();
		idx.files.addAll(Arrays.asList(files));
		idx.id = id;
		idx.name = "index" + idx.id;
		return idx;
	}
	
	public static Index create(int id, String name, IndexedFile... files) {
		Index idx = new Index();
		idx.files.addAll(Arrays.asList(files));
		idx.id = id;
		idx.name = name;
		return idx;
	}
	
	public void add(IndexedFile file) {
		this.files.add(file);
	}
	
	public void addAll(IndexedFile...files) {
		this.files.addAll(Arrays.asList(files));
	}
	
	public void remove(IndexedFile file) {
		this.files.remove(file);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public List<IndexedFile> getFiles() {
		return files;
	}

}
