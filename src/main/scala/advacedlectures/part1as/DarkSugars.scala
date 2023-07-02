package advacedlectures.part1as

import scala.util.Try

object DarkSugars extends App {

  // syntax sugar # 1: methods with single param

  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  // syntatic sugar that allows us to write a code with curly braces in a syntax
  // very similar to java
  val description = singleArgMethod {
    // write some complex code
    42
  }

  //Try applied method
  val aTryinstance = Try { // like java's try {...} curly braces
    throw new RuntimeException
  }

  List(1,2,3).map { x =>
    x+1
  }

  // syntax sugar #2: single abstract method pattern -> instances of trait's with a single method can actually be reduce to lambdas
  trait Action {
    def act(x: Int): Int
  }

  // anonymous class
  val onInstance: Action = new Action { // suggestion conver to single abstract method
    override def act(x: Int): Int = x + 1
  }

  // you can reduce the trait to single instance to a lambda

  val aFunkyInstance: Action = (x: Int) => x + 1  // (x: Int) <- lambda, scala compiile figure out that this is funcion of one type
  // to act method

  // in example : Concurrency -> with Runnables(Java interface that can be passed on to threads) trait
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala") // this is valid thread creation
  })

  // also valid(equivalent) with DarkSugar syntactic sugar
  val aSweeterThread = new Thread(() => println("hello, Scala")  ) // () -> lambda is empty because run() method doesn't have parameters

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  // also work for abstract class with one method that is not implemented you can use lambda
  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet") // this is language syntax sugar, this is not related with implicits

  // syntax sugar #3: the :: and #:: methods are special

  val prependedList = 2 :: List(3,4)
  // infix methods are actually converted to first object -> 2.::(List(3,4)) -> this is absurd because there is not :: method in Int
  // compile rewrite to -> List(3,4).::(2)
  // scala spec: last char decides associativity of method
  // it end : is right associative, if not end : is left associativity

  /*
  1 :: 2 :: 3 :: List(4,5) equivalent to List(4,5).::(3).::(2).::(1)
   */
  // #:: methods example with -->: this is used in Stream

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int] // perfectly viable
  // because the arrow colon is right associative because it ends in a column

  // syntax sugar #4 : multi-word method naming  (is more like language feature)

  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")  //method composed with 3 words
  }

  val lilly = new TeenGirl("lilly")
  lilly `and then said` "Scala is so sweet!" // rarely used in practice

  // syntax sugar #5 : infix types - Generics

  class Composite[A,B] // Gengerics like Map
  // val composite: Composite[Int, String] = ???

  // infix type
  // val infixComposite: Int Composite String = ???

  class -->[A,B]
  // val towards: Int --> String  = ???

  // syntax sugar # 6 : update() is very special, much like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7 // rewritten to anArray.update(2,7) , 2 is the index position and 7 is the value to be updated
  // used in mutable collection
  // remember apply() and update()

  // syntax sugar # 7 : setters for matable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member = internalMember /// getter
    def member_=(value: Int): Unit =
      internalMember = value  // setter
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_ = 42


}
