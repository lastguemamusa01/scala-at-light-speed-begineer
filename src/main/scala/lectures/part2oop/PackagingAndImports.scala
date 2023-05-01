package lectures.part2oop  // part2oop subpackage of package lectures

import playgroud.{PrinceCharming, Cinderella => Princess} // import the Cinderella from playground package , Gruping {}
// import playgroud._    // import everything from playground _ , _ -> everything
// => Princess name Aliasing -> useful when import same name from different class, solve naming conflict

import java.util.Date
import java.sql.{Date => SqlDate}

object PackagingAndImports extends App {

  // package - bunch of definitions grouped under the same name nad matches 99% directory structure
  // package lectures.part2oop

  // package members are accessible by their simple name
  val writer = new Writer("Min", "Ku", 2020)

  // another package, you need to import
  val princess = new Princess
  // the way you don't want to import, you need to use complete name with package
  // val princess = new playground.Cinderella // fully qualified name

  // packages are in hierarchy -> dot notation .
  // matching folder structure.


  // package object - originated from the problem that sometimes we may want to write methods or constants
  // outside of basically everything else -> kind of universal constants or methods that reside outside classes
  // only can have 1 package object per package
  // not used frequently

  sayHello
  println(SPEED_OF_LIGHT)

  // imports
  val prince = new PrinceCharming

  val date = new Date
  // val sqlDate = new java.sql.Date(2018,5,3) // use fully qualified name , deprecated
  val sqlDate = new SqlDate(2018,5,5) // use aliasing

  // default imports - are packages that are automatically imported without any intentional import from your side
  // java.lang - String, Ojbect, Exception
  // scala - Int, Nothing, Function,
  // scala.Predef - println, ???
}
