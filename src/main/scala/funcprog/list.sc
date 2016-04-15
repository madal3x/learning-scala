sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]
object List {
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }
  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }
  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def tail[A](xs: List[A]): List[A] = xs match {
    case Nil => throw new Error("Nil.tail")
    case Cons(x, xs) => xs
  }

  def drop[A](xs: List[A], n: Int): List[A] = (xs, n) match {
    case (_, 0) => xs
    case (Nil, _) => Nil
    case (Cons(x, xs), n) => drop(xs, n - 1)
  }

  // two parameter lists to improve type inference, early ones to define the types
  def dropWhile[A](xs: List[A])(p: A => Boolean): List[A] = xs match {
    case Nil => Nil
    case Cons(y, ys) => if (p(y)) dropWhile(ys)(p) else Cons(y, ys)
  }

  def setHead[A](h: A)(xs: List[A]) =
    xs match {
      case Nil => Cons(h, Nil)
      case Cons(y, ys) => Cons(h, ys)
    }

  def init[A](xs: List[A]): List[A] =
    xs match {
      case Nil => Nil
      case Cons(y, Nil) => Nil
      case Cons(y, ys) => init(ys)
    }

  def foldRight[A, B](l: List[A], z: B)(f: (A, B) => B): B =
    l match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(l: List[Int]) =
    foldRight(l, 0.0)(_ + _)

  def product2(l: List[Double]) =
    foldRight(l, 1.0)(_ * _)

  def length2[A](l: List[A]): Int = foldRight(l, 0)((a: A, b: Int) => 1 + b)

  def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B =
    l match {
      case Nil => z
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
    }

  def sum3(l: List[Int]) =
    foldLeft(l, 0)(_ + _)

  def product3(l: List[Int]) =
    foldLeft(l, 1)(_ * _)

  def length3[A](l: List[A]): Int = foldLeft(l, 0)((b, a) => b + 1)

  def reverse[A](l: List[A]): List[A] = foldLeft(l, Nil: List[A])((b, a) => Cons(a, b))

  def foldLeftViaFoldRight[A, B](l: List[A], z: B)(f: (B, A) => B): B = ???
  def foldRightViaFoldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B = ???
  def concatListOfLists[A](l: List[List[A]]): List[A] = ???

  def add1(l: List[Int]): List[Int] =
    l match {
      case Nil => Nil
      case Cons(x, xs) => Cons(x + 1, add1(xs))
    }

  def map[A, B](l: List[A])(f: A => B): List[B] =
    l match {
      case Nil => Nil
      case Cons(x, xs) => Cons(f(x), map(xs)(f))
    }

  def filter[A](l: List[A])(p: A => Boolean): List[A] =
    l match {
      case Nil => Nil
      case Cons(x, xs) => if (p(x)) Cons(x, filter(xs)(p)) else filter(xs)(p)
    }

  // ???
  def append[A](l: List[A], la: List[A]): List[A] =
    l match {
      case Nil => la
      case Cons(x, Nil) => Cons(x, la)
      case Cons(x, xs) => Cons(x, append(xs, la))
    }

  // ???
  def flatMap[A, B](l: List[A])(f: A => List[B]): List[B] =
    l match {
      case Nil => Nil
      case Cons(x, xs) => append(f(x), flatMap(xs)(f))
    }

  def filterViaFlatMap[A](l: List[A])(p: A => Boolean): List[A] = ???

  def hasSubsequence(l: List[Int], s: List[Int]): Boolean = (l, s) match {
    case (_, Nil) => true
    case (Nil, _) => false
    case (Cons(l, ls), Cons(x, xs)) =>
      if (l == x) hasSubsequence(ls, xs) else hasSubsequence(ls, s)
  }
}

val example = Cons(1, Cons(2, Cons(3, Nil)))
val example2 = List(1, 2, 3)
val total = List.sum(example)

val x = List(1, 2, 3, 4, 5) match {
  case Cons(x, Cons(2, Cons(4, _))) => x
  case Nil => 42
  case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
  case Cons(h, t) => h + List.sum(t)
  case _ => 101
}

List.drop(List(1, 2, 3, 4), 2)

List.foldRight(List(1, 2, 3), Nil: List[Int])(Cons(_, _))

List.hasSubsequence(List(1, 2, 3, 4, 5), List(3, 4))
List.hasSubsequence(List(1, 2, 3, 4, 5), List(3, 6))
