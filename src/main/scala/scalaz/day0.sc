def sum(xs: List[Int]) = xs.foldLeft(0)(_ + _)

trait Monoid[A] {
  def mappend(a: A, b: A): A
  def mzero: A
}

object IntMonoid extends Monoid[Int] {
  def mappend(a: Int, b: Int) = a + b
  def mzero = 0
}

def sum2(xs: List[Int]) = xs.foldLeft(IntMonoid.mzero)(IntMonoid.mappend)

def sum3(xs: List[Int], m: Monoid[Int]) = xs.foldLeft(m.mzero)(m.mappend)


implicit val intMonoid = IntMonoid
def sum4[A](xs: List[A])(implicit m: Monoid[A]) = xs.foldLeft(m.mzero)(m.mappend)

// context bound param
def sum5[A: Monoid](xs: List[A]) = {
  val m = implicitly[Monoid[A]]
  xs.foldLeft(m.mzero)(m.mappend)
}

object Monoid {
  implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  implicit val StringMonoid: Monoid[String] = new Monoid[String] {
    def mappend(a: String, b: String) = a + b
    def mzero = ""
  }
}

def sum6[A: Monoid](xs: List[A]): A = {
  val m = implicitly[Monoid[A]]
  xs.foldLeft(m.mzero)(m.mappend)
}

val multiMonoid: Monoid[Int] = new Monoid[Int] {
  def mappend(a: Int, b: Int): Int = a * b
  def mzero: Int = 1
}

sum6(List(1, 2, 3, 4))(multiMonoid)


object FoldLeftList {
  def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B): B = xs.foldLeft(b)(f)
}

def sum7[A: Monoid](xs: List[A]) = {
  val m = implicitly[Monoid[A]]
  FoldLeftList.foldLeft(xs, m.mzero, m.mappend)
}

trait FoldLeft[F[_]] {
  def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B): B
}

object FoldLeft {
  implicit val FoldLeftList: FoldLeft[List] = new FoldLeft[List] {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B): B = xs.foldLeft(b)(f)
  }
}

def sum8[M[_]: FoldLeft, A: Monoid](xs: M[A]): A = {
  val m = implicitly[Monoid[A]]
  val fl = implicitly[FoldLeft[M]]

  fl.foldLeft(xs, m.mzero, m.mappend)
}

sum8(List(1,2,3,4))
sum8(List("a","b","c"))

trait MonoidOp[A] {
  val F: Monoid[A]
  val value: A
  def |+|(a2: A) = F.mappend(value, a2)
}

implicit def toMonoidOp[A: Monoid](a: A): MonoidOp[A] = new MonoidOp[A] {
  val F = implicitly[Monoid[A]]
  val value = a
}

1 |+| 2
"a" |+| "b"
