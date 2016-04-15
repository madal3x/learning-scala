val o: Option[Int] = Some(2)
o match {
  // match and keep reference
  case x@Some(2) => println(x)
  /*case Some(x) => println(x)*/
  case x: Some[Int] => println(x)
  case None => println("none")
  case _ => println("other")
}


val NameTagPattern = "Hello my name is (.+) and (.+)".r
val ListPattern = "First: (.+), Last: (.+)".r

case class Name(f: String, l: String)

def extractName(s: String): Option[Name] = {
  Option(s).collectFirst {
    case NameTagPattern(f, l) => Name(f, l)
    case ListPattern(f, l) => Name(f, l)
  }
}

extractName("Hello my name is firstname and lastname")
extractName("First: alex, Last: mad")

//Option(line) collect { case r(group) => group }

case class Calculator(brand: String, model: String)

val hp20b = Calculator("hp", "20b")

def calcType(c: Calculator) = c match {
  case Calculator("hp", "20b") => "scientific"
  case Calculator(brand, model) => s"brand $brand model $model"
  case _ => "normal"
  // or
  //case Calculator(_, _) => "Calculator of unknown type"
}

calcType(hp20b)
calcType(Calculator("sony", "87"))


val tuple1 = ('+', 23, 47)
val tuple2 = ('-', 56, 45)

def twoOpCalc(in: (Char, Int, Int)) = in match {
  case ('+', a, b) => a + b
  case ('-', a, b) => a - b
  case _ => None
}

twoOpCalc(tuple1)
twoOpCalc(tuple2)


val txt =
  """
window.mapDivId = '''map0Div'''
window.map0Div =
lat: 46.963432,
lng: 11.010631,
zoom: null,
locId: 1144995,
geoId: 608679,
isAttraction: false,
isEatery: false,
isLodging: true,"""

val latP = "lat: ([^,].*),".r.unanchored
val lngP = "lng: ([^,].*),".r.unanchored
Option(txt) collect {case latP(lat) => lat}
Option(txt) collect {case lngP(lng) => lng}
"(?s).*lat: (.*),.*".r.findFirstMatchIn(txt)
//.*lng: (.*^,),

