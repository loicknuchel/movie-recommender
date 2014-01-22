package controllers

import play.api._
import play.api.mvc._
import dao.MovieDao
import dao.RatingDao
import recommender.Recommender
import scala.collection.JavaConverters._
import dao.DB
import models.Movie

object Application extends Controller {
  def movieList(movies: Map[Int, Movie]): List[Movie] = DB.movies.map(p => p._2).toList.sortBy(m => -DB.movieStats(m.id).avgRating)

  def index = Action {
    Redirect(routes.Application.movies)
  }

  def movies = Action {
    Ok(views.html.movies(movieList(DB.movies), DB.movieStats, "movies"))
  }

  def taggedMovies(tag: String) = Action {
    Ok(views.html.movies(movieList(DB.movies).filter(m => m.tags.contains(tag)), DB.movieStats, "movies in #" + tag))
  }

  def directedMovies(director: String) = Action {
    Ok(views.html.movies(movieList(DB.movies).filter(m => m.director.equals(director)), DB.movieStats, "movies directed by " + director))
  }

  def moviesWithStar(star: String) = Action {
    Ok(views.html.movies(movieList(DB.movies).filter(m => m.stars.contains(star)), DB.movieStats, "movies with " + star))
  }

  def movie(id: Int) = Action {
    val similarMovies = Recommender.getSimilarMovies(id).asScala
      .filterKeys(movie => movie != id).map({ case (id, sim) => (DB.movies(id), sim.toDouble) }).toList
      .sortBy(m => -m._2).take(5)
    Ok(views.html.movie(DB.movies(id), DB.movieStats(id), similarMovies))
  }

  def profiles = Action {
    Redirect(routes.Application.profile(DB.userId))
  }

  def profile(id: Int) = Action {
    val userRatings = DB.ratings.filter(r => r.user == id)
    val ratedMovies = userRatings.map(rating => rating.movie)
    val recommendedMovies = Recommender.getRecommendedMovies(id).asScala
      .filterKeys(movie => !ratedMovies.contains(movie)).map({ case (id, sim) => (DB.movies(id), sim.toDouble) }).toList
      .sortBy(m => -m._2).take(4)
    Ok(views.html.profile(userRatings.sortBy(r => -r.rating), recommendedMovies, DB.movies, DB.movieStats))
  }

}