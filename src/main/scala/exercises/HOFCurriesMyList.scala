package exercises

abstract class HOFCurriesMyList[+A] {

  // we used functions as first class values

  def head: A
  def tail: HOFCurriesMyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): HOFCurriesMyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  // function type A => B
  // higher-order functions - either receive functions as parameters or return other functions as result
  def map[B](transformer: A => B): HOFCurriesMyList[B]
  def flatMap[B](transformer: A => HOFCurriesMyList[B] ): HOFCurriesMyList[B]
  def filter(predicate: A => Boolean): HOFCurriesMyList[A]

  // concatenation method
  def ++[B >: A](list: HOFCurriesMyList[B]): HOFCurriesMyList[B]

  // hofs
  /*
   1 - exapand MyList  -> HOFCurriesMyList.scala
    - foreach method - A => Unit    -> for each element have side effect. apply this function of every element of myList
        [1,2,3].foreach(x => println(x))
    - sort function - compare 2 functions, ((A,A) => Int) => MyList
        [1,2,3].sort((x,y) => y - x) => [3,2,1]  descending sort
    - zipWith (list, (A,B) => C) => MyList[C]
        [1,2,3].zipWith([4,5,6], x * y) => [1*4, 2*5, 3*6] = [4,10,18]
    - fold - curried function -
        fold(start value)(function) => a value
        [1,2,3].fold(0)(x+y) => 6 - sum of the numbers in the list
   */
  // foreach methods

  def foreach(f: A => Unit): Unit
  def sort(compre:(A, A) => Int): HOFCurriesMyList[A]
  def zipWith[B, C](list: HOFCurriesMyList[B], zip:(A, B) => C): HOFCurriesMyList[C]
  // folding is very important when you want to collapse all your data into on single value -> reduce -< fold one of reduce form
  def fold[B](start: B)(operator: (B,A) => B): B


}

object HOFCurriesPrediEmpty extends HOFCurriesMyList[Nothing] {   // list of Nothing
  def head: Nothing = throw new NoSuchElementException // ??? return nothing -> will be implemented later ->
  def tail: HOFCurriesMyList[Nothing] = throw new NoSuchElementException  // no element is empty list, throw exception
  def isEmpty: Boolean = true
  def add[B >: Nothing](element: B): HOFCurriesMyList[B] = new HOFCurriesEmptyCons(element,HOFCurriesPrediEmpty)
  def printElements: String = ""

  def map[B](transformer: Nothing => B): HOFCurriesMyList[B] = HOFCurriesPrediEmpty
  def flatMap[B](transformer: Nothing => HOFCurriesMyList[B]): HOFCurriesMyList[B] = HOFCurriesPrediEmpty
  def filter(predicate: Nothing => Boolean): HOFCurriesMyList[Nothing] = HOFCurriesPrediEmpty

  // concatenation method
  def ++[B >: Nothing](list: HOFCurriesMyList[B]): HOFCurriesMyList[B] = list

  // hofs
  def foreach(f: Nothing => Unit): Unit = ()  // () -> unit value
  def sort(compare: (Nothing, Nothing) => Int) = HOFCurriesPrediEmpty
  def zipWith[B, C](list: HOFCurriesMyList[B], zip:(Nothing, B) => C): HOFCurriesMyList[C] = // zip with one empty and one non empty list donen't make sense
    if (!list.isEmpty) throw new RuntimeException("Lists do not have the same length")
    else HOFCurriesPrediEmpty

  def fold[B](start: B)(operator: (B, Nothing) => B): B = start


}

class HOFCurriesEmptyCons[+A](h: A, t: HOFCurriesMyList[A]) extends HOFCurriesMyList[A] {

  def head: A = h
  def tail: HOFCurriesMyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): HOFCurriesMyList[B] = new HOFCurriesEmptyCons(element, this)
  def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements // recursive method  -> this only work in scala 2, for scala 3 use String interpolator


  def filter(predicate: A => Boolean): HOFCurriesMyList[A] =
    if (predicate(h)) new HOFCurriesEmptyCons(h, t.filter(predicate)) // called like function predicate(h) or predicate.apply(h)
    else t.filter(predicate)

  def map[B](transformer: A => B): HOFCurriesMyList[B] =
    new HOFCurriesEmptyCons(transformer(h), t.map(transformer))

  // concatenation method
  def ++[B >: A](list: HOFCurriesMyList[B]): HOFCurriesMyList[B] = new HOFCurriesEmptyCons(h, t ++ list)

  def flatMap[B](transformer: A => HOFCurriesMyList[B]): HOFCurriesMyList[B] =
    transformer(h) ++ t.flatMap(transformer)

  // hofs
  override def foreach(f: A => Unit): Unit = {
    f(h)
    t.foreach(f)
  }

  def sort(compare: (A,A) => Int): HOFCurriesMyList[A] = {

    // this is not tail recursive
    def insert(x: A, sortedList: HOFCurriesMyList[A]): HOFCurriesMyList[A] =
      if(sortedList.isEmpty) new HOFCurriesEmptyCons(x, HOFCurriesPrediEmpty)
      else if(compare(x, sortedList.head) <= 0) new HOFCurriesEmptyCons(x, sortedList)
      else new HOFCurriesEmptyCons(sortedList.head, insert(x, sortedList.tail))


    val sortedTail = t.sort(compare)
    insert(h, sortedTail) // insert -> auxiliary fucntion -> do sorted insertion of a value inside an already sorted list

  }

  def zipWith[B, C](list: HOFCurriesMyList[B], zip: (A, B) => C): HOFCurriesMyList[C] =
    if(list.isEmpty) throw new RuntimeException("Lists do not have the same length") // the list not have same length
    else new HOFCurriesEmptyCons( zip(h, list.head), t.zipWith(list.tail, zip))

  /*
  [1,2,3].fold(0)(+) =
  = [2,3].fold(1)(+) =
  = [3].fold(3)(+) =
  = [].fold(6)(+) =
  = 6
   */
  def fold[B](start: B)(operator: (B, A) => B): B =
    t.fold(operator(start,h))(operator)

}

object HOFCurriesMyListTest extends App {

  // linked list
  val listOfIntegers: HOFCurriesMyList[Int] = new HOFCurriesEmptyCons(1, new HOFCurriesEmptyCons(2, new HOFCurriesEmptyCons(3, HOFCurriesPrediEmpty)))
  val listOfStrings: HOFCurriesMyList[String] = new HOFCurriesEmptyCons("Hello", new HOFCurriesEmptyCons("Scala", HOFCurriesPrediEmpty))
  val anotherListOfIntegers: HOFCurriesMyList[Int] = new HOFCurriesEmptyCons(4, new HOFCurriesEmptyCons(5, HOFCurriesPrediEmpty))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

  // println(listOfIntegers.map(elem => elem * 2).toString)
  println(listOfIntegers.map(_ * 2).toString)

  println(listOfIntegers.filter(elem => elem % 2 == 0).toString)
  // println(listOfIntegers.filter(_ % 2 == 0).toString)

  println((listOfIntegers ++ anotherListOfIntegers).toString)

  // here we cannot use underscore, we use element 2 times. you cannot use underscore for multiple times
  println(listOfIntegers.flatMap(elem => new HOFCurriesEmptyCons(elem, new HOFCurriesEmptyCons(elem + 1, HOFCurriesPrediEmpty))).toString)

  //hofs

  listOfIntegers.foreach(println)   //  println === x => println(x)`

  println(listOfIntegers.sort((x,y) => y - x))
  println(anotherListOfIntegers.zipWith[String, String](listOfStrings, _ +"-"+ _))  // _ + " "+ _ -> short hand lambda notation


  println(listOfIntegers.fold(0)(_ + _))

  // for comprehensions
  val combinations = for {
    n <- listOfIntegers
    string <- listOfStrings
  } yield n + "-" + string

  println(combinations)

  // for comprenhension is expression, so you can paste it in println
  println(for {
    n <- listOfIntegers
    string <- listOfStrings
  } yield n + "-" + string)

  // if you want to implement your own collections, if you want make them compatible with for comprehensions
  // all you need to do is provdie map, flatMap and filter that looks like above

}
