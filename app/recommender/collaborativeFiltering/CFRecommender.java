package recommender.collaborativeFiltering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Rating;
import recommender.utils.DataFilter;
import recommender.utils.DataOperation;
import recommender.utils.MyCollectionUtils;
import dao.DB;

public class CFRecommender {
	
	/**
	 * Make movie recommendations for the sprecified user with the <b>simpleAverage</b>
	 * predictor
	 * 
	 * @param userId
	 * @return movieId recommended with predicted rating
	 */
	public static Map<Integer, Double> simpleAverageRecommender(Integer userId) {
		List<Rating> ratings = DB.getRatings();
		List<Rating> userProfile = DataFilter.ratingsForUser(ratings, userId);

		Map<Integer, Double> moviesToPredict = new HashMap<Integer, Double>();
		for (Rating r : ratings) {
			if (!movieExistInRatings(userProfile, r.movie())) {
				moviesToPredict.put(r.movie(), null);
			}
		}
		for (Entry<Integer, Double> entry : moviesToPredict.entrySet()) {
			moviesToPredict.put(entry.getKey(), CFPredictor.simpleAverage(ratings, entry.getKey()));
		}
		return MyCollectionUtils.sortByValue(moviesToPredict, false);
	}

	/**
	 * Make movie recommendations for the sprecified user with the <b>ponderatedAverage</b>
	 * predictor
	 * 
	 * @param userId
	 * @return movieId recommended with predicted rating
	 */
	public static Map<Integer, Double> ponderatedAverageRecommender(Integer userId) {
		List<Rating> ratings = DB.getRatings();
		Map<Integer, List<Rating>> userPartitionnedRatings = DataOperation.groupByUser(ratings);
		Map<Integer, Double> similarities = CFPredictor.userSimilarities(userPartitionnedRatings, userId);
		List<Rating> userProfile = DataFilter.ratingsForUser(ratings, userId);

		Map<Integer, Double> moviesToPredict = new HashMap<Integer, Double>();
		for (Rating r : ratings) {
			if (!movieExistInRatings(userProfile, r.movie())) {
				moviesToPredict.put(r.movie(), null);
			}
		}

		for (Entry<Integer, Double> entry : moviesToPredict.entrySet()) {
			moviesToPredict.put(entry.getKey(), CFPredictor.ponderatedAverage(ratings, userId, entry.getKey(), userPartitionnedRatings, similarities));
		}
		return MyCollectionUtils.sortByValue(moviesToPredict, false);
	}

	/**
	 * Make movie recommendations for the sprecified user with the <b>normalizedPonderatedAverage</b>
	 * predictor
	 * 
	 * @param userId
	 * @return movieId recommended with predicted rating
	 */
	public static Map<Integer, Double> normalizedPonderatedAverageRecommender(Integer userId) {
		List<Rating> ratings = DB.getRatings();
		Map<Integer, List<Rating>> userPartitionnedRatings = DataOperation.groupByUser(ratings);
		Map<Integer, Double> similarities = CFPredictor.userSimilarities(userPartitionnedRatings, userId);
		List<Rating> userProfile = DataFilter.ratingsForUser(ratings, userId);

		Map<Integer, Double> moviesToPredict = new HashMap<Integer, Double>();
		for (Rating r : ratings) {
			if (!movieExistInRatings(userProfile, r.movie())) {
				moviesToPredict.put(r.movie(), null);
			}
		}

		for (Entry<Integer, Double> entry : moviesToPredict.entrySet()) {
			moviesToPredict
					.put(entry.getKey(), CFPredictor.normalizedPonderatedAverage(ratings, userId, entry.getKey(), userPartitionnedRatings, similarities));
		}
		return MyCollectionUtils.sortByValue(moviesToPredict, false);
	}

	private static boolean movieExistInRatings(List<Rating> ratings, Integer movie) {
		for (Rating r : ratings) {
			if (r.movie() == movie) {
				return true;
			}
		}
		return false;
	}
}
