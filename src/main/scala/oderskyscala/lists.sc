object lists {
  // x :: xs - prepend x to xs
  // xs ::: ys - prepend xs to ys
  // xs ++ ys - append xs to ys

  def init[T](xs: List[T]): List[T] = xs match {
  	case List() => List()
  	case List(x) => List()
  	case y :: ys => y :: init(ys)
  }
  init(List(1,2,3))
  
  def last[T](xs: List[T]): T = xs match {
  	case List() => throw new Error("empty list head")
  	case List(x) => x
  	case y :: ys => last(ys)
  }
  last(List(1,2,3))
  
  def reverse[T](xs: List[T]): List[T] = xs match {
  	case List() => List()
  	case y :: ys => reverse(ys) ++ List(y)
  }
  reverse(List(1,2,3))

  def removeAt[T](n: Int, xs: List[T]): List[T] = (xs take n) ::: (xs drop n + 1)
  removeAt(2, List(1,2,3))
  def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case List() => ys
    case z :: zs => z :: concat(zs, ys)
  }
  concat(List(1,2,3), List(4,5,6))

  def isort(xs: List[Int]): List[Int] = xs match {
    case List() => List()
    case y :: ys => insert(y, isort(ys))
  }
  def insert(x: Int, xs: List[Int]): List[Int] = xs match {
    case List() => List(x)
    case y :: ys => if (x < y) x :: xs else y :: insert(x, ys)
  }
  isort(List(19,11,14,5))

  def msort(xs: List[Int]): List[Int] = {
    xs.length / 2 match {
      case 0 => xs
      case n =>
        val (fst, snd) = xs splitAt n
        merge(msort(fst), msort(snd))
    }
  }

  def merge(xs: List[Int], ys: List[Int]): List[Int] = (xs, ys) match {
    case (Nil, _) => ys
    case (_, Nil) => xs
    case (x :: xs1, y :: ys1) =>
      if (x < y) x :: merge(xs1, ys) else y :: merge(xs, ys1)
  }

  msort(List(19,11,14,15))

  def mapFun[T, U](xs: List[T], f: T => U): List[U] =
    (xs foldRight List[U]())((a, b) => f(a) :: b)
  mapFun(List(1,2,3), (i: Int) => i*2)

  def lengthFun[T](xs: List[T]): Int =
    (xs foldRight 0)((a, b) => b + 1)
  lengthFun(List(1,2,3))

  def concatFun[T](xs: List[T], ys: List[T]): List[T] =
    (xs foldRight ys)(_ :: _)
  concatFun(List(1,2,3), List(4,5,6))
}