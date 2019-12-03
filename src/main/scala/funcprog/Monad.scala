package funcprog

trait Functor[F[_]] {
  def map[A,B](fa: F[A])(f: A => B): F[B]
}

object Functor {
  val listFunctor = new Functor[List] {
    def map[A,B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }
}

trait Monad[M[_]] extends Functor[M] {
  def unit[A](a: => A): M[A]
  def flatMap[A,B](ma: M[A])(f: A => M[B]): M[B]

  def map[A,B](ma: M[A])(f: A => B): M[B] =
    flatMap(ma)(a => unit(f(a)))

  def map2[A,B,C](ma: M[A], mb: M[B])(f: (A,B) => C): M[C] =
    flatMap(ma)(a => map(mb)(b => f(a,b)))

  def sequence[A](lma: List[M[A]]): M[List[A]] =
    lma.foldRight(unit(List[A]()))((ma, mla) => map2(ma, mla)(_ :: _))

  def traverse[A,B](la: List[A])(f: A => M[B]): M[List[B]] =
    sequence(la.map(f))
}

object Monad {
  val optionMonad = new Monad[Option] {
    def unit[A](a: => A): Option[A] =
      Option(a)
    def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] =
      ma.flatMap(f)
  }
}
