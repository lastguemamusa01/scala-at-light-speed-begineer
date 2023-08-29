package exercises

import scala.annotation.tailrec


abstract class MyStream[+A] { // covariant
  def isEmpty: Boolean

  def head: A

  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] // prepend operator, B is supertype of A(variance problem)

  def ++[B >: A](anotherStream: =>  MyStream[B]): MyStream[B] // concatenate 2 streams

  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): MyStream[B]

  def flatMap[B](f: A => MyStream[B]): MyStream[B]

  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // take the firt n elements out of this stream

  def takeAsList(n: Int): List[A] = take(n).toList()

  // auxiliary method to takeAsList - converts the stream to a list, then
  /*
  [1 2 3].toList([]) =
  [2 3].toList([1]) =
  [3].toList([2 1]) =
  [].toList([3 2 1]) =
  [1 2 3]
   */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
  if(isEmpty) acc.reverse
  else tail.toList(head :: acc)

}


object EmptyStream extends MyStream[Nothing] {

  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new StreamCons(element, this) // prepend operator, B is supertype of A(variance problem)

  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream // concatenate 2 streams

  def foreach(f: Nothing => Unit): Unit = ()  // there are no element to apply foreach

  def map[B](f: Nothing => B): MyStream[B] = this // there are no element to map

  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this // there are no element to flatmpan

  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this // there are no element to filter

  def take(n: Int): MyStream[Nothing] = this // still return empty string no matter how many elements is extracted


}


// call by name and lazy Vals

class StreamCons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {

  def isEmpty: Boolean = false

  override val head: A = hd

  override lazy val tail: MyStream[A] = tl // combining call by name and lazy vals -> call by need

  /*
  val s = new cONS(1, EmptyStream) // EmptyStream will not be evaluated inside cons util it's needed
  val prepended = 1 #:: s = new StreamCons(1, s)
   */
  def #::[B >: A](element: B): MyStream[B] = new StreamCons(element, this)

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new StreamCons(head, tail ++ anotherStream)  // => add call by name

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  /*
  s = new StreamCons(1, ?)
  mapped = s.map(_ + 1) = new StreamCons(2, s.tail.map(_ + 1)) // s.tail.map(_ + 1) is not evaluated until you
  explicitly call it -> mapped.tail
   */
  def map[B](f: A => B): MyStream[B] = new StreamCons(f(head), tail.map(f)) // preserves lazy evaluation

  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f) // tail.flatMap(f) -> stackoverflow
  // this need to be eager evaluated, instead of lazily evaluated

  def filter(predicate: A => Boolean): MyStream[A] =
    if(predicate(head)) new StreamCons(head, tail.filter(predicate))
    else tail.filter(predicate)

  // take the first n elements out of this stream
  def take(n: Int): MyStream[A] =
    if(n <= 0) EmptyStream
    else if (n == 1) new StreamCons(head, EmptyStream)
    else new StreamCons(head, tail.take(n-1))

}


// companion object
object MyStream {
  // factory
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new StreamCons(start, MyStream.from(generator(start))(generator))

}

object StreamsPlayground extends App{

  // call by name and lazy val = call by need

  val naturals = MyStream.from(1)(_ + 1)  // there is no stackoverflow error or infinite evaluation, because this is lazily evaluated
  println(naturals.head)  // 1
  println(naturals.tail.head)  // 2
  println(naturals.tail.tail.head)  // 3

  val startFrom0 = 0 #:: naturals // naturals.#::(0)   -> #:: method with ends with colon(:) -> this is right associative
  println(startFrom0.head) // 0

  startFrom0.take(10000).foreach(println) // print 0 to 9999

  // map, flatMap
  println(startFrom0.map(_ * 2).take(100).toList())  // List (0, 2, 4 ..... 198) - list of 100 even numbers

  // stackoverflow
  // List(0, 1, 1, 2, 2, 3, 3, 4, 4, 5)
  // adding => call by name , ++ maintains lazy evaluation
  println(startFrom0.flatMap(x => new StreamCons(x, new StreamCons(x + 1, EmptyStream))).take(10).toList())

  // Exercises on streams
  // 1 - stream of Fibonacci numbers
  // 2 - stream of prime numbers with Eratosthenes' sieve
  /*
    [2 3 4 ....]
    filter out all numbers divisible by 2
    [2 3 5 7 9 11 .....]
    filter out all numbers divisible by 3
    [2 3 5 7 11 13 17 ....]
    filter out all numbers divisible by 5
    .....
   */

  /*
  [first, ??? -> lazy evaluation
  [ first , [ ... <- other stream
  [ first, fibo(second, first+second)
   */

  def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] =
    new StreamCons(first, fibonacci(second, first+second))

  println(fibonacci(1,1).take(100).toList()) // List(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229 ....

  // eratosthenes sieve
  /*
   [2 3 4 5 6 7 8 9 10 11 12 ....
   [2 3 5 7 9 11 13 .... ]
   [2 erathosthenes applied to (numbers filtered by n % 2 != 0)]
   [2 3 eartosthenes applied to [5 7 9 11 ...] filtered by n % 3 != 0
   [2 3 5 ....
   */

  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if(numbers.isEmpty) numbers
    else new StreamCons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0))) // n => n -> shortened _
  // head -> 2
  // tail -> 3 4 5 6 7 8 9 10 11 12 ....

  println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())

  println(startFrom0.filter(_ < 10).take(10).toList()) // List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
  // List(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541)

}
