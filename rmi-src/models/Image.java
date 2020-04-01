package models;

public class Image {	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public byte[] getJpeg() {
		return jpeg;
	}
	
	public void setJpeg(byte[] jpeg) {
		this.jpeg = jpeg;
	}

	public Image() {}
	
	public Image(Integer id, String title, byte[] jpeg) {
		this.id = id;
		this.title = title;
		this.jpeg = jpeg;
	}

	protected Integer id;
	protected String title;
	protected byte[] jpeg;
}
