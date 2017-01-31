package com.softgate.fs;

/**
 * A wrapper class that will contain a files actual data.
 * 
 * @author Chad Adams
 */
public class IndexedFile {
	
	/**
	 * The header of this {@link IndexedFile}.
	 */
	private IndexedFileHeader header;
	
	/**
	 * The data or payload of this file.
	 */
	private byte[] payload;	
	
	public IndexedFile(IndexedFileHeader header) {
		this(header, new byte[0]);
	}
	
	/**
	 * Creates a new {@link IndexedFile}.
	 * 
	 * @param header
	 * 		The header of this file.
	 * 
	 * @param payload
	 * 		The payload that contains the files actual data.
	 */
	public IndexedFile(IndexedFileHeader header, byte[] payload) {		
		this.header = header;
		this.payload = payload;
	}
	
	public IndexedFile copy() {		
		return new IndexedFile(header, payload);
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {		
		this.payload = payload;
	}

	public IndexedFileHeader getHeader() {
		return header;
	}

	public void setHeader(IndexedFileHeader header) {
		this.header = header;
	}

}
