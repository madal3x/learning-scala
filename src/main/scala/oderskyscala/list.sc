object list {
  println("Welcome to the Scala worksheet")
  
  def singleton[T](elem: T): List[T] = new Cons(elem, Nil)

  def nth[T](n: Int, l: List[T]): T =
  	if (l.isEmpty) throw new IndexOutOfBoundsException
  	else if (n == 0) l.head
  	else nth(n - 1, l.tail)
  
  singleton(1)
  singleton(false)
  
  nth(2, new Cons(10, new Cons(11, new Cons(12, new Cons(13, Nil)))))
                                                  
  List()
  List(2, 3)
}

object List {
	def apply[T](x1: T, x2: T) = new Cons(x1, new Cons(x2, Nil))
	def apply[T](): List[T] = Nil
}

/*
turn class Nil into singleton with type parameter covariance (T+, Nil <: Nothing)
trait List[T] {
	def isEmpty: Boolean
	def head: T
	def tail: List[T]
}
*/

trait List[+T] {
	def isEmpty: Boolean
	def head: T
	def tail: List[T]
	def prepend(elem: T): List[T] = new Cons(elem, this)
}
/*
class Nil[T] extends List[T] {
	def isEmpty = true
	def head = throw new NoSuchElementException("Nil.head")
	def tail = throw new NoSuchElementException("Nil.tail")
}
*/
object Nil extends List[Nothing] {
	def isEmpty = true
	def head = throw new NoSuchElementException("Nil.head")
	def tail = throw new NoSuchElementException("Nil.tail")
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
	def isEmpty = false
}