object colors {
  class ValidColor private (val colorString: String) extends AnyVal
  object ValidColor {
    private def apply(colorString: String): ValidColor = new ValidColor(colorString)

    val BLUE = ValidColor(scala.io.AnsiColor.BLUE)
  }

  // new ValidColor("balskdjaklsdhasd") // will not compile.

  def onlyAcceptValidColors(color: ValidColor): String = color.colorString
}


