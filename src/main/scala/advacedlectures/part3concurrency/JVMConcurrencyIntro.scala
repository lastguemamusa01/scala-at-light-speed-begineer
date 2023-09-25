package advacedlectures.part3concurrency

import java.util.concurrent.Executors

object JVMConcurrencyIntro extends App {

  /*
   JVM threads

   Scala Methods -> synchronized methods and volatile annotations

   parallel programming on the JVM -> Thread -> Instance of Class

   in java
   interface Runnable {
    public void run()
   }

   */

  // JVM threads
  // runnable -> anonymous class
  // this will instantiates a thread object with runnable object

  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }
  val aThread = new Thread(runnable)  // java class

  // gives the signal to the JVM to start a JVM thread
  aThread.start()  // use start for starting a thread -> create a JVM threads -> OS thread
  // runnable.run() // doesn't do anything in parallel !
  aThread.join() // blocks until aThread finishes running

  // abstract method call -> reduce runnable to lambda
  val threadHello = new Thread( () => (1 to 5).foreach(_ => println("hello")))
  val threadGoodBye = new Thread( () => (1 to 5).foreach(_ => println("goodbye")))

  // goodbye and hello in different print order,
  // different runs with multithreaded environment produced different results

  threadHello.start()
  threadGoodBye.start()

  // executors
  // JVM thread is very expensive to start and kill, so the solution is reuse them
  // JVM java offer nice standard API to reuse threads with executors and thread poll

  // create a pool
  val pool = Executors.newFixedThreadPool(10) // 10 threads para reusar
  // there are a lot o ftype of ThreadPool : cachedThreadPool, schedule thread pool, fixed thread pool, single thread pool
  pool.execute( () => println("something in the thread pool") )

  // i dont care start and stop threads in executors

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 seconds")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 secods")
  })

  pool.shutdown()  // no more actions can be submitted
  // pool.execute(() => println("should not appear")) // throw exception

  // pool.shutdownNow() // this will sleep thread also will be throw exception
 println(pool.isShutdown) // true



}
