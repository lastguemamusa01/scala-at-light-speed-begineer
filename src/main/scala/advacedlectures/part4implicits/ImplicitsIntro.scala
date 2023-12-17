package advacedlectures.part4implicits

object ImplicitsIntro extends App {

  /*
  implicits allow us to implement more robust frameworks and intuitive APIs and advanced
  functional programming concepts and even enrich existing libraries and types

   */

  // how this compiled , the arrow -> is implicit class named ArrowAssoc
  val pair = "Min Ku" -> "555" // arrowSoc instance of Minku , call with this -> arrow method with argument "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name!"
  }

  // method that convert string to a person with that string as it is name
  implicit def fromStringToPerson(str: String): Person = Person(str)

  // normally, greet method doesn't exist in String class
  println("Min ku".greet)  // compiler wrap this code to println(fromStringToPerson("Min ku").greet)

  // if there is another class with same method name as greet but return Int, and implicitMethod that use another class
  // the compiler throw error

//  class A {
//    def greet: Int = 2
//  }
//
//  implicit def fromStringToA(str: String): A = new A

  // implicit parameters
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defultAmount: Int = 10 // this implicit val will me passe to the second parameter of the increment method

  // not default args -> implicit val is founded by compiler from it's search scope
  increment(2)

  increment(2)(3)

  // futures were constructed with implicit parameter, Future apply method takes implicited parameter list with executor




}
