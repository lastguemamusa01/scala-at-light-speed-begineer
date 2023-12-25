package advacedlectures.part4implicits

object Givens extends App {

  // Givens in scala 3 is like implicits in scala 2
  val aList = List(2,4,3,1)

  val anOrderedList = aList.sorted // implicit Ordering[Int]

  // Scala 2 style
  object Implicits {
    implicit val descedingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }

  // scala 3 style
  object Givens {
    // given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _) // implicit val is given instances
  }
  // given instances or values or gives <==> implicits vals
  // doesn't matter the which order is written. this will pick as implicit arguments

  println(anOrderedList)

  // instantiating an anonymous class
  object GivenAnonymousClassNaive {
//    given descendingOrdering_v2: Ordering[Int] = new Ordering[Int] { // anonymous class
//      override def compare(x: Int, y: Int): Int = x - y
//    }
  }

  object GivenWith {
//    given descendingOrdering_v3: Ordering[Int] with { // with shorting the code and proper way of defining given instances for traits
//      override def compare(x: Int, y: Int): Int = x - y // when you need to override a bunch of methods
//    }
  }

  // for use the Givens you need to import it

  // import GivenWith._ // in scala 3, this import does not import givens as well , very hard to trace the where implicits comming from in scala 2
  // import GivenWith.given // in scala 3, imports all givens
  // import GivenWith.descendingOrdering_v3 // if you want specfic given

  // implicit arguments <==> using clauses

  def extremes[A](list: List[A])(implicit ordering: Ordering[A]): (A, A) = {
    val sortedList = list.sorted
    (sortedList.head, sortedList.last)
  }


  // in scala 3 using clause is replace implicit arguemnts of scala 2

//  def extremes_v2[A](list: List[A])(using ordering: Ordering[A]): (A, A) = {
//    val sortedList = list.sorted
//    (sortedList.head, sortedList.last)
//  }

  // implicit def - in scala 2 are used to create new implicit values based on existing implicit values
  // used to synthesize new implicit values

  trait Combinator[A] { // in math this is called semi-group
    def combine(x: A, y: A): A
  }

  // List(1,2,3) < List(2,3,4)   sum of the list
  implicit def listOrdering[A](implicit simpleOrdering: Ordering[A], combinator: Combinator[A]): Ordering[List[A]] =
    new Ordering[List[A]] {
      override def compare(x: List[A], y: List[A]): Int = {
        val sumX = x.reduce(combinator.combine)
        val sumY = y.reduce(combinator.combine)
        simpleOrdering.compare(sumX , sumY)

      }
    }

  // equivalent in scala 3 with givens

//  given listOrdering_v2[A](using simpleOrdering: Ordering[A], combinator: Combinator[A]): Ordering[List[A]] with {
//    override def compare(x: List[A], y: List[A]): Int = {
//      val sumX = x.reduce (combinator.combine)
//      val sumY = y.reduce (combinator.combine)
//      simpleOrdering.compare (sumX, sumY)
//
//    }
//  }

  // implicit conversions (abused in scala 2) -> not recommended using in scala 2 and scala 3

  case class Person(name: String) {
    def greet(): String = s"Hi, my name is $name"
  }

  implicit def string2Person(string: String): Person = Person(string)

  val minGreet = "Min".greet() // string2Person("Min").greet() -> pain in the ass in the large code base -> discoraged use it

  // in Scala 3
//  import scala.language.implicitConversions // required in scala 3 -> to do conversion
//  given string2PersonConversion: Conversion[String, Person] with { // import and instantiate Conversion - show that your are doing some conversions to another devs
//    override def apply(x: String) = Person(x)
//  }



}
