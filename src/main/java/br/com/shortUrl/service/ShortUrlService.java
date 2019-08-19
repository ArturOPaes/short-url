package br.com.shortUrl.service;

import java.net.URI;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.shortUrl.domain.ShortUrl;
import br.com.shortUrl.exception.ShortCodeNotFoundException;
import br.com.shortUrl.repository.ShortUrlRepository;

@Service
public class ShortUrlService {

	private static final String SPACE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

	private static final int BASE = SPACE.length();

	private final Random random = new Random();

	@Autowired
	private ShortUrlRepository shortUrlRepository;
	
	public ShortUrl shortenUrl(String uri) {
		Optional<ShortUrl> search = shortUrlRepository.findByUri(uri);
		
		if(search.isPresent()) {
			return search.get();
		}
		
		int randomNumber = Math.abs(this.random.nextInt());
		String shortCode = encode(randomNumber);
		ShortUrl shortUrl = new ShortUrl(uri, shortCode);
		
		shortUrlRepository.save(shortUrl);
		
		return shortUrl;
	}

	public ShortUrl expandShortCode(String shortCode, Boolean info) {
		ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
				.orElseThrow(() -> new ShortCodeNotFoundException(shortCode));
		
		if(info) {
			shortUrl.setLinkClicks(shortUrl.getLinkClicks() + 1);
			shortUrlRepository.save(shortUrl);
		}
		
		return shortUrl;
	}

	private static String encode(int num) {
		Assert.isTrue(num > 0, "Number must be positive");
		StringBuilder str = new StringBuilder();
		while (num > 0) {
			str.insert(0, SPACE.charAt(num % BASE));
			num = num / BASE;
		}
		return str.toString();
	}

}