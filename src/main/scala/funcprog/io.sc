object io {
  trait IO[+A] { self =>
    def run: A

    def map[B](f: A => B): IO[B] =
      new IO[B] { def run = f(self.run) }

    def flatMap[B](f: A => IO[B]): IO[B] =
      new IO[B] { def run = f(self.run).run }
  }

  def fahrenheitToCelsius(f: Double): Double =
    (f - 32) * 5.0/9.0

  /*def ReadLine: IO[String] = IO { readLine }
  def PrintLine(msg: String): IO[Unit] = IO { println(msg) }
  def converter:*/
}