/*
type of sum is
(Int => Int) => (Int, Int) => Int
eq to
(Int => Int) => ( (Int, Int) => Int )
(function types associate to the right)
 */
def sum(f: Int => Int)(a: Int, b: Int): Int = {
  if (a > b) 0 else f(a) + sum(f)(a+1, b)
}

/*
sum(f)(a, b) = g(a, b)
 */

def sumInts = sum(x => x) _
def sumCubes = sum(x => x * x *x) _
sumInts(1, 3)
sumCubes(1, 3)
def fact(x: Int): Int = if (x == 0) 1 else x * fact(x-1)
def sumTL(f: Int => Int)(a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a > b) acc else loop(a+1, acc+f(a))
  }

  loop(a, 0)
}
def sumIntsTL = sumTL(x => x) _
def sumCubesTL = sumTL(x => x * x * x) _
sumIntsTL(1, 3)
sumCubesTL(1, 3)

/*
def f(args1)(args2)...(argsn) = E
equivalent to

def f(args1)(args2)...(argsn-1) = { def g(argsn) = E; g }

or for short

def f(args1)(args2)...(argsn-1) = (argsn => E)

equivalent to
f
def f = (args1 => (args2 => ... (argsn => E)...))
*/
def sumCurry(f: Int => Int): (Int, Int) => Int = {
  def sumF(a: Int, b: Int): Int = {
    if (a > b) 0 else f(a) + sumF(a + 1, b)
  }

  sumF
}
def sumIntsCurry = sumCurry(x => x)
def sumCubesCurry = sumCurry(x => x * x * x)
sumIntsCurry(1, 3)
sumCubesCurry(1, 3)

def product(f: Int => Int)(a: Int, b: Int): Int = {
  if (a > b) 1 else f(a) * product(f)(a + 1, b)
}
def factorial(n: Int) = product(x => x)(1, n)

def mapReduce(f: Int => Int, combine: (Int, Int) => Int, zero: Int)(a: Int, b: Int): Int = {
  if (a > b) zero else combine(f(a), mapReduce(f, combine, zero)(a + 1, b))
}

def sumIntsMR = mapReduce(x => x, (a, b) => a + b, 0) _
sumIntsMR(1, 3)

def productMR = mapReduce(x => x, (a, b) => a * b, 1) _
productMR(1, 3)