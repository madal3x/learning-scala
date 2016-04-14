// g(f(x))

def f(s: String): String = s"f($s)"
def g(s: String): String = s"g($s)"

def gfx = g _ compose f _

gfx("hello")

def gfx2 = f _ andThen g _

gfx2("hello")