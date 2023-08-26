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

  - removing an element
  - intersection with another set
  - difference with another set

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

  def -(elem: A): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A] //difference
  def &(anotherSet: MySet[A]): MySet[A] // intersection

  def unary_! : MySet[A]

}

class EmptySet[A] extends MySet[A] {

  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)  // Adding
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet //concatenate // Union

  // map, flatmap and filter
  def map[B](f: A => B) : MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()  // () -> unit value

  def -(elem: A): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this //difference
  def &(anotherSet: MySet[A]): MySet[A] = this // intersection


  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)

}

// this is so inneficient, PropertyBasedSet will going to replace AllInclusiveSet

//class AllInclusiveSet[A] extends MySet[A] {
//
//  override def contains(elem: A): Boolean = true
//
//  override def +(elem: A): MySet[A] = this
//
//  override def ++(anotherSet: MySet[A]): MySet[A] = this
//
//  // naturals = allInclusiveSet[Int] = all the natural numbers
//  // naturals.map(x => x % 3) => ???
//  // [0 1 2]
//  override def map[B](f: A => B): MySet[B] = ???
//  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
//
//
//  override def filter(predicate: A => Boolean): MySet[A] = ??? // property-based set
//
//  override def foreach(f: A => Unit): Unit = ???
//
//  override def -(elem: A): MySet[A] = ???
//
//  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
//  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
//
//  override def unary_! : MySet[A] = new EmptySet[A]
//
//}

// all elements of type A which stafisfy a property
// math term ->  {x in A | property(x) }

class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {

  // more flexible

  def contains(elem: A): Boolean = property(elem)
  // { x in A | property(x) } + element = { x in a | property(x) || x == element }
  def +(elem: A): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || x == elem) // mathematical definition in scala syntax

  // { x in A | property(x) } ++ set => {x in A | property(x) | set contains x}
  def ++(anotherSet: MySet[A]): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  // all integers => (_ % 3) => [0, 1, 2]
  def map[B](f: A => B) : MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def foreach(f: A => Unit): Unit = politelyFail


  def filter(predicate: A => Boolean): MySet[A] =
    new PropertyBasedSet[A](x => property(x) && predicate(x))

  def -(elem: A): MySet[A] = filter(x => x != elem)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  def unary_! : MySet[A] =
    new PropertyBasedSet[A](x => !property(x)) // if you have even numbers, this going to return all odd numbers


  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")

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

  def -(elem: A): MySet[A] =
    if (head == elem) tail
    else tail - elem + head

  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intersection -> filter(x => anotherSet.contains(x))
  // apply is the same contract with contains -> x => anotherSet(x) -> this can reduce to filter(anotherSet)
  // because anotherSet is also function -> so intersection = filtering, because our set is functional

  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)  //difference
  // filter(x => !anotherSet.contains(x)) -> this could reduce also to  filter(x => !anotherSet(x))
  // implementing unary_! could be reduced filter(!anotherSet)

  // create new operator => unary
  // implementation of unary_! = negation of a set
  // set[1,2,3] => negation of that set is return all minus 1 ,2, 3
  // nagate finite of element -> result is infinite of element

  def unary_! : MySet[A] = // have space method name and : -> unary_! :
    new PropertyBasedSet[A](x => !this.contains(x))

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

  val negative = !s // s.unary_! = all the naturals not equal to 1,2,3,4
  println(negative(2)) // false -> number 2 is not in the negative set
  println(negative(5)) // true -> number 5 is in negative set

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5)) // false -> 5 should not incluveded is negative even

  val negativeEven5 = negativeEven + 5 // all the even number > 4 + 5
  println(negativeEven5(5)) // true -> because 5 is added

  // do not flatMap, Map or forEach with PropertyBasedSet -> they are going to crash
  // we don't know if the set is finite or not

}
