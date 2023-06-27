package lectures.part4pm

import scala.util.Random

object PatternMatching extends App {

  // switch on steroids

  val random = new Random
  val x = random.nextInt(10) // 0 to 10

  // PatternMatching

  val description = x match {
    case 1 => "the One"
    case 2 => "double or nothing"
    case 3 => "third time is the charm"
    case _ => "something else" // _ is wildcard(anything) -> default
  }

  println(x)
  println(description)

  // 1. Decompose values
  // conjuntion with case classes
  // case classes have the ability to be deconstructed or extracted in pattern matching

  case class  Person(name: String, age: Int)
  val bob = Person("Bob", 20)

  // instead of providing value, provide a pattern
  // case class pattern and extract the values out of an object
  // if a < 21 -> called guard
  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi, my name is $n and I can't drink in the USA"
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"
    case _ => "i don't know who i am" // _ ->  something else
  }

  println(greeting)

  /*
  1 - cases are matched in order
  2 - what if no cases match ? comment -> case _ =>   , scala.MatchError will throw
  3 - type of the Pattern match expression -> type inference, if all is string, string but
  if you have string and int -> Any . unified type of all the types in all the cases
  4 - pattern matching works really well with case classes -> have extractor patterns out of the box

   */

  // Patttern matching on sealed hierachies, sealed class help you that the match case need to be completed
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal: Animal = Dog("Terra Nova")
  animal match {
    case Dog(someBreed) => println(s"Matched a dog of the $someBreed breed") // this will have a warning, because Parrot is not covered
  }

  // match everything

  // val isEven = x % 2 == 0
   val isEven = x match {
     case n if n %2 == 0 => true
     case _ => false
   }

  // overkill, why we do that.
  // val isEvenCond = if(x % 2 == 0) true else false  // why - what the fuck
  val isEvenNormal = x % 2 == 0

  /*
  Exercise
  simple function uses PM(Pattern Matching)
     takes an Expr as parameter => return human readble form of it

     Sum(Number(2), Number(3)) => 2 + 3
     Sum(Sum(Number(2), Number(3)), Number(4)) => 2 + 3 + 4
     Prod(Sum(Number(2), Number(1)), Number(3)) => (2+1) * 3
     Sum(Prod(Number(2), Number(1)), Number(3)) => 2*1+3

   */

  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr

  def show(e: Expr): String = e match {
    case Number(n) => s"$n"
    case Sum(e1, e2) => show(e1) + "+" + show(e2)
    case Prod(e1, e2) => {
      def mayShowParentheses(exp: Expr) = exp match {
        case Prod(_,_) => show(exp) // _ -> anything
        case Number(_) => show(exp)
        case _ => "(" + show(exp) + ")"
      }

      mayShowParentheses(e1) + " * " + mayShowParentheses(e2)
    }
  }


  println(show(Sum(Number(2), Number(3))))
  println(show(Sum(Sum(Number(2), Number(3)), Number(4))))
  println(show(Prod(Sum(Number(2), Number(1)), Number(3))))
  println(show(Prod(Sum(Number(2), Number(1)), Sum( Number(3), Number(4)))))
  println(show(Sum(Prod(Number(2), Number(1)), Number(3))))

}
