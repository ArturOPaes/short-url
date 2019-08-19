package br.com.shortUrl.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.shortUrl.domain.ShortUrl;

@Repository
public interface ShortUrlRepository extends MongoRepository<ShortUrl, String> {

	Optional<ShortUrl> findByShortCode(String shortCode);
	
	Optional<ShortUrl> findByUri(String uri);
}
