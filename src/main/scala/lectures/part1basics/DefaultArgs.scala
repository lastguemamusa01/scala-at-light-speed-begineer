package lectures.part1basics

import scala.annotation.tailrec

object DefaultArgs extends App {

  //Default and named arguments


  @tailrec
  def tailRecursiveFact(n: Int, acc: Int): Int =
    if(n <=1) acc
    else tailRecursiveFact(n-1, n*acc)

  val fact10 = tailRecursiveFact(10,1)

  // either this accumulator pollutes the function signature because
  // we're ony interesed in the end parameter(n)
  // accumulator spolis our function signature or we need to wrap this up into a bigger function

  /*
  def anotherFactorial(n: Int): BigInt = {
    @tailrec   // this annotation assure if this is tail recursion or not, if it is not will show where is the error
    def factHelper(x: Int, accumulator: BigInt): BigInt =
      if (x <= 1) accumulator
      else factHelper(x-1, x*accumulator) // tail recursion -> use same stack, not use a lot of memory stack
    // tal recursion -> use recursive call as the LAST expression

    factHelper(n, 1)
  }
  */

  // scala allows us to pass default values for some parameters that we don't really want to appear every single time

  @tailrec
  def tailRecursiveDefaultArgumentFact(n: Int, acc: Int = 1): Int =   // if i dont pass acc value, default will be 1
    if(n <=1) acc
    else tailRecursiveDefaultArgumentFact(n-1, n*acc)

  val factDefaultArg10 = tailRecursiveDefaultArgumentFact(10)
  val factDefaultArg10withValue = tailRecursiveDefaultArgumentFact(10,2) // default value overwritten to 2
  // passing default values for arguments is a popular feature in other languages like javascript or python

  // this can be function of api
  def savePicture(format: String = "jpg", width: Int = 1920, height: Int = 1080): Unit = println("saving picture")

  savePicture("jpg",800 ,600) // if this is used frequently, jpg format, if used as default argument
  // savePicture(800 ,600) // error -> this is confuse, delete parameter in the first place, he is waiting jpg
  savePicture() // this will be fine
  // but , if you pass value
  // savePicture(100) // which parameter stand the 100 -> error

  savePicture("bmw") // compiler, said ok this is format

  // 1 .pass in every leading argument
  // 2. or we can name the arguments

  // name the argument
  savePicture(width = 800) // ok i will rename the width with value 800
  savePicture(height = 900, width = 800, format = "bmp") // the order does'nt matter in name the arguments

  // when you pass same params every time like acc is 1, use default argument and ommit it when you call it factDefaultArg10(10)

}
