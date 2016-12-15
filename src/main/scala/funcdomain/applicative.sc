trait Functor[F[_]] {
  def map[A,B](fa: F[A])(f: A => B): F[B]
}

trait Applicative[F[_]] extends Functor[F] {
  def point[A](a: => A): F[A]

  def ap[A,B](fa: => F[A])(f: => F[A => B]): F[B]

  def lift[A,B](f: A => B): F[A] => F[B] = fa =>
    ap(fa)(point(f))

  def apply2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = {
    val a = map(fa)(f.curried)
    ap(fb)(a)
  }

  def lift2[A,B,C](f: (A, B) => C): (F[A], F[B]) => F[C] =
    apply2(_, _)(f)

  def apply3[A,B,C,D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] = {
    val fff: F[A=>B=>C=>D] = point(f.curried)
    val a: F[B=>C=>D] = ap(fa)(fff)
    val b: F[C=>D] = ap(fb)(a)
    val c: F[D] = ap(fc)(b)

    c
  }

  def lift3[A,B,C,D](f: (A, B, C) => D): (F[A], F[B], F[C]) => F[D] =
    apply3(_, _, _)(f)


  def unit[A](a: => A): F[A] = point(a)

  def traverse[A,B](as: List[A])(f: A => F[B]): F[List[B]] =
    as.foldRight(unit(List[B]()))((a, fbs) => apply2(f(a), fbs)(_ :: _))

  def sequence[A](fas: List[F[A]]): F[List[A]] = ???

}

object OptionApplicative extends Applicative[Option] {
  def point[A](a: => A): Option[A] = Option(a)

  def ap[A, B](fa: => Option[A])(f: => Option[(A) => B]): Option[B] = (fa, f) match {
    case (Some(a), Some(ff)) => Some(ff(a))
    case _ => None
  }

  def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = fa match {
    case Some(a) => Some(f(a))
    case _ => None
  }
}

object ListApplicative extends Applicative[List] {
  def point[A](a: => A): List[A] = List(a)

  def ap[A, B](fa: => List[A])(f: => List[(A) => B]): List[B] =
    for {
      a <- fa
      ff <- f
    } yield ff(a)

  /*(fa, f) match {
    case (x :: xs, ff :: ffs) => ff(x) :: ap(xs)(ffs)
    case _ => Nil
  }*/

  def map[A, B](fa: List[A])(f: (A) => B): List[B] = fa match {
    case x :: xs => f(x) :: map(xs)(f)
    case _ => Nil
  }
}

/*trait Applicative2[F[_]] extends Functor[F] {
  def map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = {
    val a = map(fa)(f.curried)
    apply(a)(fb)
  }

  def map3[A,B,C,D](fa: F[A],
                    fb: F[B],
                    fc: F[C])(f: (A, B, C) => D): F[D] = {
    val fa = unit(f.curried)
    val a = apply()/*(fa)*/
  }
    /*apply(
      apply(
        apply(unit(f.curried))
        (fa)
      )(fb)
    )(fc)*/

  def apply[A,B](f: F[A => B])(fa: F[A]): F[B]
  def unit[A](a: A): F[A]
}*/
