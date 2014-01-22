package recommender.nonPersonalized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Movie;
import models.Rating;
import dao.DB;

import recommender.utils.DataOperation;
import recommender.utils.MyCollectionUtils;

public class Association {

	public static Map<Integer, Double> simpleAssociation(Integer movieId) {
		Map<Integer, List<Integer>> userLikedMovies = getUserLikes();
		List<Integer> movieIds = getMovieIds();
		Map<Integer, Double> movieAssociation = new HashMap<Integer, Double>();
		for (Integer id : movieIds) {
			if (id != movieId) {
				Double movieLiked = 0.0, movieBothLiked = 0.0;
				for (Entry<Integer, List<Integer>> entry : userLikedMovies.entrySet()) {
					if (entry.getValue().contains(movieId)) {
						movieLiked++;
						if (entry.getValue().contains(id)) {
							movieBothLiked++;
						}
					}
				}
				movieAssociation.put(id, movieBothLiked / movieLiked);
			}
		}
		return MyCollectionUtils.sortByValue(movieAssociation, false);
	}

	public static Map<Integer, Double> normalizedAssociation(Integer movieId) {
		Map<Integer, List<Integer>> userLikedMovies = getUserLikes();
		List<Integer> movieIds = getMovieIds();
		Map<Integer, Double> movieAssociation = new HashMap<Integer, Double>();
		for (Integer id : movieIds) {
			if (id != movieId) {
				Double movieLiked = 0.0, movieBothLiked = 0.0, movieNotLiked = 0.0, secondMovieLiked = 0.0;
				for (Entry<Integer, List<Integer>> entry : userLikedMovies.entrySet()) {
					if (entry.getValue().contains(movieId)) {
						movieLiked++;
						if (entry.getValue().contains(id)) {
							movieBothLiked++;
						}
					} else {
						movieNotLiked++;
						if (entry.getValue().contains(id)) {
							secondMovieLiked++;
						}
					}
				}
				movieAssociation.put(id, (movieBothLiked / movieLiked) / (secondMovieLiked / movieNotLiked));
			}
		}
		return MyCollectionUtils.sortByValue(movieAssociation, false);
	}

	private static Map<Integer, List<Integer>> getUserLikes() {
		List<Rating> ratings = DB.getRatings();
		Map<Integer, List<Rating>> userPartitionnedRatings = DataOperation.groupByUser(ratings);
		Map<Integer, List<Integer>> userLikedMovies = new HashMap<Integer, List<Integer>>();
		for (Entry<Integer, List<Rating>> entry : userPartitionnedRatings.entrySet()) {
			Integer userId = entry.getKey();
			userLikedMovies.put(userId, new ArrayList<Integer>());
			for (Rating r : entry.getValue()) {
				if (r.rating() > DB.likeThreshold()) {
					userLikedMovies.get(userId).add(r.movie());
				}
			}
		}
		return userLikedMovies;
	}

	private static List<Integer> getMovieIds() {
		Map<Object, Movie> movies = DB.getMovies();
		List<Integer> movieIds = new ArrayList<Integer>();
		for (Entry<Object, Movie> entry : movies.entrySet()) {
			movieIds.add((Integer) entry.getKey());
		}
		return movieIds;
	}
}
