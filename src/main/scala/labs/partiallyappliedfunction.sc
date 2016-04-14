def sum3op(a: Int, b: Int, c: Int) = a + b + c

def sum2opWith5 = sum3op(5, _, _)


def sum3opReloaded(a: Int)(b: Int, c: Int) = a + b + c

def sum2opWith5Reloaded = sum3opReloaded(5) _