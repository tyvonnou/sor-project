package models;

import annotation.AutoIncrement;
import annotation.Column;
import annotation.NotNull;
import annotation.PrimaryKey;
import annotation.Table;
import base.DatabaseType;
import base.SQLModel;

@Table(name = "t_image")
public class Image extends SQLModel<Image> {	
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

	@Column(type = DatabaseType.INTEGER)
	@NotNull
	@AutoIncrement
	@PrimaryKey
	protected Integer id;
	
	@Column(type = DatabaseType.STRING)
	@NotNull
	protected String title;
	
	@Column(type = DatabaseType.BYTES)
	@NotNull
	protected byte[] jpeg;
}
