package recommender;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import models.Movie;
import recommender.collaborativeFiltering.CFRecommender;
import recommender.nonPersonalized.Association;
import recommender.utils.MyCollectionUtils;
import dao.DB;

public class Recommender {
	public static Map<Integer, Double> getSimilarMovies(Integer movieId) {
		long start = System.currentTimeMillis();

		// Map<Integer, Double> similarMovies = randomMovies();
		Map<Integer, Double> similarMovies = Association.normalizedAssociation(movieId);

		long stop = System.currentTimeMillis();
		System.out.println("getSimilarMovies(" + movieId + ") done in " + (stop - start) + " ms");
		return similarMovies;
	}

	public static Map<Integer, Double> getRecommendedMovies(Integer userId) {
		long start = System.currentTimeMillis();

		// Map<Integer, Double> recommendedMovies = randomMovies();
		Map<Integer, Double> recommendedMovies = CFRecommender.ponderatedAverageRecommender(userId);

		long stop = System.currentTimeMillis();
		System.out.println("getRecommendedMovies(" + userId + ") done in " + (stop - start) + " ms");
		return recommendedMovies;
	}

	public static Map<Integer, Double> randomMovies() {
		Map<Integer, Double> randomMovies = new HashMap<Integer, Double>();
		Map<Object, Movie> movies = DB.getMovies();
		for (Entry<Object, Movie> entry : movies.entrySet()) {
			randomMovies.put((Integer) entry.getKey(), Math.random() * 5);
		}
		return MyCollectionUtils.sortByValue(randomMovies, false);
	}

}
