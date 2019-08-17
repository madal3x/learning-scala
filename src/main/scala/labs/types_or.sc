// https://stackoverflow.com/questions/3508077/how-to-define-type-disjunction-union-types

class StringOrInt[T]
object StringOrInt {
  implicit object IntWitness extends StringOrInt[Int]
  implicit object StringWitness extends StringOrInt[String]
}

def foo[T: StringOrInt](x: T) = x match {
  case x: String => x
  case x: Int => x
}

foo("")
foo(0)

type ¬[A] = A => Nothing
type ∨[T, U] = ¬[¬[T] with ¬[U]]
type ¬¬[A] = ¬[¬[A]]
type |∨|[T, U] = { type λ[X] = ¬¬[X] <:< (T ∨ U) }

def size[T : (Int |∨| String)#λ](t : T) = t match {
  case i : Int => i
  case s : String => s.length
}

size(4)
size("abc")