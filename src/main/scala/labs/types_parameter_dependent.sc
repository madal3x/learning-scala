// http://gigiigig.github.io/tlp-step-by-step/dependent-types.html

trait Foo {
  trait Bar
  def bar: Bar
}

def foo(f: Foo): f.Bar = f.bar