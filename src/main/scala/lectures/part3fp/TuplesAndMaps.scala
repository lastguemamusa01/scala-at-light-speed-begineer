package lectures.part3fp

import scala.annotation.tailrec

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

  /*
  1. What would happen if I had 2 original entries "Jim" -> 555 and "JIM" -> 9000 ?
    !!! careful with mapping keys -> the result key is not overlap, if this is overlapped this will be replaced by the new value
    val phoneBook = Map(("Jim", 555), "Min" -> 463, ("JIM", 9000)).withDefaultValue(-1)
    println(phoneBook.map(pair => pair._1.toLowerCase -> pair._2))
    Map(jim -> 90000, min -> 463)
    normal result -> Map(jim -> 555, min -> 463)

  2. Created overly simplified social network based on maps
    Person = String
    - add a person to the network
    - remove a person
    - friend (mutual)
    - unfriend (mutual)

    stats
    - number of firends of a person
    - person with most friends
    - how many people have no friends
    - if there is a social connection between 2 people (direct or not)

   */

  // map of string to list of string - naive facebook

  def add(network: Map[String, Set[String]], person: String): Map[String, Set[String]] =
    network + (person -> Set())     // the List() will create new List and add the Person

  def friend(network: Map[String, Set[String]], a: String, b: String) : Map[String, Set[String]] = {
    // retrieve los string of friends of a and b
    val friendsA = network(a)
    val friendsB = network(b)

    // result
    network + (a -> (friendsA + b)) + (b -> (friendsB + a))  // there will be duplicate if you use List, so use Set
  }

  def unfriend(network: Map[String, Set[String]], a: String, b: String) = {
    // retrieve los string of friends of a and b
    val friendsA = network(a)
    val friendsB = network(b)

    network + (a -> (friendsA - b)) + (b -> (friendsB - a)) // + creating new pairing in the map, overwrite the old pairing

  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
    // we need auxiliary functions, we can't simply remove the key person from the map
    // because this person might have friendships and those friends will have some
    // dangling friendships to an inexistent person
    // so we will have to remove those friendships first before we remove the person as a key

    // auxiliary function -> recursive functions
    def removeAux(friends: Set[String], networkAcc: Map[String, Set[String]]): Map[String, Set[String]] =
      if (friends.isEmpty) networkAcc
      else removeAux(friends.tail, unfriend(networkAcc, person, friends.head))

    val unfriended = removeAux(network(person), network)
    unfriended - person // this remove the person

  }

  val empty: Map[String, Set[String]] = Map()
  val network = add(add(empty, "Bob"), "Mary")
  println(network)
  println(friend(network, "Bob", "Mary"))
  println(unfriend(friend(network, "Bob", "Mary"), "Bob", "Mary"))
  println(remove(friend(network, "Bob", "Mary"), "Bob"))

  // Jim -> Bob -> Mary

  val people = add(add(add(empty, "Bob"), "Mary"), "Jim")
  val jimBob = friend(people, "Bob", "Jim")
  val testNet = friend(jimBob, "Bob", "Mary")

  println(testNet)

  def nFriends(network: Map[String, Set[String]], person: String) : Int =
    if(!network.contains(person)) 0
    else network(person).size  // network apply will return the set of that person in the map

  println(nFriends(testNet, "Bob")) // 2

  def mostFriends(network: Map[String, Set[String]]): String =
    network.maxBy(pair => pair._2.size)._1 // maxBy method of Map, whichi is matched by and this receives a lambda from a pairing to a value, and value has to be comparable
    // _2 -> list of friends for every pairing -> this will be every set size, maxBy -> return maximum value by the lambda.
    // we return _1 which is the key or the first element form that pairing.


  println(mostFriends(testNet))

  def nPeopleWithNoFrieds(netowrk: Map[String, Set[String]]): Int =
    netowrk.count(_._2.isEmpty) // count method -> count all the pairs whichi this lambda is true,  pair => pair is equivalent _
    // netowrk.count(pair => pair._2.isEmpty)
    // netowrk.filter(pair => pair._2.isEmpty).size   ->  this is another way
    //network.filterKeys(k => netowrk(k).isEmpty).size
    // another way of filtering ->filterKeys - for every key -> we get the set,
    // the set isEmpty check, and then only true will be remained, and call size to get number of people with no friends


  println(nPeopleWithNoFrieds(testNet))

  // there is social connection direct or indirect A and B -> BFS algorithm
  //
  def socialConnection(network: Map[String, Set[String]], a: String, b: String): Boolean = {
    // aux method bfs, tail recursive functions
    @tailrec
    def bfs(target: String, consideredPeople: Set[String], discoveredPeople: Set[String]): Boolean = {
      // can i find a target in descovered Poeple having considered considered people already
      if(discoveredPeople.isEmpty) false // no set of people to search in, haven't found the target and i cannot find the target
      else {
        // a person out of discovered people
        val person = discoveredPeople.head  // this person is already been considered
        if(person == target) true
        else if(consideredPeople.contains(person)) bfs(target, consideredPeople, discoveredPeople.tail)
        else bfs(target, consideredPeople + person, discoveredPeople.tail ++ network(person))
      }
    }

    bfs(b, Set(), network(a) + a)
  }

  println(socialConnection(testNet,"Mary","Jim"))
  println(socialConnection(network,"Mary","Bob"))

}
