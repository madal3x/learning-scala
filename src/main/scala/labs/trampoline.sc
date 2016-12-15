// https://skillsmatter.com/skillscasts/3244-stackless-scala-free-monads

case class State[S,+A](runS: S => (A,S)) {
  def map[B](f: A => B) =
    State[S,B](s => {
      val (a, s1) = runS(s)
      (f(a), s1)
    })

  def flatMap[B](f: A => State[S,B]) =
    State[S,B](s => {
      val (a, s1) = runS(s)
      f(a).runS(s1)
    })
}

object State {
  def get[S]: State[S,S] =
    State(s => (s,s))

  def set[S](s: S): State[S,Unit] =
    State(_ => ((), s))

  def pure[S,A](a: A): State[S,A] =
    State(s => (a,s))
}

def zipIndex[A](as: List[A]): List[(Int, A)] =
  as.foldLeft(State.pure[Int, List[(Int, A)]](List()))(
    (acc, a) =>
      for {
        xs <- acc
        n <- State.get
        _ <- State.set(n + 1)
      } yield (n,a) :: xs
  ).runS(0)._1.reverse

zipIndex(List(9,8,7))

sealed trait Trampoline[+A] {
  final def runT: A = this match {
    case More(k) =>
      k().runT
    case Done(v) =>
      v
  }
}

case class More[+A](k: () => Trampoline[A]) extends Trampoline[A]
case class Done[+A](result: A) extends Trampoline[A]

def even[A](ns: List[A]): Boolean =
  ns match {
    case Nil => true
    case x :: xs => odd(xs)
  }

def odd[A](ns: List[A]): Boolean =
  ns match {
    case Nil => false
    case x :: xs => even(xs)
  }

def evenT[A](ns: List[A]): Trampoline[Boolean] =
  ns match {
    case Nil => Done(true)
    case x :: xs => More(() => oddT(xs))
  }

def oddT[A](ns: List[A]): Trampoline[Boolean] =
  ns match {
    case Nil => Done(false)
    case x :: xs => More(() => evenT(xs))
  }

evenT(Range(1,1000000).toList).runT

