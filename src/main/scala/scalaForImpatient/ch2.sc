import java.net.MalformedURLException

import math._

// if has as return type the common supertype of both branches
val x = -1
val s = if (x > 0) 1 else -1
val s1 = if (x > 0) 1
val s2 = if (x > 0) "positive" else -1

// separate multiple expressions/statements on the same line with ;
var r = 0
var n = 0
if (n > 0) {
  r = r * n; n -= 1
}

// when expression is on multiple lines, end the line in smth that makes it clear that it continues (like method, open block)
val tot = x + s +
  r + n

if (n > 0) {
  r = r * n
  n -= 1
}

// blocks have as return value the last expression value; if it's an assignment it's type is Unit
val dx, dy, x0, y0, y = 0
val distance = {
  val dx = x - x0
  val dy = y - y0
  sqrt(dx * dx + dy * dy) // return value
}

// assignments' return type is Unit
var r1: Unit = ()
var r2: Int = 1
r1 = r2 = 1

// output
println("Answer: " + 42)
printf("Hello, %s! You are %d years old.\n", "Fred", 42)

// loops
// multiple generators
for (i <- 1 to 3; j <- 1 to 3) print((10 * i + j) + " ")
// generator with guard
for (i <- 1 to 3; j <- 1 to 3 if i != j) print((10 * i + j) + " ")
// yield constructs a collection of values made from each iteration
for (i <- 1 to 10) yield i % 3

// functions
def fac(n: Int) = {
  var r = 1
  for (i <- 1 to n) r = r * i
  r // return value
}

// default values for arguments
def decorate(str: String, left: String = "[", right: String = "]") =
  left + str + right

// calling a function with named arguments
decorate(left = "<<<", str = "Hello", right = ">>>")

// combining unnamed with named arguments (unnamed must be first)
decorate("Hello", right = "]<<<")

// variable number of arguments (args is a Seq)
def sum(args: Int*) = {
  var result = 0
  for (arg <- args) result += arg
  result
}

val total = sum(1, 4, 6, 8, 10)

// val s = sum(1 to 10) not allowed, need to tell compiler to consider Seq as argument sequence
val total2 = sum(1 to 10: _*)


// procedures (return type Unit, no preceding = sign)
def box(s: String) {
  // Look carefully: no =
  val border = "-" * s.length + "--\n"
  println(border + "|" + s + "|\n" + border)
}

// Evaluated as soon as words is defined
val words1 = scala.io.Source.fromFile("/usr/share/dict/words").take(5).mkString
// Evaluated the first time words is used
lazy val words2 = scala.io.Source.fromFile("/usr/share/dict/words").take(5).mkString
// Evaluated every time words is used
def words3 = scala.io.Source.fromFile("/usr/share/dict/words").take(5).mkString

// exceptions
// the return type of if is given by the branch without throw
// throw return's type is Nothing
val x2 = 10
if (x2 >= 0) {
  sqrt(x)
} else
  throw new IllegalArgumentException("x should not be negative")
// pattern matching for catching exceptions
// use _ for variable name if you don't need it
val url = "url"
try {
  // open url
} catch {
  case _: MalformedURLException => println("Bad URL: " + url)
  case ex: IllegalArgumentException => ex.printStackTrace()
}


// exercises
def signum(x: Int) = {
  if (x < 0) -1
  else {
    if (x == 0) 0
    else 1
  }
}

for (i <- 10 to 1 by -1)
  println(i)

def countdown(n: Int): Unit = {
  n to 1 by -1 foreach println
}
countdown(10)

def unicodeProduct(str: String) = {
  var prod = 1
  for (char <- str)
    prod *= char.charValue()
  prod
}
unicodeProduct("Hello")

def unicodeProductFun(str: String) =
  str.foldLeft(1)((prod, el) => prod * el.charValue())

unicodeProductFun("Hello")

def unicodeProductForFun(str: String) = {
  val l = for (char <- str) yield char.charValue().toInt

  l.product
}

unicodeProductForFun("Hello")

def unicodeProductRecursive(str: String): Int = {
  if (str.length == 1)
    str(0).charValue()
  else
    str.head.charValue() * unicodeProductRecursive(str.tail)
}
unicodeProductRecursive("Hello")

case class Address(streetName: String) {
  println(streetName)
}

val address1 = Address("Koningslaan")
val address2 = Address("Koningslaan")

address1 == address2
