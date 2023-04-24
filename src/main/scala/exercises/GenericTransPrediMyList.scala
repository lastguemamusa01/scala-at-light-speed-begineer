package exercises

abstract class GenericTransPrediMyList[+A] {


  /*
   1 - create a generic trait called MyPredicate[-T] -> have a small method (test(T) => Boolean // return boolean)
       to test whether a value of type T passes a condition so every subclass of MyPredicate T will actually
       have a different implementation of that little method
   2 - Generic trait called MyTransformer[-A,B] -> have a small method( transform(A) => B ) to convert a value of Type A into value of Type B
       so every subclass of MyTransformer have a different implemenation
   3 - GenericTransPrediMyList:
       - map(transformer) =>  GenericTransPrediMyList   // retun a new GenericTransPrediMyList of different type
       - filter(MyPredicate) => GenericTransPrediMyList
       - flapmap(transformer from A to GenericTransPrediMyList[B]) =>  GenericTransPrediMyList[B]

       class EvenPredicate extends MyPredicate[Int]
       class StringToIntTransformer extends MyTransformer[String, Int]

       pseudocode
       [1,2,3].map(n * 2) = [2,4,6]
       filter
       [1,2,3,4].filter(n%2) = [2,4]
       [1,2,3].flatmap(n => [n,n+1]) => [1,2,2,3,3,4]

  */

  def head: A
  def tail: GenericTransPrediMyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): GenericTransPrediMyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  def map[B](transformer: MyTransformer[A,B]): GenericTransPrediMyList[B]
  def flatMap[B](transformer: MyTransformer[A, GenericTransPrediMyList[B]]): GenericTransPrediMyList[B]
  def filter(predicate: MyPredicate[A]): GenericTransPrediMyList[A]

  // concatenation method
  def ++[B >: A](list: GenericTransPrediMyList[B]): GenericTransPrediMyList[B]

}

object GenericTransPrediEmpty extends GenericTransPrediMyList[Nothing] {   // list of Nothing
  def head: Nothing = throw new NoSuchElementException // ??? return nothing -> will be implemented later ->
  def tail: GenericTransPrediMyList[Nothing] = throw new NoSuchElementException  // no element is empty list, throw exception
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): GenericTransPrediMyList[B] = new GenericTransPrediEmptyCons(element,GenericTransPrediEmpty)
  def printElements: String = ""

  def map[B](transformer: MyTransformer[Nothing,B]): GenericTransPrediMyList[B] = GenericTransPrediEmpty
  def flatMap[B](transformer: MyTransformer[Nothing, GenericTransPrediMyList[B]]): GenericTransPrediMyList[B] = GenericTransPrediEmpty
  def filter(predicate: MyPredicate[Nothing]): GenericTransPrediMyList[Nothing] = GenericTransPrediEmpty

  // concatenation method
  def ++[B >: Nothing](list: GenericTransPrediMyList[B]): GenericTransPrediMyList[B] = list

}

class GenericTransPrediEmptyCons[+A](h: A, t: GenericTransPrediMyList[A]) extends GenericTransPrediMyList[A] {

  def head: A = h
  def tail: GenericTransPrediMyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): GenericTransPrediMyList[B] = new GenericTransPrediEmptyCons(element, this)
  def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements // recursive method  -> this only work in scala 2, for scala 3 use String interpolator

  /*

  pseudo syntax
  [1,2,3].filter(n % 2 == 0)
  = [2,3].filter(n % 2 == 0)
  = new GenericTransPrediEmptyCons(2, [3].filter(n % 2 == 0))
  = new GenericTransPrediEmptyCons(2, GenericTransPrediEmpty.filter(n % 2 == 0))
  = new GenericTransPrediEmptyCons(2, GenericTransPrediEmpty)

   */

  def filter(predicate: MyPredicate[A]): GenericTransPrediMyList[A] =
    if (predicate.test(h)) new GenericTransPrediEmptyCons(h, t.filter(predicate))
    else t.filter(predicate)

  /*

  pseudo syntax
  [1,2,3].map(n*2)
  = new GenericTransPrediEmptyCons(2, [2,3].map(n*2))
  = new GenericTransPrediEmptyCons(2, new GenericTransPrediEmptyCons(4, [3].map(n*2)))
  = new GenericTransPrediEmptyCons(2, new GenericTransPrediEmptyCons(4, new GenericTransPrediEmptyCons(6, GenericTransPrediEmpty.map(n*2))))
  = new GenericTransPrediEmptyCons(2, new GenericTransPrediEmptyCons(4, new GenericTransPrediEmptyCons(6, GenericTransPrediEmpty)))

   */
  def map[B](transformer: MyTransformer[A,B]): GenericTransPrediMyList[B] =
    new GenericTransPrediEmptyCons(transformer.transform(h), t.map(transformer))

  /*
  [1,2] ++ [3,4,5]
  = new GenericTransPrediEmptyCons(1, [2] ++ [3,4,5])
  = new GenericTransPrediEmptyCons(1, new GenericTransPrediEmptyCons(2, GenericTransPrediEmpty ++ [3,4,5]))
  = new GenericTransPrediEmptyCons(1, new GenericTransPrediEmptyCons(2, new GenericTransPrediEmptyCons(3 , new GenericTransPrediEmptyCons(4, new GenericTransPrediEmptyCons(5)))))

   */
  // concatenation method
  def ++[B >: A](list: GenericTransPrediMyList[B]): GenericTransPrediMyList[B] = new GenericTransPrediEmptyCons(h, t ++ list)


  /*

  [1,2].flatMap(n => [n, n+1])
  = [1,2] ++ [2].flatMap(n => [n, n+1])
  = [1,2] ++ [2,3] ++ GenericTransPrediEmpty.flatMap(n => [n, n+1])
  = [1,2] ++ [2,3] ++ GenericTransPrediEmpty
  = [1,2,2,3]

  */

  def flatMap[B](transformer: MyTransformer[A, GenericTransPrediMyList[B]]): GenericTransPrediMyList[B] =
    transformer.transform(h) ++ t.flatMap(transformer)


}

trait MyPredicate[-T] {
  def test(elem: T): Boolean
}

trait MyTransformer[-A, B] {
  def transform(elem:A): B
}

object GenericTransPrediMyListTest extends App {

  // linked list
  val listOfIntegers: GenericTransPrediMyList[Int] = new GenericTransPrediEmptyCons(1, new GenericTransPrediEmptyCons(2, new GenericTransPrediEmptyCons(3, GenericTransPrediEmpty)))
  val listOfStrings: GenericTransPrediMyList[String] = new GenericTransPrediEmptyCons("Hello", new GenericTransPrediEmptyCons("Scala", GenericTransPrediEmpty))
  val anotherListOfIntegers: GenericTransPrediMyList[Int] = new GenericTransPrediEmptyCons(4, new GenericTransPrediEmptyCons(5, GenericTransPrediEmpty))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

  println(listOfIntegers.map(new MyTransformer[Int,Int] {
    override def transform(elem: Int): Int = elem*2
  }).toString)

  println(listOfIntegers.filter(new MyPredicate[Int] {
    override def test(elem: Int): Boolean = elem % 2 == 0
  }).toString)

  println((listOfIntegers ++ anotherListOfIntegers).toString)

  println(listOfIntegers.flatMap(new MyTransformer[Int, GenericTransPrediMyList[Int]] {
    override def transform(elem: Int): GenericTransPrediMyList[Int] = new GenericTransPrediEmptyCons(elem, new GenericTransPrediEmptyCons(elem + 1, GenericTransPrediEmpty))
  }).toString)

}
