package br.com.shortUrl.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.shortUrl.domain.ShortUrl;
import br.com.shortUrl.service.ShortUrlService;

@Controller
@RequestMapping("/v1")
public class ShortUrlController {
	
	@Autowired
	private ShortUrlService shortUrlService;

	@PostMapping("/")
	public ResponseEntity shortenUrl(@RequestParam String uri) {
		ShortUrl shortUrl = shortUrlService.shortenUrl(uri);
		URI shortenedUrl = URI.create("http://localhost:8080/v1/" + shortUrl.getShortCode());
		return ResponseEntity.status(HttpStatus.OK).body(shortenedUrl);
	}

	@GetMapping("/{shortCode}")
	public ResponseEntity expandUrl(@PathVariable String shortCode) {
		ShortUrl shortUrl = shortUrlService.expandShortCode(shortCode, true);
		URI fullUrl = URI.create(shortUrl.getUri());
		return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
				.location(fullUrl).build();
	}

	@GetMapping("/{shortCode}/info")
	@ResponseBody
	public ShortUrl fetchShortUrlInfo(@PathVariable String shortCode) {
		return shortUrlService.expandShortCode(shortCode, false);
	}

}
