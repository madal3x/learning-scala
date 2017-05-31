// https://thedet.wordpress.com/2012/04/28/functors-monads-applicatives-can-be-so-simple/

case class MyBox[T](value: T)

val boxedstring = MyBox("hello")


def rawLengthOf(s: String): Int = s.length
def map[A,B](f: A => B): MyBox[A] => MyBox[B] =
  (a: MyBox[A]) => MyBox(f(a.value))
map(rawLengthOf)(boxedstring)


def boxedLengthOfRes(s: String): MyBox[Int] = MyBox(s.length)
def flatMap[A,B](f: A => MyBox[B]): MyBox[A] => MyBox[B] =
  (a: MyBox[A]) => f(a.value)
flatMap(boxedLengthOfRes)(boxedstring)


def boxedLengthOf: MyBox[String => Int] = MyBox(s => s.length)
def ap[A,B](f: MyBox[A => B]): MyBox[A] => MyBox[B] =
  (a: MyBox[A]) => MyBox(f.value(a.value))
ap(boxedLengthOf)(boxedstring)