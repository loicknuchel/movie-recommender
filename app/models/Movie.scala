package models

import scala.collection.JavaConverters._
case class Movie(
  id: Int,
  name: String,
  rating: Double,
  director: String,
  stars: List[String],
  tags: List[String],
  keywords: List[String],
  description: String) {
  def getStars: java.util.List[String] = stars.asJava
  def getTags: java.util.List[String] = tags.asJava
  def getKeywords: java.util.List[String] = keywords.asJava
  def url = "/assets/images/movies/" + id + ".jpg"
}
  