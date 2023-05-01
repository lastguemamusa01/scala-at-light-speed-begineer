package exercises

abstract class LambdaMyList[+A] {

  // we used functions as first class values

  def head: A
  def tail: LambdaMyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): LambdaMyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  // function type A => B
  // higher-order functions - either receive functions as parameters or return other functions as result
  def map[B](transformer: A => B): LambdaMyList[B]
  def flatMap[B](transformer: A => LambdaMyList[B] ): LambdaMyList[B]
  def filter(predicate: A => Boolean): LambdaMyList[A]

  // concatenation method
  def ++[B >: A](list: LambdaMyList[B]): LambdaMyList[B]

}

object LambdaPrediEmpty extends LambdaMyList[Nothing] {   // list of Nothing
  def head: Nothing = throw new NoSuchElementException // ??? return nothing -> will be implemented later ->
  def tail: LambdaMyList[Nothing] = throw new NoSuchElementException  // no element is empty list, throw exception
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): LambdaMyList[B] = new LambdaEmptyCons(element,LambdaPrediEmpty)
  def printElements: String = ""

  def map[B](transformer: Nothing => B): LambdaMyList[B] = LambdaPrediEmpty
  def flatMap[B](transformer: Nothing => LambdaMyList[B]): LambdaMyList[B] = LambdaPrediEmpty
  def filter(predicate: Nothing => Boolean): LambdaMyList[Nothing] = LambdaPrediEmpty

  // concatenation method
  def ++[B >: Nothing](list: LambdaMyList[B]): LambdaMyList[B] = list

}

class LambdaEmptyCons[+A](h: A, t: LambdaMyList[A]) extends LambdaMyList[A] {

  def head: A = h
  def tail: LambdaMyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): LambdaMyList[B] = new LambdaEmptyCons(element, this)
  def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements // recursive method  -> this only work in scala 2, for scala 3 use String interpolator


  def filter(predicate: A => Boolean): LambdaMyList[A] =
    if (predicate(h)) new LambdaEmptyCons(h, t.filter(predicate)) // called like function predicate(h) or predicate.apply(h)
    else t.filter(predicate)

  def map[B](transformer: A => B): LambdaMyList[B] =
    new LambdaEmptyCons(transformer(h), t.map(transformer))

  // concatenation method
  def ++[B >: A](list: LambdaMyList[B]): LambdaMyList[B] = new LambdaEmptyCons(h, t ++ list)

  def flatMap[B](transformer: A => LambdaMyList[B]): LambdaMyList[B] =
    transformer(h) ++ t.flatMap(transformer)


}

object LambdaMyListTest extends App {

  // linked list
  val listOfIntegers: LambdaMyList[Int] = new LambdaEmptyCons(1, new LambdaEmptyCons(2, new LambdaEmptyCons(3, LambdaPrediEmpty)))
  val listOfStrings: LambdaMyList[String] = new LambdaEmptyCons("Hello", new LambdaEmptyCons("Scala", LambdaPrediEmpty))
  val anotherListOfIntegers: LambdaMyList[Int] = new LambdaEmptyCons(4, new LambdaEmptyCons(5, LambdaPrediEmpty))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

  // println(listOfIntegers.map(elem => elem * 2).toString)
  println(listOfIntegers.map(_ * 2).toString)

  println(listOfIntegers.filter(elem => elem % 2 == 0).toString)
  // println(listOfIntegers.filter(_ % 2 == 0).toString)

  println((listOfIntegers ++ anotherListOfIntegers).toString)

  // here we cannot use underscore, we use element 2 times. you cannot use underscore for multiple times
  println(listOfIntegers.flatMap(elem => new LambdaEmptyCons(elem, new LambdaEmptyCons(elem + 1, LambdaPrediEmpty))).toString)

}

