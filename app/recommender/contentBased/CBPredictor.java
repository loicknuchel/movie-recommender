package recommender.contentBased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Movie;
import models.Rating;

import recommender.utils.DataOperation;

public class CBPredictor {

	public static Double simplePrediction(Map<String, Double> userProfile, Map<String, Double> movieProfile) {
		return DataOperation.vectorCosine(userProfile, movieProfile);
	}

	public static Map<String, Double> buildUserProfile(List<Rating> userRatings, Map<Object, Movie> movies) {
		Map<String, Double> userProfile = new HashMap<String, Double>();
		Double avg = DataOperation.average(userRatings);
		for (Rating r : userRatings) {
			Movie movie = movies.get(r.movie());
			List<String> features = new ArrayList<String>();
			features.addAll(movie.getTags());
			// features.addAll(movie.getKeywords());
			// features.addAll(movie.getStars());
			for (String tag : features) {
				if (userProfile.get(tag) == null) {
					userProfile.put(tag, 0.0);
				}
				userProfile.put(tag, userProfile.get(tag) + (r.rating() - avg));
			}
		}

		return userProfile;
	}

	public static Map<String, Double> buildMovieProfile(Movie movie) {
		Map<String, Double> vector = new HashMap<String, Double>();
		List<String> features = new ArrayList<String>();
		features.addAll(movie.getTags());
		// features.addAll(movie.getKeywords());
		// features.addAll(movie.getStars());
		for (String val : features) {
			vector.put(val, 1.0);
		}
		return vector;
	}
}
