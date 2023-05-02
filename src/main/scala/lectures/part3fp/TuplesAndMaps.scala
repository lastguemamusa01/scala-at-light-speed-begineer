package lectures.part3fp

object TuplesAndMaps extends App {

  // tuples = finite, ordered kind of lists
  val aTuple = new Tuple2(2, "hello, scala")  // int, string -> group  Tuple2[Int, String] = (Int, String) -> syntatic sugar for Tuple2
  val aTuple2 = Tuple1(2, "hello, scala")
  val aTuple3 = (2, "hello, scala")

  // tuples can group at most 22 elements of diffrent types ->  22 -> they are used conjunction with function types -> Function1..22

  println(aTuple3._1) // 2   -> ._2 -> print second element string hello,scala
  println(aTuple.copy(_2 = "goodbye java")) // copy methods in the same style as with case classes
  println(aTuple.swap) // ("hello, scala", 2)

  // Maps - collection used to associate things with other things
  // keys -> values

  val aMap: Map[String, Int] = Map()

  val phoneBook = Map(("Jim", 555), "Min" -> 463).withDefaultValue(-1) // inside you can put tuples or pairings  , also valid "Min" -> 463 syntatic sugar for tuple
  // tuples = ("Jim", 555)
  // pairing = "Min" -> 463
  // => double arrow is for lamdbdas
  // a -> b is sugar for (a, b)

  println(phoneBook)

  // map operations

  println(phoneBook.contains("Jim")) // true
  println(phoneBook("Jim")) // 555
  println(phoneBook("Mary")) // crash -> NoSuchElement Exception . for eviting add in the Map().withDefaultValue(-1) -> -1

  // add a pairing

  val newPairing = "Mary" -> 678
  val newPhoneBook = phoneBook + newPairing  // create a new phone book because maps are immutable -> Map(Jim -> 555, Min -> 463, Mary -> 678)

  println(newPhoneBook)

  // functionales on maps
  // map, flatMap, filter

  println(phoneBook.map(pair => pair._1.toLowerCase -> pair._2)) // Map(jim -> 555, min -> 463)  - convert all key to lower case

  // filterKeys
  println(phoneBook.view.filterKeys(x => x.startsWith("J")).toMap) // only key startwith J  -> Map(Jim -> 555)

  // mapValues
  println(phoneBook.view.mapValues(number => number * 10).toMap) // Map(Jim -> 5550, Min -> 4630)  , only affect to all values-> * 10

  // conversions to other collections

  println(phoneBook.toList)  // List((Jim,555), (Min,463))
  println(List(("Daniel",555)).toMap) // Map(Daniel -> 555)

  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0))) // group and print same first characters
  // HashMap(J -> List(James, Jim), A -> List(Angela), M -> List(Mary), B -> List(Bob), D -> List(Daniel))




}
