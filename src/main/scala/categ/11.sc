// https://bartoszmilewski.com/2014/10/28/category-theory-for-programmers-the-preface/

def id[A](x: A): A = x

def goff[A, B, C](f: A => B, g: B => C): A => C =
  (a: A) => g(f(a))

def myf(x: Int): Int = x*5

goff[Int, Int, Int](myf, id)(5) == goff[Int, Int, Int](id, myf)(5)
goff[Int, Int, Int](myf, id)(5) == myf(5)

