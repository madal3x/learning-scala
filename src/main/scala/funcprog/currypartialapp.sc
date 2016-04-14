def partial1[A,B,C](a: A, f: (A,B) => C): B => C = {
  f(a,_)
}

def sumWith5 = partial1(5, (a: Int, b: Int) => a + b)
sumWith5(3)

def curry[A, B, C](a: A, f: (A,B) => C): A => (B => C) = {
  def f1(a: A): B => C = {
    def f2(b: B): C = f(a,b)

    f2
  }

  f1
}

def uncurry[A,B,C](f: A => B => C): (A, B) => C = {
  def f1(a: A, b: B): C = {
    f(a)(b)
  }

  f1
}

def compose[A,B,C](f: B => C, g: A => B): A => C = {
  def fg(a: A): C = {
    f(g(a))
  }

  fg
}

val f = (a: Int) => a + 2
val g = (a: Int) => a * 2

(f andThen g)(3) // g(f(a))
(f compose g)(3) // f(g(a))