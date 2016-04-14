val capitalOfCountry = Map("US" -> "Washington", "Switzerland" -> "Bern")

// throws NoSuchElementException
//capitalOfCountry("Andorra")

// wraps in an option
capitalOfCountry get "Andorra"
capitalOfCountry get "US"

val fruit = List("orange", "apple", "pear", "pineapple")

fruit sortWith(_.length < _.length)
fruit.sorted


fruit.groupBy(_.head)

class Poly(terms0: Map[Int, Double]) {
  val terms = terms0 withDefaultValue 0.0

  def this(bindings: (Int, Double)*) =
    this(bindings.toMap)

  //def +(other: Poly) = new Poly(terms ++ (other.terms map adjust))
    // terms ++ other.terms overwrites the values of keys from terms
    // with the values of common keys from other.terms
    //new Poly(terms ++ other.terms)

  def +(other: Poly) = new Poly((other.terms foldLeft terms)(addTerm))

  def addTerm(terms: Map[Int, Double], term: (Int, Double)): Map[Int, Double] = {
    val (exp, coeff) = term
    terms + (exp -> (coeff + terms(exp)))
  }

  // without withDefaultValue
  /*def adjust(term: (Int, Double)): (Int, Double) = {
    val (exp, coeff) = term
    terms get exp match {
      case Some(coeff1) => exp -> (coeff + coeff1)
      case None => exp -> coeff
    }
  }*/
  def adjust(term: (Int, Double)): (Int, Double) = {
    val (exp, coeff) = term
    exp -> (coeff + terms(exp))
  }

  override def toString =
    (for ((exp, coeff) <- terms.toList.sorted.reverse) yield coeff + "x^" + exp) mkString " + "
}
val p1 = new Poly(Map(1 -> 2.0, 3 -> 4.0, 5 -> 6.2))
val p2 = new Poly(Map(0 -> 3.0, 3 -> 7.0))
val p3 = new Poly(1 -> 5.4, 5 -> 4.5)
p1 + p2 + p3