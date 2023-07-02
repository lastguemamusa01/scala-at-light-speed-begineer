package advacedlectures.part1as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65
  //instructions vs expressions
  // instructions are imperative language like java, python and c++
  // instructions - executed in sequence
  // scala - use expressions - build expressions top of the expressions

  // code block is expression used a lot
  val aCodeBlock = {  // {}
    if(aCondition) 54 // useless expression
    56  // this is the real value
  }

  // compile infers types for use - type inference
  // Unit -> only do side effect - printing something - equivalent void
  val theUnit = println("hello, Scala")

  // functions
  def aFunction(x: Int): Int = x + 1

  // recursion: stack and tail, stack recursion is bad -> convert to tail recursion
  @tailrec def factorial(n: Int, accumulator: Int): Int =
    if(n <= 0) accumulator
    else factorial(n-1, n * accumulator)

  // tail recursion - make not use additional stack frame when calling then , evicting stackoverflow

  // object-orientation programming

  class Animal
  class Dog extends Animal

  val aDog: Animal = new Dog // subtyping pollymorphism

  // abstract data type - abstract classes and trait
  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("Crunch")
  }

  // overloding and overwriting

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog) // normal method call
  aCroc eat aDog // natural language - sugar syntax -> infix notation

  // scala permissive with naming
  // many operators in scala rewritten as method
  // 1 + 2
  // 1.+(2)

  // anonymous classes - instantiate classes on the spot with implementations just for that use case
  // the compiler create new anonymous classes for us by extending Carnivore and overwritting the eat method
  val aCarnivor = new Carnivore {  // this is trait, cannot instantiate, but can supply an implementation of this trait on the spot
    override def eat(a: Animal): Unit = println("roar!")
  }

  // Generics
  abstract class MyList[+A]  // +A -> covariance, variacne and variance problemns in this course

  // singleton and companions object
  object MyList // abstract class MyList[+A] and object MyList(singleton) are companions object

  // case classes
  case class Person(name: String, age: Int) // case calasses very lightweight data structure that have companions objects with apply methods
  // and a bunch of utilities already define for us. case class is serializable, that all the parameters are acutally fields

  // exceptions and try/catch
  // val throwsException = throw new RuntimeException  // this throw excepction in jvm , type -> Nothing
  // Nothing - type of nothingness

  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught an exception"
  } finally {
    println("some longs")
  }

  // packaging and imports -> naming aliases, packaging objects and all kind of stuffs

  // scala more objct oriented than object -> design around classes and object
  // scala all is object, class or package object -> everything is object

  // functional programming
  // apply method is scala able to call the objects like functions
  // because functions are actually instances of classes with apply methods

  val incrementer = new Function1[Int,Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)


  // scala has first class support functions reducing with syntatic sugar reducing instances of this type  Function1[Int,Int]
  val anonymousIncrementer = (x: Int) => x + 1 // lambdas for anonymous functions

  List(1,2,3).map(anonymousIncrementer) // map -> High order functions
  // map , flatMap, filter


  // for-comprenhension
  // syntax sugar for chain of map and flatmpa
  val pairs = for {
    num <- List(1,2,3) // optionally -> filter if you add if guard -> if condition
    char <- List('a','b','c')
  } yield num + "-" + char

  // Scala Collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples

  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // "collections": Options, Try
  val anOption = Some(2) // Some, None, Option type

  // pattern matching - most powerful feature in Scala -> switch on steorids
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob",22)
  val greeting = bob match {
    case Person(n,_) => s"Hi, my name is $n"
  }

  // all the patterns

}
