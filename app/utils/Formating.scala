package utils

object Formating {
  def roundAt(n: Double, p: Int): Double = { val s = math pow (10, p); (math rint n * s) / s }
}