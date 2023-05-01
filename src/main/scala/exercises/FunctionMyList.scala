package exercises

abstract class FunctionMyList[+A] {

  // we used functions as first class values

  def head: A
  def tail: FunctionMyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): FunctionMyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  // function type A => B
  // higher-order functions - either receive functions as parameters or return other functions as result
  def map[B](transformer: A => B): FunctionMyList[B]
  def flatMap[B](transformer: A => FunctionMyList[B] ): FunctionMyList[B]
  def filter(predicate: A => Boolean): FunctionMyList[A]

  // concatenation method
  def ++[B >: A](list: FunctionMyList[B]): FunctionMyList[B]

}

object FunctionPrediEmpty extends FunctionMyList[Nothing] {   // list of Nothing
  def head: Nothing = throw new NoSuchElementException // ??? return nothing -> will be implemented later ->
  def tail: FunctionMyList[Nothing] = throw new NoSuchElementException  // no element is empty list, throw exception
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): FunctionMyList[B] = new FunctionEmptyCons(element,FunctionPrediEmpty)
  def printElements: String = ""

  def map[B](transformer: Nothing => B): FunctionMyList[B] = FunctionPrediEmpty
  def flatMap[B](transformer: Nothing => FunctionMyList[B]): FunctionMyList[B] = FunctionPrediEmpty
  def filter(predicate: Nothing => Boolean): FunctionMyList[Nothing] = FunctionPrediEmpty

  // concatenation method
  def ++[B >: Nothing](list: FunctionMyList[B]): FunctionMyList[B] = list

}

class FunctionEmptyCons[+A](h: A, t: FunctionMyList[A]) extends FunctionMyList[A] {

  def head: A = h
  def tail: FunctionMyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): FunctionMyList[B] = new FunctionEmptyCons(element, this)
  def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements // recursive method  -> this only work in scala 2, for scala 3 use String interpolator

  /*

  pseudo syntax
  [1,2,3].filter(n % 2 == 0)
  = [2,3].filter(n % 2 == 0)
  = new FunctionEmptyCons(2, [3].filter(n % 2 == 0))
  = new FunctionEmptyCons(2, FunctionPrediEmpty.filter(n % 2 == 0))
  = new FunctionEmptyCons(2, FunctionPrediEmpty)

   */

  def filter(predicate: A => Boolean): FunctionMyList[A] =
    if (predicate(h)) new FunctionEmptyCons(h, t.filter(predicate)) // called like function predicate(h) or predicate.apply(h)
    else t.filter(predicate)

  /*

  pseudo syntax
  [1,2,3].map(n*2)
  = new FunctionEmptyCons(2, [2,3].map(n*2))
  = new FunctionEmptyCons(2, new FunctionEmptyCons(4, [3].map(n*2)))
  = new FunctionEmptyCons(2, new FunctionEmptyCons(4, new FunctionEmptyCons(6, FunctionPrediEmpty.map(n*2))))
  = new FunctionEmptyCons(2, new FunctionEmptyCons(4, new FunctionEmptyCons(6, FunctionPrediEmpty)))

   */
  def map[B](transformer: A => B): FunctionMyList[B] =
    new FunctionEmptyCons(transformer(h), t.map(transformer))

  /*
  [1,2] ++ [3,4,5]
  = new FunctionEmptyCons(1, [2] ++ [3,4,5])
  = new FunctionEmptyCons(1, new FunctionEmptyCons(2, FunctionPrediEmpty ++ [3,4,5]))
  = new FunctionEmptyCons(1, new FunctionEmptyCons(2, new FunctionEmptyCons(3 , new FunctionEmptyCons(4, new FunctionEmptyCons(5)))))

   */
  // concatenation method
  def ++[B >: A](list: FunctionMyList[B]): FunctionMyList[B] = new FunctionEmptyCons(h, t ++ list)


  /*

  [1,2].flatMap(n => [n, n+1])
  = [1,2] ++ [2].flatMap(n => [n, n+1])
  = [1,2] ++ [2,3] ++ FunctionPrediEmpty.flatMap(n => [n, n+1])
  = [1,2] ++ [2,3] ++ FunctionPrediEmpty
  = [1,2,2,3]

  */

  def flatMap[B](transformer: A => FunctionMyList[B]): FunctionMyList[B] =
    transformer(h) ++ t.flatMap(transformer)


}

// predicate and transformer is already function type
//trait MyFunctionPredicate[-T] { // T => Boolean
//  def test(elem: T): Boolean
//}
//
//trait MyFunctionTransformer[-A, B] { // A => B
//  def transform(elem:A): B
//}

object FunctionMyListTest extends App {

  // linked list
  val listOfIntegers: FunctionMyList[Int] = new FunctionEmptyCons(1, new FunctionEmptyCons(2, new FunctionEmptyCons(3, FunctionPrediEmpty)))
  val listOfStrings: FunctionMyList[String] = new FunctionEmptyCons("Hello", new FunctionEmptyCons("Scala", FunctionPrediEmpty))
  val anotherListOfIntegers: FunctionMyList[Int] = new FunctionEmptyCons(4, new FunctionEmptyCons(5, FunctionPrediEmpty))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

  println(listOfIntegers.map(new Function1[Int,Int] {  // replace with Function1
    override def apply(elem: Int): Int = elem*2
  }).toString)

  println(listOfIntegers.filter(new Function1[Int, Boolean] { // change dto Function1[Int, Boolean]
    override def apply(elem: Int): Boolean = elem % 2 == 0
  }).toString)

  println((listOfIntegers ++ anotherListOfIntegers).toString)

  println(listOfIntegers.flatMap(new Function1[Int, FunctionMyList[Int]] {
    override def apply(elem: Int): FunctionMyList[Int] = new FunctionEmptyCons(elem, new FunctionEmptyCons(elem + 1, FunctionPrediEmpty))
  }).toString)

}
