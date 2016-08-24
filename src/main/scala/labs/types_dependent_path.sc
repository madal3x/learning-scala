// http://gigiigig.github.io/tlp-step-by-step/dependent-types.html

/*
# means that we don’t refer to any specific instance, in this case Foo#Bar,
every Bar inside every instance of Foo will be a valid instance

. means that we can only refer the Bar instances that belong to a specif instance of Foo
*/

class Foo {
  class Bar
}

val foo1 = new Foo
val foo2 = new Foo

val a: Foo#Bar = new foo1.Bar
val b: Foo#Bar = new foo2.Bar

val c: foo1.Bar = new foo1.Bar
// val d: foo2.Bar = new foo1.Bar
// [error]  found   : console.foo1.Bar
// [error]  required: console.foo2.Bar