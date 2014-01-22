package dao

import models.Rating
import scala.io.Source
import models.Movie

object RatingDao {
  val file = "data/ratings.csv"
  val separator = ";"

  def loadRatings: List[Rating] = {
    val f = Source.fromFile(file)
    val dataset = f.getLines.drop(1).filter(s => s.length > 0).map(line => {
      val cell = line.split(separator)
      new Rating(
        cell(0).toInt,
        cell(1).toInt,
        cell(2).toDouble)
    }).toList
    f.close
    dataset
  }

  def userRatings(id: Int): List[Rating] = {
    List(
      Rating(id, 1891, 4), // Star Wars: Episode V - The Empire Strikes Back (1980)
      Rating(id, 121, 5), // The Lord of the Rings: The Two Towers (2002)
      Rating(id, 603, 4.5), // The Matrix (1999)
      Rating(id, 272, 3.5), // Batman Begins (2005)
      Rating(id, 2164, 5), // Stargate (1994)
      Rating(id, 664, 1), // Titanic (1997)
      Rating(id, 3049, 0.5), // Ace Ventura: Pet Detective (1994)
      Rating(id, 640, 3), // Catch Me If You Can (2002)
      Rating(id, 462, 1), // Erin Brockovich (2000)
      Rating(id, 1597, 2) // Meet the Parents (2000)
      )
  }
}