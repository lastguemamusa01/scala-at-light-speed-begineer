package lectures.part3fp

object MapFlatmapFilterFor extends App {

  // list collection
  val list = List(1,2,3) // apply method on the List Companion object
  println(list)
  println(list.head)
  println(list.tail)

  // map
  println(list.map(_ + 1))
  println(list.map(_ + " is a number"))

  // filter
  println(list.filter(_ % 2 == 0))

  // flatMap
  val toPair = (x: Int) => List(x, x+1)
  println(list.flatMap(toPair))

  // print all combinations between two lists
  val numbers = List(1,2,3,4)
  val chars = List('a','b','c','d')
  val colors = List("black", "white")

  // print of all combinations  List("a1","a2"..."d4")

  // use function and recusive functions
  // solution is not 2 for - or for anidados
  // val combinations = numbers.flatMap(number => chars.map(character => "" + character + number))
  // iterations -> para for anidados - usar mejor flatMap -> flatMap ..... last -> map
  // val combinations = numbers.flatMap(number => chars.flatMap(character => colors.map( color => "" + character + number + "-" + color)))

  val combinations = numbers.filter(_ % 2 == 0).flatMap(number => chars.flatMap(character => colors.map( color => "" + character + number + "-" + color)))
  println(combinations)

  // forEach -> similar like map -> but receives functions that returning units

  list.foreach(println)

  // for-comprehesions -> better readable iterations than flatMap and map
  // equivalent to flatmap, flatmap map chain above
  val forCombinations = for { // compiler rewritten this to flaptMap and map
    number <- numbers if number % 2 == 0 // if number % 2 == 0 -> filter out some even numbers -> this is equivalent filter
    character <- chars
    color <- colors
  } yield "" + character + number + "-" + color

  println(forCombinations)

  // side-effect
  for {
    number <- numbers
  } println(number)

  // syntax overload - sometimes used in practice
  list.map { x =>
    x * 2
  }

  /*
  1 - MyList supports for comprehensions, for work for comprehensions need to have mpa, filter and flatMap -> HOFCurriesMyList.scala
      map(f: A => B) => MyList[B]
      filter(p: A => Boolean) => MyList[A]
      flatMap(f: A => MyList[B]) => MyList[B]
  2 - a small collection of at most ONE elment - Maybe[+T]
      - implement map, flatMap, filter
  */

   // Maybe is used in functional programming to denote Optional values -> Maybe.scala

  // scala offers both mutable and immutable collections
  // mutable collections can be updated in place
  // immutable collections never change - you need to instantiate a new collection every time you need to make a change
  // default we are using immutable coLlections
  /*
  object predef {
    type Map [A, +B] = immutable.Map[A, B]
  }
   */

  // Immutable collections -> are found in scala.collections immutable package -> Traversable(Start it)
  // Iterable extended by Traversable.
  // Set, Seq, Map is extended by Iterable
  // Set -> HashSet, SortedSet
  // Map -> HashMap, SorteMap
  // Seq -> IndexedSeq, LinearSeq
  // IndexSeq -> Vector, Range, String -> access 0 time
  // LinearSeq -> List, Queue(FIFO), Stream, Stack(LIFO)

  // mutable collections -> are foun in scala.collections.mutable (X)
  // Itearble extended by Traversable
  // Set, Seq, Map is extended by Iterable
  // Set -> HashSet, LinkedHashSet
  // Map -> HashMap, MultiMap
  // Seq -> IndexedSeq, LinearSeq, Buffer
  // IndexSeq -> StringBuilder, ArrayBuffer
  // Buffer -> ArrayBuffer, ListBuffer
  // LinearSeq -> LinkedList, MutableList

  // Traversable - base trait for all collections ->
    // maps - map, flatMap, collect
    // conversions - toArray, toList, toSeq
    // size info - isEmpty, Size, nonEmpty
    // tests - exists, forall
    // folds - foldLeft, foldRight, reduceLeft, reduceRight
    // retrieval - head, find, tail
    // string ops - mkString



}
