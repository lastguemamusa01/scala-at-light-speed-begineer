package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  /*
  set instance are callable(they have apply)

  val set = Set(1,2,3)

  set(2) // true
  set(42) // false

  Set instance are callable like functions
  the apply method always returns a value: true/false
  => Sets behave like actual functions
  Sets are functions

  Exercise - implement a functional set

  Singly linked set

  MySet in invariant not coveriant +A
  so emptySet[A] is class

   */

  def apply(elem: A): Boolean =  // already defined in mySet trait
    contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]  // Adding
  def ++(anotherSet: MySet[A]): MySet[A] //concatenate

  // map, flatmap and filter
  def map[B](f: A => B) : MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

}

class EmptySet[A] extends MySet[A] {

  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)  // Adding
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet //concatenate

  // map, flatmap and filter
  def map[B](f: A => B) : MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()  // () -> unit value

}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  def contains(elem: A): Boolean =
    elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] =   // Adding
    if (this contains elem) this
    else new NonEmptySet[A](elem, this)

  /*
   [1 2 3] ++ [4 5] =
   [2 3] ++ [4 5]+ 1 =
   [3] ++ [4 5] + 1 + 2 =
   [] ++ [4 5] + 1 + 2 + 3 =
   [4 5] + 1 + 2 + 3 = [4 5 1 2 3]
   */

  def ++(anotherSet: MySet[A]): MySet[A] = //concatenate
    tail ++ anotherSet + head

  // map, flatmap and filter
  def map[B](f: A => B) : MySet[B] = (tail map f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

}

// companion Object for Set -> for building sets

object MySet {
  /*
  val s = MySet(1,2,3) = buildSet(seq(1,2,3), [])
  = buildSet(seq(2,3), []+1)
  = buildSet(seq(3), [1]+2)
  = buildSet(seq(), [1,2]+3)
  = [1,2,3]
   */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if(valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1,2,3,4)
  s + 5 ++ MySet(-1,-2) + 3 flatMap (x => MySet(x, 10 * x)) filter (_ % 2 == 0) foreach println

  // implemented a collection without thinking of it as a collection
  // but rather as a function, as a property
}
