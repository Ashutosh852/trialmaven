package com.onlinestore;


import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.onlinestore.dto.BookInfoDto;
import com.onlinestore.utility.Response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@Sql("/schema.sql")
public class IntegrationTest {

	private TestRestTemplate resttemplate = new TestRestTemplate();

	BookInfoDto book1 = new BookInfoDto(129L, "title", "Vk Kumavat", 2, 100);
	BookInfoDto book2 = new BookInfoDto(122L, "Sciense", "Rk Joshi", 4, 190);
	BookInfoDto book3 = new BookInfoDto(123L, "Maths", "Vk Mishra", 6, 160);

	@Test
	public void testAddNewBook() throws Exception {

		String url = "http://localhost:8080/book";
		URI uri = new URI(url);
		this.cleanUp();
		Response response = new Response();
		HttpEntity<BookInfoDto> httpEntity = new HttpEntity<BookInfoDto>(book1);
		response = this.resttemplate.postForEntity(uri, httpEntity, Response.class).getBody();

		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void testgetAll() throws Exception {

		String url = "http://localhost:8080/book";
		URI uri = new URI(url);
		Response response = new Response();
		response = this.resttemplate.getForEntity(uri, Response.class).getBody();

		Assertions.assertEquals(response.getSuccess(), true);

	}

	@BeforeEach
	void setUp() throws URISyntaxException {
		String url = "http://localhost:8080/book";
		URI uri = new URI(url);

		
		HttpEntity<BookInfoDto> httpEntity = new HttpEntity<BookInfoDto>(book1);
		this.resttemplate.postForEntity(uri, httpEntity, Response.class).getBody();

	}

	@AfterEach
	void cleanUp() throws URISyntaxException {
		String url = "http://localhost:8080/book/129";
		URI uri = new URI(url);

		this.resttemplate.delete(uri);
	}

	@Test
	public void testGetBookBYTd() throws Exception {

		Response response = new Response();

		String url = "http://localhost:8080/book/129";
		URI uri = new URI(url);

		response = this.resttemplate.getForEntity(uri, Response.class).getBody();

		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void testAddBookQuantity() throws Exception {

		Response response = new Response();

		String url = "http://localhost:8080/book/129";
		URI uri = new URI(url);
		HttpEntity<BookInfoDto> httpEntity = new HttpEntity<BookInfoDto>(book1);

		response = this.resttemplate.exchange(uri, HttpMethod.PUT, httpEntity, Response.class).getBody();

		Assertions.assertEquals(response.getSuccess(), true);
		Assertions.assertEquals(response.getStatuscode(), 200);

	}

	@Test
	public void testBuyBook() throws Exception {
		Response response = new Response();

		String url = "http://localhost:8080/book/buy/129";
		URI uri = new URI(url);
		HttpEntity<BookInfoDto> httpEntity = new HttpEntity<BookInfoDto>(book1);

		response = this.resttemplate.exchange(uri, HttpMethod.PUT, httpEntity, Response.class).getBody();

		Assertions.assertEquals(response.getSuccess(), true);
		Assertions.assertEquals(response.getStatuscode(), 200);

	}

	@Test
	public void testBuyBookMoreThanStock() throws Exception {
		Response response = new Response();
		book1.setQuantity(100);
		String url = "http://localhost:8080/book/buy/129";
		URI uri = new URI(url);
		HttpEntity<BookInfoDto> httpEntity = new HttpEntity<BookInfoDto>(book1);

		response = this.resttemplate.exchange(uri, HttpMethod.PUT, httpEntity, Response.class).getBody();

		Assertions.assertEquals(response.getSuccess(), false);
		Assertions.assertEquals(response.getStatuscode(), 500);

	}

	@Test
	public void testDeleteBookById() throws Exception {
		Response response = new Response();
		String url = "http://localhost:8080/book/129";
		URI uri = new URI(url);
		HttpEntity<BookInfoDto> httpEntity = new HttpEntity<BookInfoDto>(book1);
		response = this.resttemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Response.class).getBody();

		Assertions.assertEquals(response.getSuccess(), true);
		Assertions.assertEquals(response.getStatuscode(), 200);

	}

}
