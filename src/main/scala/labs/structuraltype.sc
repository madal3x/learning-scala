import scala.language.reflectiveCalls

trait Foo {
  def doFoo: String
}

type FooLike = {
  def doFoo: String
}

class LikeFoo {
  def doFoo: String = "foo"
}

class LikeFooDelegate extends LikeFoo with Foo{
  override def doFoo: String = super.doFoo
}

def takesFoo(foo: Foo) = foo.doFoo
def takesFooLike(foo: FooLike) = foo.doFoo

takesFooLike(new LikeFoo)
takesFoo(new LikeFooDelegate)