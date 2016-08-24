import scala.annotation.tailrec

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {
  def simple(seed: Long): RNG = new RNG {
    def nextInt = {
      val seed2 = (seed*0x5DEECE66DL + 0xBL) &
        ((1L << 48) - 1)

      ((seed2 >>> 16).asInstanceOf[Int],
        simple(seed2))
    }
  }
}

def positiveInt(rng: RNG): (Int, RNG) = {
  val (i, r) = rng.nextInt
  (if (i < 0) (i + 1).abs else i, r)
}

val rngSimple = RNG.simple(11231241L)

positiveInt(rngSimple)
positiveInt(rngSimple)
val (p, rp) = positiveInt(rngSimple)

positiveInt(rp)

def double(rng: RNG): (Double, RNG) = {
  val (n, r) = positiveInt(rng)
  (n / (Int.MaxValue.toDouble + 1), r)
}

val (d, rd) = double(rngSimple)
double(rd)


def intDouble(rng: RNG): ((Int,Double), RNG) = {
  val (ni, ri) = rng.nextInt
  val (nd, rd) = double(rng)

  ((ni, nd), rd)
}

intDouble(rngSimple)

def doubleInt(rng: RNG): ((Double,Int), RNG) = {
  val ((ni, nd), r) = intDouble(rng)

  ((nd, ni), r)
}

doubleInt(rngSimple)

def double3(rng: RNG): ((Double,Double,Double), RNG) = {
  val (nd1, rd1) = double(rng)
  val (nd2, rd2) = double(rd1)
  val (nd3, rd3) = double(rd2)

  ((nd1, nd2, nd3), rd3)
}

double3(rngSimple)

def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
  @tailrec
  def tailrec(n: Int, l: List[Int], rng: RNG): (List[Int], RNG) = {
    if (n == 0)
      (l, rng)
    else {
      val (i, r) = rng.nextInt

      tailrec(n-1, i :: l, r)
    }
  }

  tailrec(count, List(), rng)
}

ints(10)(rngSimple)



object State {
  def unit[S,A](a: A): State[S, A] =
    State(s => (a, s))

  def sequence[S,A](xs: List[State[S,A]]): State[S, List[A]] =
    xs.foldRight(unit[S, List[A]](List())) {
      (f, z) => f.map2(z)(_ :: _)
    }

  def get[S]: State[S, S] =
    State(s => (s, s))

  def set[S](s: S): State[S, Unit] =
    State(_ => ((), s))

  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get
    _ <- set(f(s))
  } yield ()
}

//type State[S,+A] = S => (A,S)
case class State[S,+A](run: S => (A,S)) {
  def map[B](f: A => B): State[S,B] =
    flatMap(a =>
      State.unit(f(a)))

  def map2[B,C](sb: State[S,B])(f: (A,B) => C): State[S,C] =
    flatMap(a =>
      sb.map(b =>
        f(a,b)))

  def flatMap[B](f: A => State[S,B]): State[S,B] =
    State(s => {
      val (a, s2) = run(s)
      f(a).run(s2)
    })
}


type Rand[A] = State[RNG, A]

val int: Rand[Int] = State(s => s.nextInt)

def positiveMax(n: Int): Rand[Int] =
  int.map(_ % n)

positiveMax(10).run(rngSimple)
positiveMax(10).run(rngSimple)

val _double: Rand[Double] =
  int.map(_ / (Int.MaxValue.toDouble + 1))

_double.run(rngSimple)
_double.run(rngSimple)

def _intDouble: Rand[(Int, Double)] =
  for {
    i <- int
    d <- _double
  } yield (i, d)

_intDouble.run(rngSimple)

def _doubleInt: Rand[(Double, Int)] =
  for {
    d <- _double
    i <- int
  } yield (d, i)

_doubleInt.run(rngSimple)

def _ints(count: Int): Rand[List[Int]] =
  State.sequence(List.fill(count)(int))

_ints(10).run(rngSimple)



