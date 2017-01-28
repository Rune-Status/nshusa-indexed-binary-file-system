package com.softgate.fs;

public class IndexedFile {
	
	private String name;
	
	private byte[] data;
	
	public IndexedFile(byte[] data) {
		this.name = "unknown";
		this.data = data;
	}
	
	public IndexedFile(String name, byte[] data) {
		this.name = name;
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

}
