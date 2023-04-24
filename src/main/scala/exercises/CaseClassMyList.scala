package exercises

abstract class CaseClassMyList[+A] {

  /*
  Expand GenericTransPrediMyList - to use case classes and case objects -> named CaseClassMyList
  your list data structure, is extremely powerfull with case classes and case objects
  already have implementation of equals and hashcode out of the box -> use in collection
  the list is serializable
  case class - fast way of defining light data structures with little boilerplate with a lot of features .
  companions already implemented, sensible equals, hashCode, toString, auto-promoted params to fields
  cloning, serializable, extractable for pattern matching,
  case object
   */


  def head: A
  def tail: CaseClassMyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): CaseClassMyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  def map[B](transformer: CaseClassMyTransformer[A,B]): CaseClassMyList[B]
  def flatMap[B](transformer: CaseClassMyTransformer[A, CaseClassMyList[B]]): CaseClassMyList[B]
  def filter(predicate: CaseClassMyPredicate[A]): CaseClassMyList[A]

  // concatenation method
  def ++[B >: A](list: CaseClassMyList[B]): CaseClassMyList[B]

}

case object CaseObjectEmpty extends CaseClassMyList[Nothing] {   // list of Nothing
  def head: Nothing = throw new NoSuchElementException // ??? return nothing -> will be implemented later ->
  def tail: CaseClassMyList[Nothing] = throw new NoSuchElementException  // no element is empty list, throw exception
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): CaseClassMyList[B] = new CaseClassCons(element,CaseObjectEmpty)
  def printElements: String = ""

  def map[B](transformer: CaseClassMyTransformer[Nothing,B]): CaseClassMyList[B] = CaseObjectEmpty
  def flatMap[B](transformer: CaseClassMyTransformer[Nothing, CaseClassMyList[B]]): CaseClassMyList[B] = CaseObjectEmpty
  def filter(predicate: CaseClassMyPredicate[Nothing]): CaseClassMyList[Nothing] = CaseObjectEmpty

  // concatenation method
  def ++[B >: Nothing](list: CaseClassMyList[B]): CaseClassMyList[B] = list

}

case class CaseClassCons[+A](h: A, t: CaseClassMyList[A]) extends CaseClassMyList[A] {

  def head: A = h
  def tail: CaseClassMyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): CaseClassMyList[B] = new CaseClassCons(element, this)
  def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements // recursive method  -> this only work in scala 2, for scala 3 use String interpolator

  /*

  pseudo syntax
  [1,2,3].filter(n % 2 == 0)
  = [2,3].filter(n % 2 == 0)
  = new CaseClassCons(2, [3].filter(n % 2 == 0))
  = new CaseClassCons(2, CaseObjectEmpty.filter(n % 2 == 0))
  = new CaseClassCons(2, CaseObjectEmpty)

   */

  def filter(predicate: CaseClassMyPredicate[A]): CaseClassMyList[A] =
    if (predicate.test(h)) new CaseClassCons(h, t.filter(predicate))
    else t.filter(predicate)

  /*

  pseudo syntax
  [1,2,3].map(n*2)
  = new CaseClassCons(2, [2,3].map(n*2))
  = new CaseClassCons(2, new CaseClassCons(4, [3].map(n*2)))
  = new CaseClassCons(2, new CaseClassCons(4, new CaseClassCons(6, CaseObjectEmpty.map(n*2))))
  = new CaseClassCons(2, new CaseClassCons(4, new CaseClassCons(6, CaseObjectEmpty)))

   */
  def map[B](transformer: CaseClassMyTransformer[A,B]): CaseClassMyList[B] =
    new CaseClassCons(transformer.transform(h), t.map(transformer))

  /*
  [1,2] ++ [3,4,5]
  = new CaseClassCons(1, [2] ++ [3,4,5])
  = new CaseClassCons(1, new CaseClassCons(2, CaseObjectEmpty ++ [3,4,5]))
  = new CaseClassCons(1, new CaseClassCons(2, new CaseClassCons(3 , new CaseClassCons(4, new CaseClassCons(5)))))

   */
  // concatenation method
  def ++[B >: A](list: CaseClassMyList[B]): CaseClassMyList[B] = new CaseClassCons(h, t ++ list)


  /*

  [1,2].flatMap(n => [n, n+1])
  = [1,2] ++ [2].flatMap(n => [n, n+1])
  = [1,2] ++ [2,3] ++ CaseObjectEmpty.flatMap(n => [n, n+1])
  = [1,2] ++ [2,3] ++ CaseObjectEmpty
  = [1,2,2,3]

  */

  def flatMap[B](transformer: CaseClassMyTransformer[A, CaseClassMyList[B]]): CaseClassMyList[B] =
    transformer.transform(h) ++ t.flatMap(transformer)


}

trait CaseClassMyPredicate[-T] {
  def test(elem: T): Boolean
}

trait CaseClassMyTransformer[-A, B] {
  def transform(elem:A): B
}

object CaseClassMyListTest extends App {

  // linked list
  val listOfIntegers: CaseClassMyList[Int] = new CaseClassCons(1, new CaseClassCons(2, new CaseClassCons(3, CaseObjectEmpty)))
  val cloneListOfIntegers: CaseClassMyList[Int] = new CaseClassCons(1, new CaseClassCons(2, new CaseClassCons(3, CaseObjectEmpty)))
  val listOfStrings: CaseClassMyList[String] = new CaseClassCons("Hello", new CaseClassCons("Scala", CaseObjectEmpty))
  val anotherListOfIntegers: CaseClassMyList[Int] = new CaseClassCons(4, new CaseClassCons(5, CaseObjectEmpty))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

  println(listOfIntegers.map(new CaseClassMyTransformer[Int,Int] {
    override def transform(elem: Int): Int = elem*2
  }).toString)

  println(listOfIntegers.filter(new CaseClassMyPredicate[Int] {
    override def test(elem: Int): Boolean = elem % 2 == 0
  }).toString)

  println((listOfIntegers ++ anotherListOfIntegers).toString)

  println(listOfIntegers.flatMap(new CaseClassMyTransformer[Int, CaseClassMyList[Int]] {
    override def transform(elem: Int): CaseClassMyList[Int] = new CaseClassCons(elem, new CaseClassCons(elem + 1, CaseObjectEmpty))
  }).toString)

  println(cloneListOfIntegers == listOfIntegers) // true , if this is class -> implementing equal method for recursive method is pain in the ass
  // compare all the elements recursively

}

