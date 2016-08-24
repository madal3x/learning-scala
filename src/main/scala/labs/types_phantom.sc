// http://gigiigig.github.io/tlp-step-by-step/phantom-types.html

trait Status
trait Open extends Status
trait Closed extends Status

trait Door[S <: Status]
object Door {
  def apply[S <: Status] = new Door[S] {}

  def open[S <: Closed](d: Door[S]) = Door[Open]
  def close[S <: Open](d: Door[S]) = Door[Closed]
}

val closedDoor = Door[Closed]
val openDoor = Door.open(closedDoor)
val closedAgainDoor = Door.close(openDoor)

// val closedClosedDoor = Door.close(closedDoor)
// val openOpenDoor = Door.open(openDoor)