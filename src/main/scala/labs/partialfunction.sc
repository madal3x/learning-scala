// a PartialFunction is a function that is defined only for some input values
// a PartialFunction is a subtype of a Function

val one: PartialFunction[Int, String] = {case 1 => "one"}

one(1)
one.isDefinedAt(1)
one.isDefinedAt(2)


// case is a PartialFunction
case class Name(f: String, l: String)
val catalog = List(Name("alex", "madan"), Name("cristi", "dirva"))
catalog.filter({case Name(f, l) => f(0) == 'a'})


// composing multiple PartialFunction
val two: PartialFunction[Int, String] = {case 2 => "two"}
def three: PartialFunction[Int, String] = {case 3 => "three"}
val something: PartialFunction[Int, String] = {case _ => "something"}

val partial = one orElse two orElse three orElse something
partial(1)
partial(2)
partial(3)
partial(100)