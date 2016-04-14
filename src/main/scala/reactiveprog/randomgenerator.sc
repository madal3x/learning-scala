import java.util.Random

trait Generator[+T] {
  // alias for this
  self =>

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    def generate: S = f(self.generate)
    // or without the self alias
    //def generate = f(Generator.this.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate: S = f(self.generate).generate
  }
}

val integers = new Generator[Int] {
  val rand = new Random()
  def generate = rand.nextInt()
}

/*val booleans = new Generator[Boolean] {
  def generate = integers.generate > 0
}*/

val booleans = for {x <- integers} yield x > 0
// translates to
/*val booleans = new Generator[Boolean] {
  def generate = ((x: Int) => x > 0)(integers.generate)
}*/

def pairs[T, U](t: Generator[T], u: Generator[U]) = for {
  x <- t
  y <- u
} yield (x, y)

val intbools = pairs(integers, booleans)

integers.generate
integers.generate

booleans.generate
booleans.generate
booleans.generate
booleans.generate

intbools.generate
intbools.generate