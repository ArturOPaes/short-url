package br.com.shortUrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.shortUrl.domain.ShortUrl;
import br.com.shortUrl.exception.ShortCodeNotFoundException;
import br.com.shortUrl.repository.ShortUrlRepository;
import br.com.shortUrl.service.ShortUrlService;

@RunWith(MockitoJUnitRunner.class)
public class ShortUrlServiceTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private ShortUrlRepository repository;

	@InjectMocks
	private ShortUrlService shortUrlService;


	@Test
	public void canShortenUrls() {
		String uri = "http://muchmore.digital";
		ShortUrl shortUrl = shortUrlService.shortenUrl(uri);
		assertThat(shortUrl.getShortCode()).isNotBlank();
		assertThat(shortUrl.getUri()).isEqualTo(uri);
		verify(repository, times(1)).save(any());
	}

	@Test
	public void canExpandShortCodes() {
		String uri = "http://muchmore.digital";
		ShortUrl shortUrl = new ShortUrl(uri, "muchmore");
		Mockito.when(repository.findByShortCode("muchmore")).thenReturn(Optional.of(shortUrl));
		ShortUrl result = shortUrlService.expandShortCode("muchmore", false);
		assertThat(result.getUri()).isEqualTo(uri);
		assertThat(result.getShortCode()).isEqualTo("muchmore");
		verify(repository, times(1)).findByShortCode(eq("muchmore"));
	}

	@Test
	public void unknownShortCode() {
		Mockito.when(repository.findByShortCode("muchmore")).thenReturn(Optional.empty());
		this.thrown.expect(ShortCodeNotFoundException.class);
		this.thrown.expect(Matchers.hasProperty("shortCode", Matchers.equalTo("muchmore")));
		ShortUrl result = shortUrlService.expandShortCode("muchmore", false);
	}

}