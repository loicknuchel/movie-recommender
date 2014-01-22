package recommender.collaborativeFiltering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import recommender.utils.DataFilter;
import recommender.utils.DataOperation;

import models.Rating;

public class CFPredictor {

	public static Double simpleAverage(List<Rating> ratings, Integer movie) {
		return DataOperation.average(DataFilter.ratingsForMovie(ratings, movie));
	}

	public static Double ponderatedAverage(List<Rating> ratings, Integer user, Integer movie, Map<Integer, List<Rating>> userPartitionnedRatings,
			Map<Integer, Double> similarities) {
		Double sum = 0d, sumWeight = 0d;
		for (Rating r : DataFilter.ratingsForMovie(ratings, movie)) {
			if (r.user() != user) {
				sum += r.rating() * similarities.get(r.user());
				sumWeight += similarities.get(r.user());
			}
		}

		if (sumWeight == 0)
			return 0d;
		else
			return sum / sumWeight;
	}

	public static Double normalizedPonderatedAverage(List<Rating> ratings, Integer user, Integer movie, Map<Integer, List<Rating>> userPartitionnedRatings,
			Map<Integer, Double> similarities) {
		Double sum = 0d, sumWeight = 0d;
		for (Rating r : DataFilter.ratingsForMovie(ratings, movie)) {
			if (r.user() != user) {
				Double userAvg = DataOperation.average(userPartitionnedRatings.get(r.user()));
				Double userDeviation = DataOperation.deviation(userPartitionnedRatings.get(r.user()), userAvg);
				sum += DataOperation.normalize(r.rating(), userAvg, userDeviation) * similarities.get(r.user());
				sumWeight += similarities.get(r.user());
			}
		}
		Double userAvg = DataOperation.average(userPartitionnedRatings.get(user));
		Double userDeviation = DataOperation.deviation(userPartitionnedRatings.get(user), userAvg);

		if (sumWeight == 0)
			return 0d;
		else
			return DataOperation.denormalize(sum / sumWeight, userAvg, userDeviation);
	}

	public static Double userSimilarity(Map<Integer, List<Rating>> userPartitionnedRatings, Integer user1, Integer user2) {
		return userSimilarity(userPartitionnedRatings.get(user1), userPartitionnedRatings.get(user2));
	}

	public static Double userSimilarity(List<Rating> ratings1, List<Rating> ratings2) {
		Double avg1 = DataOperation.average(ratings1);
		Double avg2 = DataOperation.average(ratings2);

		Double sum = 0d, sumWeight = DataOperation.deviation(ratings1, avg1) * DataOperation.deviation(ratings2, avg2);
		for (Rating r : ratings1) {
			Rating rating = DataFilter.ratingForMovie(ratings2, r.movie());
			if (rating != null) {
				sum += ((r.rating() - avg1) * (rating.rating() - avg2));
			}
		}

		if (sumWeight == 0) {
			return 0d; // si l'utilisateur donne tout le temps la même note...
		} else {
			return sum / sumWeight;
		}
	}

	public static Map<Integer, Double> userSimilarities(Map<Integer, List<Rating>> userPartitionnedRatings, Integer user) {
		Map<Integer, Double> similarities = new HashMap<Integer, Double>();
		for (Entry<Integer, List<Rating>> entry : userPartitionnedRatings.entrySet()) {
			similarities.put(entry.getKey(), userSimilarity(userPartitionnedRatings, user, entry.getKey()));
		}
		return similarities;
	}

	public static Double movieSimilarity(Map<Integer, List<Rating>> moviePartitionnedRatings, Integer movie1, Integer movie2) {
		return movieSimilarity(moviePartitionnedRatings.get(movie1), moviePartitionnedRatings.get(movie2));
	}

	public static Double movieSimilarity(List<Rating> ratings1, List<Rating> ratings2) {
		Double avg1 = DataOperation.average(ratings1);
		Double avg2 = DataOperation.average(ratings2);

		Double sum = 0d, sumWeight = DataOperation.deviation(ratings1, avg1) * DataOperation.deviation(ratings2, avg2);
		for (Rating r : ratings1) {
			Rating rating = DataFilter.ratingForUser(ratings2, r.user());
			if (rating != null) {
				sum += ((r.rating() - avg1) * (rating.rating() - avg2));
			}
		}

		if (sumWeight == 0) {
			return 0d; // si le film a tout le temps la même note...
		} else {
			return sum / sumWeight;
		}
	}

	public static Map<Integer, Double> movieSimilarities(Map<Integer, List<Rating>> moviePartitionnedRatings, Integer movie) {
		Map<Integer, Double> similarities = new HashMap<Integer, Double>();
		for (Entry<Integer, List<Rating>> entry : moviePartitionnedRatings.entrySet()) {
			similarities.put(entry.getKey(), movieSimilarity(moviePartitionnedRatings, movie, entry.getKey()));
		}
		return similarities;
	}
}