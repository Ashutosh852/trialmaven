package com.onlinestore.services;

	
	import org.springframework.data.jpa.domain.Specification;

import com.onlinestore.entity.BookInfo;
import com.onlinestore.entity.BookInfo_;
	 
	public final class BookSearchSpecification {
	 
	    private BookSearchSpecification() {}
	 
	    public static Specification<BookInfo> titleOrAuthorContainsIgnoreCase(String searchTerm) {
	        return (root, query, cb) -> {
	            String containsLikePattern = getContainsLikePattern(searchTerm);
	            return cb.or(
	                    cb.like(cb.lower(root.<String>get(BookInfo_.title)), containsLikePattern),
	                    cb.like(cb.lower(root.<String>get(BookInfo_.author)), containsLikePattern)
	            );
	        };
	    }
	 
	    private static String getContainsLikePattern(String searchTerm) {
	        if (searchTerm == null || searchTerm.isEmpty()) {
	            return "%";
	        }
	        else {
	            return "%" + searchTerm.toLowerCase() + "%";
	        }
	    }

	}


