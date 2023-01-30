package com.onlinestore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity

@Table(name = "book_info")
public class BookInfo {

	@Id
	private Long isbn;

	@NotNull
	private String title;

	private String author;

	@Min(value = 0, message = "The Quantity need to be positive")
	private int quantity;

	@Min(value = 0, message = "The Price need to be Positive.")
	private float price;
   
	@Min(value = 0, message = "The sell need to be Positive.")
	private int sold;
	
	@Version
	int version;

	public BookInfo() {

	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public BookInfo(Long isbn, @NotNull String title, String author,
			@Min(value = 0, message = "The Quantity need to be positive") int quantity,
			@Min(value = 0, message = "The Price need to be Positive.") float price,
			@Min(value = 0, message = "The sell need to be Positive.") int sold) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.quantity = quantity;
		this.price = price;
		this.sold = sold;
	}

	

}
