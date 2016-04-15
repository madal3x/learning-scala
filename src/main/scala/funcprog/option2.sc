case class Employee(name: String, dept: String)
val employeesByName: Map[String, Employee] =
  List(Employee("Alice", "R&D"), Employee("Bob", "Accounting"))
    .map(e => (e.name, e)).toMap

val dept: scala.Option[String] = employeesByName.get("Joe").map(_.dept)


def lift[A, B](f: A => B): Option[A] => Option[B] = x => x map f

lift[Int, Int](x => x + 1)

import java.util.regex._

def pattern(s: String): Option[Pattern] =
  try {
    Some(Pattern.compile(s))
  } catch {
    case e: PatternSyntaxException => None
  }

def mkMatcher(pat: String): Option[String => Boolean] =
  pattern(pat) map (p => (s: String) => p.matcher(s).matches)

def bothMatch(pat: String, pat2: String, s: String): Option[Boolean] =
  for {
    f <- mkMatcher(pat)
    g <- mkMatcher(pat2)
  } yield f(s) && g(s)

def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
  a flatMap (aa => b map (bb => f(aa, bb)))

def bothMatch_2(pat1: String, pat2: String, s: String): Option[Boolean] = {
  map2(mkMatcher(pat1), mkMatcher(pat2))((fa, fb) => fa(s) && fb(s))
}

bothMatch_2("[0-9]", "[1-5]", "4")

// ??? ->
def sequence[A](a: List[Option[A]]): Option[List[A]] = a match {
  case Nil => Some(Nil)
  case h :: t => h flatMap (hh => sequence(t) map (hh :: _))
}
sequence(List(Some(1), Some(2), Some(3), None, Some(4)))
sequence(List(Some(1), Some(2), Some(3), Some(4)))
def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] =
  a match {
    case Nil => Some(Nil)
    case h :: t => map2(f(h), traverse(t)(f))(_ :: _)
  }
def traverse_1[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] =
  a.foldRight[Option[List[B]]](Some(Nil))((h, t) => map2(f(h), t)(_ :: _))
def sequenceViaTraverse[A](a: List[Option[A]]): Option[List[A]] =
  traverse(a)(x => x)
traverse(List("abcde", "def", "a"))(x => if (x.size > 1) Some(x) else None)
traverse(List("abcde", "def", "ab"))(x => if (x.size > 1) Some(x) else None)