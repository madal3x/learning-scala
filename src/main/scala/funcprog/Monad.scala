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

  def factor[A,B](ma: M[A], mb: M[B]): M[(A, B)] =
    map2(ma, mb)((_, _))

  def cofactor[A,B](e: Either[M[A], M[B]]): M[Either[A, B]] = e match {
    case Left(ma) =>
      map(ma)(Left(_))
    case Right(mb) =>
      map(mb)(Right(_))
  }

  def compose[A,B,C](f: A => M[B], g: B => M[C]): A => M[C] =
    a => flatMap(f(a))(g)

  def flatMapFromCompose[A,B](ma: M[A])(f: A => M[B]): M[B] =
    compose((_: Unit) => ma, f)()

  /** compose(f, unit) == f && compose(unit, f) == f */
  def idLawCompose[A,B](f: A => M[B]) = (a: A) =>
    compose(f, (b: B) => unit(b))(a) == f(a) &&
      compose((_: Unit) => unit(a), f)(a) == f(a)

  /** flatMap(f(a))(unit) == flatMap(unit(a))(f) */
  def idLawFlatMap[A,B](f: A => M[B]) = (a: A) =>
    flatMap(f(a))(unit) == flatMap(unit(a))(f)

  def join[A](mma: M[M[A]]): M[A] =
    flatMap(mma)(ma => ma)

  def flatMapFromJoin[A,B](ma: M[A])(f: A => M[B]): M[B] =
    join(map(ma)(f))

  def composeFromJoin[A,B,C](f: A => M[B], g: B => M[C]): A => M[C] = (a: A) =>
    join(map(f(a))(g))

  /** join(unit(unit(a))) == unit(a) */
  def idLawJoin[A,B] = (a: A) =>
    join(unit(unit(a))) == unit(a)
}

object State {
  case class State[S, A](run: S => (A, S)) {
    def map[B](f: A => B): State[S, B] = State(s => {
      val (a, s1) = run(s)
      (f(a), s1)
    })
    def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
      val (a, s1) = run(s)
      f(a).run(s1)
    })
  }

  type IntState[A] = State[Int, A]
  object intStateMonad extends Monad[IntState] {
    def unit[A](a: => A): IntState[A] = State(s => (a, s))
    def flatMap[A, B](ma: IntState[A])(f: A => IntState[B]): IntState[B] =
      ma.flatMap(f)
  }
}



object Monad {
  val optionMonad = new Monad[Option] {
    def unit[A](a: => A): Option[A] =
      Option(a)
    def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] =
      ma.flatMap(f)
  }
}
