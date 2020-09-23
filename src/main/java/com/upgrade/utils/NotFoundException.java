package com.upgrade.utils;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
	private long id;

	public NotFoundException(long id) {
	    this.id = id;
	}
	public NotFoundException() {
	    this.id = 0;
	}

	public long getId() {
		return id;
	}
}