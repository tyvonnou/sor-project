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
	public Image() {}

	public Image(String title, byte[] jpeg) {
		this.title = title;
		this.jpeg = jpeg;
	}

	public Image(Integer id, String title, byte[] jpeg) {
		this.id = id;
		this.title = title;
		this.jpeg = jpeg;
	}

	@Column(type = DatabaseType.INTEGER)
	@NotNull
	@AutoIncrement
	@PrimaryKey
	public Integer id;
	
	@Column(type = DatabaseType.STRING)
	@NotNull
	public String title;
	
	@Column(type = DatabaseType.BYTES)
	@NotNull
	public byte[] jpeg;
}
