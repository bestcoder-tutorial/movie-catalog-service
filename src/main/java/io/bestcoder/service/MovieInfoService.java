package io.bestcoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.bestcoder.model.CatalogItem;
import io.bestcoder.model.Movie;
import io.bestcoder.model.Rating;

@Service
public class MovieInfoService {

	@Autowired
	RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallBackCatalogItems", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "5000")
	})
	public CatalogItem getCatalogItems(Rating r) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + r.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), "description", r.getRating());
	}
	
	public CatalogItem getFallBackCatalogItems(Rating r) {
		return new CatalogItem("no name found for this movie", "", r.getRating());

	}
}
