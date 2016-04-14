import java.text.SimpleDateFormat
import java.util.Date

trait Addable[T] {
  def zero: T
  def append(a: T, b: T): T
}

implicit object IntIsAddable extends Addable[Int] {
  def zero = 0
  def append(a: Int, b: Int) = a + b
}

implicit object StringIsAddable extends Addable[String] {
  def zero = ""
  def append(a: String, b: String) = a + b
}

def sum[T](xs: List[T])(implicit addable: Addable[T]) =
  xs.foldLeft(addable.zero)(addable.append)

//or the same thing, using context bounds:

def sum2[T : Addable](xs: List[T]) = {
  val addable = implicitly[Addable[T]]
  xs.foldLeft(addable.zero)(addable.append)
}

val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
implicit def stringToDateOption(s: String): Option[Date] =
  Option(dateFormat.parse(s))

"2014-01-01".get.getTime