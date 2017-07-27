// http://typelevel.org/blog/2016/09/19/variance-phantom.html

sealed abstract class Gimme[+P]
case object AStr extends Gimme[String]
case object AnInt extends Gimme[Int]

sealed abstract class Gotme[-P]
case object UnStr extends Gotme[String]
case object UnInt extends Gotme[Int]


def gimme[P](g: Gimme[P]): (P, P) = g match {
  case AStr =>
    // implicitly[P =:= String] //  will fail for P covariant
    // implicitly[P <:< String] //  will fail for P covariant
    implicitly[String <:< P]
    ("hi", "there")
  case AnInt =>
    (42, 84)
}

gimme(AStr)
gimme(AnInt)

def mklength[P](g: Gotme[P]): P => Int = g match {
  case UnStr =>
    // implicitly[P =:= String]   will fail
    implicitly[P <:< String]
    // implicitly[String <:< P]   will fail
    (s: String) => s.length
  case UnInt => identity[Int]
}

mklength(UnStr)
mklength(UnInt)