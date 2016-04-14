import math.Ordering

def msort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
  xs.length / 2 match {
    case 0 => xs
    case n =>
      val (fst, snd) = xs splitAt n
      merge(msort(fst), msort(snd))
  }
}

def merge[T](xs: List[T], ys: List[T])(implicit ord: Ordering[T]): List[T] = (xs, ys) match {
  case (Nil, _) => ys
  case (_, Nil) => xs
  case (x :: xs1, y :: ys1) =>
    if (ord.lt(x, y)) x :: merge(xs1, ys) else y :: merge(xs, ys1)
}


msort(List(19,11,14,15))
msort(List("b","a","e","d"))