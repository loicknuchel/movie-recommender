package recommender.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Rating;

public class DataFilter {

	public static List<Rating> ratingsForMovie(List<Rating> ratings, Integer... movies) {
		List<Rating> filtredRatings = new ArrayList<Rating>();
		List<Integer> movieList = Arrays.asList(movies);
		for (Rating r : ratings) {
			if (movieList.contains(r.movie())) {
				filtredRatings.add(r);
			}
		}
		return filtredRatings;
	}

	public static List<Rating> ratingsForUser(List<Rating> ratings, Integer... users) {
		List<Rating> filtredRatings = new ArrayList<Rating>();
		List<Integer> userList = Arrays.asList(users);
		for (Rating r : ratings) {
			if (userList.contains(r.user())) {
				filtredRatings.add(r);
			}
		}
		return filtredRatings;
	}

	public static Rating ratingForMovie(List<Rating> ratings, Integer movie) {
		for (Rating r : ratings) {
			if (r.movie() == movie) {
				return r;
			}
		}
		return null;
	}

	public static Rating ratingForUser(List<Rating> ratings, Integer user) {
		for (Rating r : ratings) {
			if (r.user() == user) {
				return r;
			}
		}
		return null;
	}
}
