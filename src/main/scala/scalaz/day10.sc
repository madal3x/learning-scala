import scalaz._, Scalaz._

def myName(step: String): Reader[String, String] = Reader { step + ", I am " + _ }

def dy: Reader[String, String] = Reader { _ + "dy" }

def localExample: Reader[String, (String, String, String)] =
  for {
    a <- myName("First")
    b <- myName("Second") >=> dy
    c <- myName("Third")
  } yield (a, b, c)

localExample("Fred")
