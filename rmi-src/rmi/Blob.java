package rmi;

import java.util.Arrays;

public class Blob {
	public Integer getBegin() {
		return begin;
	}

	public byte[] getBuffer() {
		return buffer;
	}
	
	public Integer size() {
		return buffer.length;
	}

	public Blob(Integer begin, byte[] buffer) {
		this.begin = begin;
		this.buffer = Arrays.copyOf(buffer, buffer.length);
	}

	protected Integer begin;
	protected byte[] buffer;
}
