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
	@Column(type = DatabaseType.INTEGER)
	@NotNull
	@AutoIncrement
	@PrimaryKey
	public Integer idImage;

	@Column(type = DatabaseType.STRING)
	@NotNull
	public String titre;

	@Column(type = DatabaseType.BYTES)
	@NotNull
	public byte[] jpeg;

	public Image(String title, byte[] jpeg) {
		this.titre = title;
		this.jpeg = jpeg;
	}

	public Image(Integer id, String title, byte[] jpeg) {
		this.idImage = id;
		this.titre = title;
		this.jpeg = jpeg;
	}
}
