package oderskyscala

object repeatuntil {
  class Repeat(command: => Unit) {
    final def until(condition: => Boolean): Unit = {
      command
      if (condition) () else until(condition)
    }
  }

  object repeat {
    def apply(command: => Unit): Repeat = {
      new Repeat(command)
    }
  }

  var i = 0
  repeat {
    i += 1
  } until (i > 10)

  def repeat2(command: => Unit) = new {
    def until(condition: => Boolean): Unit = {
      command
      if (condition) () else until(condition)
    }
  }

  var j = 0
  repeat2 {
    j += 1
  } until (j > 10)
}