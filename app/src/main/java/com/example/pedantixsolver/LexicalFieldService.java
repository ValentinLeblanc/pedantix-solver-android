package com.example.pedantixsolver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LexicalFieldService {

	private static final String LEXICAL_FIELD_URL = "https://www.rimessolides.com/motscles.aspx?m=";

	private final RestTemplate restTemplate = new RestTemplate();

	public List<String> getSimilarWords(String word) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("User-Agent", "PostmanRuntime/7.36.0");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(LEXICAL_FIELD_URL + encodeWord(word), HttpMethod.GET,
				entity, String.class);

		return extractWords(response.getBody());
	}

	private String encodeWord(String word) {
		try {
			word = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return word;
	}

	private List<String> extractWords(String htmlContent) {
		List<String> words = new ArrayList<>();

		Document doc = Jsoup.parse(htmlContent);

		Elements motcleElements = doc.select(".motcle");
		for (Element element : motcleElements) {
			String text = element.text();
			words.add(text.replace(",", ""));
		}

		motcleElements = doc.select(".refm");
		for (Element element : motcleElements) {
			String text = element.text();
			words.add(text.replace(",", ""));
		}
		return words;
	}

}
