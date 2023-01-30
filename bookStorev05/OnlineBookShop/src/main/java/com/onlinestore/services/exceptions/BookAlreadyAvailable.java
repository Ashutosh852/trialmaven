package com.onlinestore.services.exceptions;

public class BookAlreadyAvailable extends RuntimeException

{
	public BookAlreadyAvailable(String message) {
        super(message);
    }


}
