class Calculator(brand: String) {
  val color = brand match {
    case "HP" => "blue"
    case "Apple" => "white"
    case _ => "grey"
  }
}

val calc = new Calculator("HP")
calc.color

// functions vs methods
class C{
  var acc = 0
  val methodInc = acc+=1
  def functionInc = () => acc+=1
}

val c = new C
c.methodInc
c.functionInc // this returns the function without executing it
c.acc
class ScientificCalculator(brand: String) extends Calculator(brand) {
  def log(m: Double, base: Double) = math.log(m) / math.log(base)
}

val sc = new ScientificCalculator("Apple")
sc.color
abstract class Shape {
  def getArea: Int
  val volume: Int
}

class Square extends Shape {
  def getArea = 123
  val volume = 0
}


trait Cache[K, V] {
  def get(key: K)
  def put(key: K, value: V)
  def delete(key: K)
}

def remove[K](key: K) = ()

class Foo(s: String)
// companion object, used as factory
// apply removes the need of using new
object Foo {
  def apply(s: String) = new Foo(s)
}

val f = Foo("hello")

// apply allows object to be used as a function

class Bar {
  def apply() = 0
}

val bar = new Bar
bar()

// singleton
object Counter {
  var counter = 0

  def incCounter() = counter += 1
}

Counter.incCounter()
Counter.incCounter()
Counter.counter


object addOne extends Function1[Int, Int] {
  def apply(m: Int): Int = m + 1
}

addOne(1)


class AddOne extends (Int => Int) {
  def apply(m: Int): Int = m + 1
}

val plusOne = new AddOne

plusOne(1)