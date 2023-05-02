package lectures.part3fp

import scala.util.Random

object Sequences extends App {

  // Seq - very general interface for data strucutres that have a well defined order and can be indexed
  // apply, iterator, length, reverser for indexing and iterating
  // concatenation, appending, prepending
  // a lot of others : grouping, sorting, zipping, searching, slicing

  val aSequence = Seq(2,1,3,4)  // Seq is companion object apply -> Sequence actually List
  println(aSequence)
  println(aSequence.reverse)
  println(aSequence(2)) // get the index number -> 3
  println(aSequence ++ Seq(5,6,7) ) // ++ concatenation
  println(aSequence.sorted)

  // Ranges
  val aRange: Seq[Int] = 1 to 10   // range -> 1 to 10
  aRange.foreach(println)

  val aRange2: Seq[Int] = 1 until 10   // print 1 to 9 exluding 10
  aRange2.foreach(println)

  (1 to 10).foreach(x => println("Hello"))

  // Lists
  // a Linearseq immutable linked list
    // head, tail, isEmpty are fast O(1)
    // most other operations are 0(n): length, reverse
  // Sealed - has 2 subtypes :
      // object Nil(empty)
      // class ::

  val aList = List(1,2,3)
  val prepended = 42 :: aList // :: -> syntatic sugar
  println(prepended)

  val prependedAndAppended = 42 +: aList :+ 89
  println(prependedAndAppended)

  val apples5 = List.fill(5)("apple")  // fill is curried function -> fill 5 times apple in the list
  println(apples5)  // List(apple, apple, apple, apple, apple)

  // make string methods

  println(aList.mkString("-|-"))  // all element is concatenated with this -|-    ->    1-|-2-|-3

  // arrays -> java simple arrays -> contructed with predefined lengths
  // can be mutated -> updated in place
  // are interoperable with java's T[] arrays
  // indexing is fast - access in constant time

  val numbers = Array(1,2,3,4)
  val threeElements = Array.ofDim[Int](3)  // allocate memory for 3 elements
  println(threeElements)   // [I@5ba23b66
  threeElements.foreach(println)  // 0 0 0  -> have default values 0 for int and null for String

  // mutation
  numbers(2) = 0   // syntax sugar for numbers.update(2, 0)  -> replace index 2 with 0 value
  // update special method is scala - only used mutable collections -> like apply
  println(numbers.mkString(" "))

  // arrays and seq

  // implicit conversion
  val numbersSeq: Seq[Int] = numbers // numbers is Array(java Array), despite this, convertion can be done to Seq
  println(numbersSeq)  // ArraySeq(1, 2, 0, 4) or WrappedArray(1, 2, 0, 4)

  // vector
  // immutable sequences - effectively constant indexed read and write O(log 32(n))
  // fast element addition: append/prepend
  // implemented as a fixed-branched trie (branch factor 32) -> for memory and cache optimizations
  // good performance for large size

  val vector: Vector[Int] = Vector(1,2,3)
  println(vector)

  // vectors vs lists

  // get average write time
  val maxRuns = 1000
  val maxCapacity = 1000000

  def getWriteTime(collection: Seq[Int]): Double = {
    val random = new Random

    val times = for {
      iteration <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated( random.nextInt(maxCapacity), random.nextInt())
      System.nanoTime() - currentTime
    }

    times.sum * 1.0/ maxRuns

  }

  val numbersList = (1 to maxCapacity).toList
  val numbersVector = (1 to maxCapacity).toVector

  // List -> update Head efficient, middle is not efficient
  // advantage : keeps reference to tail
  // disadvantage : updating an element in the middle takes long
  println(getWriteTime(numbersList))  // 4825589.474
  // vector  need to raverse the whole 32 branch tree and replace entire chunk
  // advantage : depth of the tree is small
  // disadvantage : needs to replace an entire 32-element chunk
  println(getWriteTime(numbersVector))   // 4101.333


}
