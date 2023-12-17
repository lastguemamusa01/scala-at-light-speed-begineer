package advacedlectures.part4implicits

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1, 4, 5, 3, 2).sorted) // sorted method take implicit ord value

  // scala.Predef

  /*
  Implicits (used as implicit parameters):
    - val/var
    - object
    - accessor methods = defs with no parentheses
   */

  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )


  // implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)

  //  object Person { // companion objects
  //    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  //  }

  //  implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age) // high priority

  /*
  Implicit scope
  - normal scope = LOCAL SCOPE(current code) - high
  - imported scope = case of futures -> implicits.global
  - companion objects -> compianios of all type involved in the moehtd signature
    - List
    - Ordering -> companion objects
    - all the types involved = A or any supertype
   */

  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]

  /*
  best practices
  when defining an implicit val :
  1 if there is a single possible value for it, and you can edit the code for the type, then define the implicit
  in the companion
  2 if there are many possible values for it, but a single good one, and you can edit the code for the type,
   then define the good implicit in the companion, and the other implicit elsewhere, preferably in either
   the local scope or in other objects
   */

  object AlphabeticNameOrdering { // companion objects
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering { // companion objects
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age) // high priority
  }

  import AlphabeticNameOrdering._

  println(persons.sorted)

  /*
  Exercise. -> sorting
    - totalprice = most used(50%)
    - by unit count = 25 %
    - by unit price = 25 %
  declare in the local current code the implicit if that implicit is used like 10 % of the code
  when you are using 50 or high % declare as companion object
  if you are using it 25% in all proyect, create separte object and use import
   */

  case class Purchase(nUnits: Int, unitPrice: Double)

  // companion object
  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  // unitCountOrdering and unitPriceOrdering need to import
  object unitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object unitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }


}
