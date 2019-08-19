package br.com.shortUrl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.shortUrl.controller.ShortUrlController;
import br.com.shortUrl.domain.ShortUrl;
import br.com.shortUrl.exception.ShortCodeNotFoundException;
import br.com.shortUrl.service.ShortUrlService;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortUrlController.class)
public class ShortUrlControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ShortUrlService shortUrlService;

	@Test
	public void shouldShortenUrl() throws Exception {
		String uri = "http://muchmore.digital";
		ShortUrl shortUrl = new ShortUrl(uri, "muchmore");
		Mockito.when(shortUrlService.shortenUrl(uri)).thenReturn(shortUrl);
		this.mvc.perform(post("/v1/").param("uri", "http://muchmore.digital"))
				.andExpect(status().isOk())
				.andExpect(content().string(Matchers.containsString("/muchmore")));
	}

	@Test
	public void shouldRedirectToUrl() throws Exception {
		String uri = "http://muchmore.digital";
		ShortUrl shortUrl = new ShortUrl(uri, "muchmore");
		Mockito.when(shortUrlService.expandShortCode("muchmore", false)).thenReturn(shortUrl);
		this.mvc.perform(get("/muchmore").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isPermanentRedirect())
				.andExpect(header().string("Location", "http://muchmore.digital"));
	}

	@Test
	public void shouldReturnNotFoundForMissingShortUrls() throws Exception {
		Mockito.when(shortUrlService.expandShortCode("muchmore", false)).thenThrow(ShortCodeNotFoundException.class);
		this.mvc.perform(get("/muchmore"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void shouldShowJSONInfo() throws Exception {
		String uri = "http://muchmore.digital";
		ShortUrl shortUrl = new ShortUrl(uri, "muchmore");
		Mockito.when(shortUrlService.expandShortCode("muchmore", false)).thenReturn(shortUrl);
		this.mvc.perform(get("/v1/muchmore/info").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.shortCode").value("muchmore"))
				.andExpect(jsonPath("$.uri").value("http://muchmore.digital"));
	}

}
