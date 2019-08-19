package br.com.shortUrl.domain;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortUrl {

	@Id
	@JsonProperty("id")
	private String id;

	@JsonProperty("uri")
	private String uri;

	@JsonProperty("shortCode")
	private String shortCode;
	
	@JsonProperty("linkClicks")
	private Integer linkClicks;

	@JsonProperty("creationDate")
	private LocalDateTime creationDate;

	public ShortUrl() {
	}

	public ShortUrl(String uri, String shortCode) {
		this.uri = uri;
		this.shortCode = shortCode;
		this.linkClicks = 0;
		this.creationDate = LocalDateTime.now();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	public Integer getLinkClicks() {
		return linkClicks;
	}

	public void setLinkClicks(Integer linkClicks) {
		this.linkClicks = linkClicks;
	}

	@Override
	public String toString() {
		return "ShortURL{" +
				"id='" + id + '\'' +
				", uri=" + uri +
				", shortCode='" + shortCode + '\'' +
				", linkClicks='" + linkClicks + '\'' +
				", creationDate=" + creationDate +
				'}';
	}
}
