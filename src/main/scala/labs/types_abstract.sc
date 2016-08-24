trait Foo {
  type T
  def value: T
}

object FooString extends Foo {
  type T = String
  def value: T = "ciao"
}

object FooInt extends Foo {
  type T = Int
  def value: T = 1
}

def getValue(f: Foo): f.T = f.value

val fs: String = getValue(FooString)
val fi: Int = getValue(FooInt)