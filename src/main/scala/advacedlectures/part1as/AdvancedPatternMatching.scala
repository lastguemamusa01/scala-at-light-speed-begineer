package advacedlectures.part1as

object AdvancedPatternMatching extends App {

  // pattern matching allows you to decompose values that conform to a given pattern

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"this only element is $head") // :: -> infix pattern
    case _ =>
  }

  /*
  tyoe of pattern matchings
  - constants
  - wildcards -> _
  - case classes
  - tuples
  - some special magic like above -> very rarely used
   */

  // your own pattern matching. customized attern matching
  // for some reasons, you cannot use case class -> restrictions
  class Person(val name: String, val age: Int) // how you can do to class be like case class, compartible with pattern matching

  object Person {

    def unapply(person: Person): Option[(String, Int)] =
      if(person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "mayor")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  /*
   Exercise

   */

  // singleton objects with applies for each of those conditions
  // if you need Singleton objects with an apply for conditions, you usually name them in lower case

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  /*
  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
    if ( arg > -10 && arg < 10 ) Some(true)
    else None
  }
  */

  // more elegant way

  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n: Int = 12
  val mathProperty = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"  // case even(_) -> use this with Option
    case _ => "no property"
  }

  println(mathProperty)
  // custom pattern matching
  // advantage - you can reuse these boolean tests in other pattern matches
  // disadvantage - if you have lots of conditions, the code for defining the pattern matching tests is getting a little bit too verbose.

  // infix patterns -> create your own infix pattern -> only work for 2 parameters, not work for 3 parameters

  case class Or[A,B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"  // Or(number, string)
  }

  println(humanDescription)

  //decomposing sequences
  val vararg = numbers match {
    case List(1,_*) => "Starting with 1" // _* -> vararg patterns , pattern matching the whole list as sequence. 1, 2 , 3 or multiple values to decompose
  }

  // standard tecnique to unapply in object in the List don't work in this case
  // for List use unapply sequence

  // we have own little collection
  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override  val head: A, override  val tail: MyList[A]) extends MyList[A]

  // unapplySeq to use  with vargar pattern ->  _*
  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] = { // Option[Seq[A]] -> this is the return type of pattern matching is using it
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
    }
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty))) // 3 element list

  val decomposed = myList match {
    case MyList(1,2,_*) => "starting with 1, 2"
    case _ => "something else"
  }

  println(decomposed)

  // custom return types for unapply
  // Option type is not mandatory
  // only need these 3 : isEmpty: Boolean and get: something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get:String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(name) => s"This person's name is $name"
    case _ => "An allien"
  })

}
