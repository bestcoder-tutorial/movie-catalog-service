package io.bestcoder.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.bestcoder.model.CatalogItem;
import io.bestcoder.model.Movie;
import io.bestcoder.model.Rating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	RestTemplate restTemplate;
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId")String userId) {
		// get all rated movies
		List<Rating> ratings = Arrays.asList(new Rating("1", 2));
		// for each movie id, call movie info service and get details
		return ratings.stream().map(r -> {
		Movie movie= restTemplate.getForObject("http://localhost:8082/movies/"+r.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), "description", r.getRating());}).collect(Collectors.toList());
		// put them all together
	}
}
