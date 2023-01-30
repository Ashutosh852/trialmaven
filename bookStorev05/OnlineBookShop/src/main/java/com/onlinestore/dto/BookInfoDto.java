package com.onlinestore.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;



public class BookInfoDto {

	@NotNull
	private Long isbn;

	@NotNull
	private String title;

	private String author;

	@Min(value = 0, message = "The Quantity need to be positive")
	private int quantity;

	@Min(value = 0, message = "The Price need to be Positive.")
	private int price;
	
	@Min(value = 0, message = "The sell need to be Positive.")
	private int sold;

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

	public void setPrice(int price) {
		this.price = price;
	}


   
	public BookInfoDto(@NotNull Long isbn, @NotNull String title, String author,
			@Min(value = 0, message = "The Quantity need to be positive") int quantity,
			@Min(value = 0, message = "The Price need to be Positive.") int price) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.quantity = quantity;
		this.price = price;
	
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public BookInfoDto() {
		
	}

}
