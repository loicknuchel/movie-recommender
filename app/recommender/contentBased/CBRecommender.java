package recommender.contentBased;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Movie;
import models.Rating;

import dao.DB;

import recommender.utils.DataFilter;
import recommender.utils.MyCollectionUtils;

public class CBRecommender {

	public static Map<Integer, Double> simpleMatch(Integer userId) {
		List<Rating> ratings = DB.getRatings();
		Map<Object, Movie> movies = DB.getMovies();
		List<Rating> userRatings = DataFilter.ratingsForUser(ratings, userId);
		Map<String, Double> userProfile = CBPredictor.buildUserProfile(userRatings, movies);
		
		Map<Integer, Map<String, Double>> movieProfiles = new HashMap<Integer, Map<String, Double>>();
		for (Entry<Object, Movie> entry : movies.entrySet()) {
			movieProfiles.put((Integer) entry.getKey(), CBPredictor.buildMovieProfile(entry.getValue()));
		}

		Map<Integer, Double> moviePredictions = new HashMap<Integer, Double>();
		for (Entry<Integer, Map<String, Double>> entry : movieProfiles.entrySet()) {
			moviePredictions.put(entry.getKey(), CBPredictor.simplePrediction(userProfile, entry.getValue()));
		}

		return MyCollectionUtils.sortByValue(moviePredictions, false);
	}
}
