package dao

import scala.collection.JavaConverters._
import models.Movie
import models.Rating
import models.MovieStat

object DB {
  val userId = 10000
  val likeThreshold = 2.0
  val movies = MovieDao.loadMovies
  val ratings = RatingDao.loadRatings ++ RatingDao.userRatings(userId)
  val movieStats = evaluateMovieStats(movies, ratings)

  def getMovies: java.util.Map[Int, Movie] = movies.asJava
  def getRatings: java.util.List[Rating] = ratings.asJava

  private def evaluateMovieStats(movies: Map[Int, Movie], ratings: List[Rating]): Map[Int, MovieStat] = {
    def like(rating: Double): Int = if (rating > likeThreshold) 1 else 0
    def dislike(rating: Double): Int = if (rating > likeThreshold) 0 else 1

    val movieStats = ratings.groupBy(r => r.movie).map({
      case (id, ratings) => (id, ratings.foldLeft((0, 0.0, 0, 0)) {
        (acc, rating) => (acc._1 + 1, acc._2 + rating.rating, acc._3 + like(rating.rating), acc._4 + dislike(rating.rating))
      })
    }).map({ case (id, stats) => (id, MovieStat(stats._2 / stats._1, stats._1, stats._3, stats._4)) })
    movieStats
  }
}