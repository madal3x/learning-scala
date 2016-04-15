def from(n: Int): Stream[Int] = n #:: from(n + 1)

val nats = from(0)

// Sieve of Eratosthenes
def sieve(s: Stream[Int]): Stream[Int] =
  s.head #:: sieve(s.tail filter (_ % s.head != 0))

sieve(from(2)).take(100).toList

// decouple a converging sequence from the termination criteria
def sqrtStream(x: Double): Stream[Double] = {
  def improve(guess: Double) = (guess + x / guess) / 2

  lazy val guesses: Stream[Double] = 1 #:: (guesses map improve)

  guesses
}

def isGoodEnough(guess: Double, x: Double) =
  math.abs((guess * guess - x) / x) < 0.0001

sqrtStream(4).take(10).toList

sqrtStream(4).filter(isGoodEnough(_, 4))