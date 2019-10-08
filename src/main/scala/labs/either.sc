// https://www.youtube.com/watch?v=UjSQlUjuZWQ


def either[C, A, B](f: A => C, g: B => C): Either[A, B] => C =
  _.fold(f, g)
