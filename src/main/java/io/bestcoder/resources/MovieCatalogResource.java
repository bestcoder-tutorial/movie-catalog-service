package io.bestcoder.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import io.bestcoder.model.CatalogItem;
import io.bestcoder.model.UserRating;
import io.bestcoder.service.MovieInfoService;
import io.bestcoder.service.UserRatingService;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	WebClient.Builder webClientBuilder;
	@Autowired
	DiscoveryClient discoveryClient;

	@Autowired
	MovieInfoService movieInfoService;
	@Autowired
	UserRatingService userRatingService;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalogSynchrone(@PathVariable("userId") String userId) {
		UserRating ratings = userRatingService.getUserRating(userId);
		return ratings.getRatings().stream().map(r -> movieInfoService.getCatalogItems(r)).collect(Collectors.toList());
	}

//	@RequestMapping("/{userId}")
//	public List<CatalogItem> getCatalogAsynchrone(@PathVariable("userId") String userId) {
//		// get all rated movies
//		UserRating ratings =restTemplate.getForObject("http://localhost:8083/ratings/users/"+userId,UserRating.class);
//		// for each movie id, call movie info service and get details
//		// resttemplate exemple
//		
////		return ratings.stream().map(r -> {
////		Movie movie= restTemplate.getForObject("http://localhost:8082/movies/"+r.getMovieId(), Movie.class);
////		return new CatalogItem(movie.getName(), "description", r.getRating());}).collect(Collectors.toList());
//
//		// web client exemple
//		return ratings.getRatings().stream().map(r -> {
//			Movie movie = webClientBuilder.build()
//					.get()
//					.uri("http://localhost:8082/movies/" + r.getMovieId())
//					.retrieve()
//					.bodyToMono(Movie.class)
//					.block();
//			// put them all together
//			return new CatalogItem(movie.getName(), "description", r.getRating());
//		}).collect(Collectors.toList());
//	}
}
