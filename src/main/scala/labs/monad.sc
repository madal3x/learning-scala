val countries = List("India", "China")
val capitals = Map(
  "India" -> "New Delhi",
  "China" -> "Beijing"
)

countries.flatMap(i => capitals.get(i).orElse(Some("None")).map(j => j))
