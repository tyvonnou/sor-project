package rmi;

import java.util.Arrays;

public class Blob {
	public Long getBegin() {
		return begin;
	}

	public byte[] getBuffer() {
		return buffer;
	}
	
	public Integer size() {
		return buffer.length;
	}

	public Blob(Long begin, byte[] buffer) {
		this.begin = begin;
		this.buffer = Arrays.copyOf(buffer, buffer.length);
	}

	protected Long begin;
	protected byte[] buffer;
}
