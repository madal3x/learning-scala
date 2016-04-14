val a = Array(1, 2, 3, 4)
for (i <- a) yield i * 2
a.mkString(" and ")
a(3)