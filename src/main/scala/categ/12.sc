import scala.collection.mutable

case class Memo[I, O](f: I => O) extends (I => O) {
  private val cache = mutable.Map.empty[I, O]
  def apply(x: I): O = cache.getOrElseUpdate(x, f(x))
}
