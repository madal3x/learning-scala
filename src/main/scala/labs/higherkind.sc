/*trait Iterable[T] {
  def filter(p: T ⇒ Boolean): Iterable[T]
  def remove(p: T ⇒ Boolean): Iterable[T] = filter (x ⇒ !p(x))
}

trait List[T] extends Iterable[T] {
  def filter(p: T ⇒ Boolean): List[T]
  override def remove(p: T ⇒ Boolean): List[T] =
    filter (x ⇒ !p(x))
}*/

trait Iterable[T, Container[_]] {
  def filter(p: T => Boolean): Container[T]
  def remove(p: T => Boolean): Container[T] =
    filter (x => !p(x))
}
trait List[T] extends Iterable[T, List]