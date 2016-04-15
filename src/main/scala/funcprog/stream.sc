object Stream {
  def empty[A]: Stream[A] =
    new Stream[A] {
      def uncons = None
    }

  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] =
    new Stream[A] {
      lazy val uncons = Some((hd, tl))
    }

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty)
      empty
    else
      cons(as.head, apply(as.tail: _*))

  def constant[A](a: A): Stream[A] = cons(a, constant(a))

  def from(n: Int): Stream[Int] = cons(n, from(n + 1))

  val fibs: Stream[Int] = {
    def loop(pp: Int, p: Int): Stream[Int] =
      cons(pp, loop(p, pp + p))

    loop(0, 1)
  }

  // ???
  // tying the knot, corecursion - https://github.com/fpinscala/fpinscala/wiki/Chapter-5:-Strictness-and-laziness
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
    f(z) match {
      case Some((h, s)) => cons(h, unfold(s)(f))
      case None => empty
    }

  val fibsViaUnfold =
    unfold((0, 1)) {case (f0, f1) => Some((f0, (f1, f0 + f1)))}

  def fromViaUnfold(n: Int) =
    unfold(n)(n => Some((n, n + 1)))

  def constantViaUnfold[A](a: A) =
    unfold(a)(_ => Some((a, a)))

  val onesViaUnfold =
    unfold(1)(_ => Some((1, 1)))
}

trait Stream[A] {
  def uncons: Option[(A, Stream[A])]

  def toList: List[A] = uncons match {
    case None => Nil
    case Some((a, as)) => a :: as.toList
  }

  def take(n: Int): Stream[A] = (n, uncons) match {
    case (0, _) | (_, None) => Stream.empty[A]
    case (_, Some((h, t))) => Stream.cons(h, t.take(n - 1))
  }

  def takeWhile(p: A => Boolean): Stream[A] = uncons match {
    case None => Stream.empty[A]
    case Some((h, t)) => if (p(h)) Stream.cons(h, t.takeWhile(p)) else Stream.empty[A]
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B =
    uncons match {
      case Some((h, t)) => f(h, t.foldRight(z)(f))
      case None => z
    }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b)

  def forAll(p: A => Boolean): Boolean =
    foldRight(true)((a, b) => p(a) && b)

  def takeWhileWithFoldRight(p: A => Boolean): Stream[A] =
    foldRight(Stream.empty[A])((a, b) => if (p(a)) Stream.cons(a, b) else Stream.empty)

  def map[B](f: A => B): Stream[B] = uncons match {
    case None => Stream.empty[B]
    case Some((a, as)) => Stream.cons(f(a), as.map(f))
  }
  def filter(p: A => Boolean): Stream[A] = uncons match {
    case None => Stream.empty[A]
    case Some((a, as)) => if (p(a)) Stream.cons(a, as.filter(p)) else as.filter(p)
  }
  def foreach[U](f: A => U): Unit = uncons match {
    case None => Nil
    case Some((a, as)) => f(a); as.foreach(f)
  }
}
Stream(1, 2, 3).toList
Stream(1, 2, 3, 4, 5).take(3).toList
Stream(2, 4, 6, 7, 8).takeWhile(_ % 2 == 0).toList
Stream(1, 2, 3).exists(_ % 2 == 0)
Stream(2, 4, 6).forAll(_ % 2 == 0)
Stream(2, 4, 5, 6).forAll(_ % 2 == 0)
Stream(2, 4, 6, 7, 8).takeWhileWithFoldRight(_ % 2 == 0).toList
val s = Stream(1, 2, 3, 4).map(e => {
  println("m:" + e); e + 10
}).filter(e => {
  println("e:" + e); e % 2 == 0
})
// infinite stream
val ones: Stream[Int] = Stream.cons(1, ones)
ones.map(_ + 1).filter(_ == 2)
// on function applied on stream it needs to be able to calculate the first element
// following example will not stop, as it cannot compute the first element
//ones.filter(_ > 1)

Stream.fibs.take(6).foreach(println)
Stream.fibsViaUnfold.take(6).foreach(println)