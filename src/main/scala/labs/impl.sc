object impls {
  implicit val fun: String => Int = _.length
}

def implarg(s: String)(implicit myfun: String => Int): Int =
  myfun(s) + 10

import impls.fun

implarg("hello")