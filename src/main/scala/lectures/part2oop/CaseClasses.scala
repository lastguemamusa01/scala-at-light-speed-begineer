package lectures.part2oop

object CaseClasses extends App {

  /*
  class person -> lightweight data structure -
  we need to implement a lot of bolerplate code like companion objects
  standard methods serializing -> equals, hashCode, toString, etc
  case classes - useful shorthand for defining a class and the companion object and a lot of sensible defaults in one go
  - perfect for creating lightweight data holding classes with minimum hassle
   */

  case class Person(name: String, age: Int)

  // 1 - class parameters are promoted to fields
  val jim = new Person("Jim",34)
  println(jim.name)  // this is valid only in case class, because class parameters are promoted to fields

  // 2 - sensible toString -> case class has string representation
  // println(instance) = println(instance.toSTRING)   systactic sugar
  println(jim.toString) // Person(Jim,34), if this is class will print lectures.part2oop.CaseClasses$Person@2c5d55r3
  println(jim)

  // 3 equals and hashcode implemented out of box -> for use in collections
  val jim2 = new Person("Jim",34)
  println(jim == jim2) // true, if this is class -> false - 2 different instances and equal method is not implemented

  // 4 . case classes have handy copy method -> duplicate

  val jim3 = jim.copy() // creates a new instance of this case class
  val jim4 = jim.copy(age= 45)
  println(jim4)

  // 5 case classes have companion objects

  val thePerson = Person // Person is the companion object ot his case class , created automatically companion object
  val mary = Person("Mary", 23) // apply method as factory method in companion object
  // we don't really use the keyword new when instantiating a case class, we use this form Person("Mary", 23)

  // 6 - case classes are serializable, useful when dealing with distributing systems
  // you can send instances of case classes through the network and in between JVM.
  // AKKA Frameworks - deals with sending serializable messages thorught the network, in general the messages are case classes

  // 7 - case classes have extractor patterns - can be used in pattern matching

  // there is something called case object -> act like case class, except it is an object

  case object UnitedKingdom {  // same feature like case classes, except they don't get companion objects becuase they are their own companion objects
    def name: String = "The UK of GB and NI"
  }

  /*
Expand GenericTransPrediMyList - to use case classes and case objects
 */
}
