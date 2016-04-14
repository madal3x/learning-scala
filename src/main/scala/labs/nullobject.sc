import scala.collection.mutable

case class User(id: Int, name: String)

class UserRepository {
  private val repository: mutable.Map[Int, User] = mutable.Map.empty

  def findById(id: Int): Option[User] = {
    if (repository.contains(id)) Some(repository(id)) else None
  }

  def addUser(user: User): Unit = {
    repository(user.id) = user
  }
}

val userRepository = new UserRepository
userRepository.addUser(User(1, "unu"))
userRepository.addUser(User(2, "doi"))

userRepository.findById(1)
userRepository.findById(3)



/*val map = Map[String, Int](
  "unu" -> 1,
  "doi" -> 2,
  "trei" -> null
)*/

val map = mutable.HashMap[String, String](
  "unu" -> "unu",
  "doi" -> "doi",
  "trei" -> null
)

map.get("unu")
map.get("trei")
map.get("patru").map(v => v)

None.map(v => v)


def sum(a: Int, b: Int) = a + b

val a = Some(3)
val b = None

for {
  a0 <- a
  b0 <- b
} yield sum(a0, b0)

val l = List(Some(5), None, Some(4))

// removes Some and leaves wrapped value, removes None
// does the same as flatMap(_)
l.flatten

l(0).filter(_ > 0)
l(1).filter(_ > 0)

case class M(c: Option[Int])

case class MS(m1: Option[M], m2: Option[M], m3: Option[M]) {
  println(Seq(m1, m2, m3).flatten.map(_.c))
  println(Seq(m1, m2, m3).flatMap(m => Seq(m)))
}

MS(Some(M(Some(5))), Some(M(None)), None)