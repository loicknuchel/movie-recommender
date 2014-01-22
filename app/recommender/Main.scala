package recommender

import dao.MovieDao
import dao.RatingDao

object Main {
  def main(args: Array[String]) {
    val movies = MovieDao.loadMovies
    val ratings = RatingDao.loadRatings

    println(RatingDao.loadRatings.length)
  }
}