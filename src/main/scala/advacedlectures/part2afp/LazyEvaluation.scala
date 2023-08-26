package advacedlectures.part2afp

object LazyEvaluation extends App {

  // lazy delays the evaluation of values
  // lazy val x: Int = throw new RuntimeException // lazy values are evaluated once, but only when they're used for the first time
  // this is not going to throw, because lazy val x is not used

  lazy val x: Int = {
    println("hello") // printing hello is part of evaluation of x
    42
  }

  // when used the lazy val , will be crash
  // value is evaluated, then the same value will stay assigned to that same name
  println(x)
  // hello
  //42

  println(x) //42

  // examples of implications :
  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")  // no
  // println boo is never happen, because the compiler is smart enough to evaluated the first condition simpleCondition is false
  // so this not going to evaluated second condition


  // in conjunction with call by name

  // def byNameMethod(n: => Int): Int = t + t + t + 1

  def byNameMethod(n: => Int): Int = {
    // call by need
    lazy val t = n // only evaluated once
    t + t + t + 1

  }
  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(10000)
    42
  }

  println(byNameMethod(retrieveMagicValue))  // 3 seconds -> waiting, waiting, waiting  -> 127
  // with lazy val only one waiting -> technique called call by need

  // use same parameter here by name multiple times, it doesn't really make sense to use call
  // by name and still evaluated multiple times inecesary -> use lazy val

  // call by need - useful when you want to evaluate your parameter here only when you need it.
  // but use same value in the rest of the code

  // actual implementation with library of lazy vals with filtering
  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  /*
  1 is less than 30?

  25 is less than 30?
  40 is less than 30?
  5 is less than 30?
  23 is less than 30?
  1 is greater than 20?
  25 is greater than 20?
  5 is greater than 20?
  23 is greater than 20?
  List(25, 23)
   */

  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20lazy = lt30Lazy.withFilter(greaterThan20)
  println
  // this is not calling the methods
  println(gt20lazy) // scala.collection.IterableOps$WithFilter@5e025e70

  gt20lazy.foreach(println) // here we can see the side effect
  /*
  1 is less than 30?  // this is yes
  1 is greater than 20?  // this going to pass
  25 is less than 30?
  25 is greater than 20?
  25
  40 is less than 30?
  5 is less than 30?
  5 is greater than 20?
  23 is less than 30?
  23 is greater than 20?
  23
   */

  // for-comprehensions use withFilter with guards

  for {
    a <- List(1,2,3) if a % 2 ==0 // if guards use lazy vals
  } yield a + 1

  // this is equivalent to above
  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /*
  Exercise : implement a lazily evaluated, singly linked STREAM of elements
  STREAM - special kin of collection in that the head of the stream is always evaluated and always
  available, but tail of the stream is always lazily evaluated and available only on demand

  naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
  naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
  naturals.foreach(println) // will creash - infinite
  naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
   */

  abstract class MyStream[+A] { // covariant
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator, B is supertype of A(variance problem)
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate 2 streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // take the firt n elements out of this stream
    def takeAsList(n: Int): List[A]
  }

  // companion object
  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }


}
