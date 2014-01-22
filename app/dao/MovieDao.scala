package dao

import models.Movie
import scala.io.Source
import models.Movie

object MovieDao {
  val file = "data/movies.csv"
  val separator = ";"

  def loadMovies(): Map[Int, Movie] = {
    val f = Source.fromFile(file)
    val dataset = f.getLines.drop(1).filter(s => s.length > 0).map(line => {
      val cell = line.split(separator)
      (cell(0).toInt, new Movie(
        cell(0).toInt,
        cell(1),
        cell(3).toDouble,
        cell(4),
        cell(5).split(",").map(s => s.trim).toList,
        cell(6).split(",").map(s => s.trim).toList,
        cell(7).split(",").map(s => s.trim).toList,
        cell(8)))
    }).toMap
    f.close
    dataset
  }
}