package io.bestcoder.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.bestcoder.model.Rating;
import io.bestcoder.model.UserRating;

@Service
public class UserRatingService {
	
	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getFallBackUserRating", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "5000")
	})
	public UserRating getUserRating(String userId) {
		return restTemplate.getForObject("http://rating-data-service/ratings/users/" + userId, UserRating.class);
	}

	public UserRating getFallBackUserRating(String userId) {
		return new UserRating(userId, Arrays.asList(new Rating("0", 0)));
	}

}
