package rmi;

import config.Config;

public class TmpImage {
	protected Chain blobs;
	protected Integer length;
	protected Integer size;
	
	public Integer getLength() {
		return length;
	}

	public Integer getSize() {
		return size;
	}
	
	public byte[] join() {
		byte[] join = new byte[length * Config.config.getBufferSize() + 1];
		Integer i = 0;
		for (Chain it = this.blobs; it != null; it = it.next) {
			byte[] buffer = it.value.getBuffer(); 
			for (Byte b : buffer) {
				join[i++] = b;
			}
		}
		return join;
	}

	class Chain {
		Chain next;
		Blob value;
		
		public Chain(Blob value) {
			this(value, null);
		}
		
		public Chain(Blob value, Chain next) {
			this.value = value;
			this.next = next;
		}
	}
	
	public boolean add(Integer begin, byte[] buffer) {
		if (this.isFull()) {
			return false;
		}
		if (this.blobs == null) {
			this.blobs = new Chain(new Blob(begin, buffer));
		} else if (this.blobs.value.begin > begin) {
			this.blobs = new Chain(new Blob(begin, buffer), this.blobs);
		} else {
			Chain previous = this.blobs;
			for(Chain it = this.blobs.next; it != null; it = it.next) {
				Blob blob = it.value;
				if (blob.begin > begin) {
					previous.next = new Chain(new Blob(begin, buffer), it);
					break;
				}
				previous = it;
			}
			if (previous.next == null) {
				previous.next = new Chain(new Blob(begin, buffer));							
			}
		}
		this.size++;
		return true;
	}
	
	public boolean isFull() {
		return this.length.equals(this.size);
	}
	
	public TmpImage(Integer length) {
		this.blobs = null;
		this.length = length;
		this.size = 0;
	}
}
