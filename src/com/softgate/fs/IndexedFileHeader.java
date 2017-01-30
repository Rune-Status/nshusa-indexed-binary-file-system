package com.softgate.fs;

/**
 * Represents Meta-data that describes a {@link IndexedFile}'s payload.
 * 
 * @author Chad Adams
 */
public class IndexedFileHeader {
	
	/**
	 * The identifier of a {@link IndexedFile}.
	 */
	private int id;
	
	/**
	 * The name of a {@link IndexedFile}.
	 */
	private String name;

	/**
	 * Creates a new {@link id}.
	 * 
	 * @param id
	 * 		The identifier
	 * 
	 * @param name
	 * 		The name
	 */
	public IndexedFileHeader(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}		

}
