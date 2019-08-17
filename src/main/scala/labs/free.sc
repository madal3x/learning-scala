sealed trait Free[F[_], A] {
  def flatMap[B](fb: A => Free[F, B]): Free[F, B] = this match {
    case Return(a) =>
      fb(a)
    case Bind(fi, fa) =>
      Bind(fi, fa andThen (_ flatMap fb))
  }
}

// fa: I => Free[F, A]
// fb: A => Free[F, B]
// r:  Free[F, B]

case class Return[F[_], A](a: A) extends Free[F, A]
case class Bind[F[_], I, A](fi: F[I], f: I => Free[F, A]) extends Free[F, A]

