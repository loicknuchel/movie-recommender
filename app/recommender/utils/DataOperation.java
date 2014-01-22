package recommender.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Rating;

public class DataOperation {
	public static Double average(List<Rating> ratings) {
		Double sum = 0d;
		for (Rating r : ratings) {
			sum += r.rating();
		}
		return sum / ratings.size();
	}

	public static Double deviation(List<Rating> ratings) {
		return deviation(ratings, average(ratings));
	}

	public static Double deviation(List<Rating> ratings, Double avg) {
		Double sum = 0d;
		for (Rating r : ratings) {
			sum += Math.pow(r.rating() - avg, 2);
		}
		return Math.sqrt(sum / ratings.size());
	}

	public static Double denormalize(Double normalizedRating, Double avg, Double deviation) {
		return (normalizedRating * deviation) + avg;
	}

	public static Double normalize(Double rating, Double avg, Double deviation) {
		if (deviation == 0)
			return avg;
		else
			return (rating - avg) / deviation;
	}

	public static Double norm(Map<String, Double> vector) {
		Double norm = 0.0;
		for (Entry<String, Double> entry : vector.entrySet()) {
			norm += Math.pow(entry.getValue(), 2);
		}
		return Math.sqrt(norm);
	}

	public static Double scalarProduct(Map<String, Double> v1, Map<String, Double> v2) {
		Double product = 0.0;
		for (Entry<String, Double> entry : v1.entrySet()) {
			if (v2.get(entry.getKey()) != null) {
				product += entry.getValue() * v2.get(entry.getKey());
			}
		}
		return product;
	}

	public static Double vectorCosine(Map<String, Double> v1, Map<String, Double> v2) {
		return scalarProduct(v1, v2) / (norm(v1) * norm(v2));
	}

	public static Map<Integer, List<Rating>> groupByUser(List<Rating> ratings) {
		Map<Integer, List<Rating>> userPartitionnedRatings = new HashMap<Integer, List<Rating>>();
		for (Rating r : ratings) {
			if (userPartitionnedRatings.get(r.user()) == null) {
				userPartitionnedRatings.put(r.user(), new ArrayList<Rating>());
			}
			userPartitionnedRatings.get(r.user()).add(r);
		}
		return userPartitionnedRatings;
	}
}
