package advacedlectures.part3concurrency

object JVMConcurrencyProblems {

  // variables are the root of almost all evil in parallel and distributed computations
  def runInParallel(): Unit = {
    var x =0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x) // race condition - root of all evil i parallel and distributed applications
    // the reason is we're using mutable variables
    // multiple threads and variables or multable data structures can cause massive problems
  }

  // use syncronization to control the sequence of the threads
  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    // involves 3 steps - 1 read old value, 2 compute result and 3 write new value
    bankAccount.amount -= price
  }

  // create another by method, where the 3 steps to buy is atomic
  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.synchronized { // jvm method, not allow multiple threads to run the critical section  at the same time
      bankAccount.amount -= price // critical section -> race condition and concurrency bugs
    }
  }


  /*
  example of race condition :
  Thread1 (shoes)
    - read amount 50000
    - compute result 50000-3000 = 47000
  thread2 (iphone)
    - read mount 50000
    - compute result 50000 - 4000 = 46000
  thread1(shoes)
    - write amount to 47000
  thread2(iphone)
    - write amount to 46000


   */
  def demoBankingSafeProblem(): Unit = {
    (1 to 10000).foreach {_ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
      val thread2 = new Thread(() => buySafe(account, "iphone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if(account.amount != 43000) println(s"aha, i've just broken the bank: ${account.amount}")
    }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iphone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"aha, i've just broken the bank: ${account.amount}")
    }
  }
  /*
  exercises
  1 - create "inception threads"
       thread 1 create thread 2
       thread 2 create thread 3
       ..... so on
       each thread prints "hello from thread $i"
       print all messages in reverse order
       50 49 48 47 ...... 1

   2 - what's the max/min value of x
   3 - "sleep fallacy: what's the value of the message ?"
   */

  // 1 inception thread

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if(i < maxThreads) {
        val newChildThread = inceptionThreads(maxThreads, i+1)
        newChildThread.start()
        newChildThread.join()
      }
      println(s"Hello from thread $i")
    })

  // 2 min /max value of x after 100 threads
  // max value = 100 - each thread increases x by 1
  // min value = 1
    // all threads read x = 0 at the same time
    // all threads (in parallel) compute 0 + 1 =1
    // all thread try to write x = 1
  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 1000).map(_ => new Thread(() => x += 1)) // three steps(read, operation execute, store)
    threads.foreach(_.start())
  }

  // 3 sleep fallacy
  // almost always, message = "scala is awesome"
  // is it guaranteed ? No
  // Obnoxious situation(possible) :
  /*
   main thread :
      message = "Scala sucks"
      awesomeThread.start()
      sleep(1001) - yields execution // some jvm, processors, operation systems. specially single core processor,
      could put the thread on hold, that means it will schedule some other thread for execution
    awesome thread :
      sleep(1000) - yields execution
    OS gives the CPU to some important thread, thakes > 2s
    OS gives the Cpu back to the main thread
    main thread:
      println(message) // "Scala sucks"
    awesome thread:
      message = "Scala is awesome"
   */

  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    println(message)
  }

  // this case syncronization doesn't work, there is no race condition
  // syncronization competing with mutliple threads for the same memory zones
  def demoFixedSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    // solution : join the worker thread
    awesomeThread.join()
    println(message)
  }

  def main(args: Array[String]): Unit = {

    //runInParallel()  // this could be 1 or sometimes could be 2
    // have no control over how those competitions are being scheduled between the threads

    // race conditions - when you modifying a variable with multiple steps.

    // demoBankingProblem() // 2 cases of i've just broken the bank
    // demoBankingSafeProblem() // 0 case, this is safe

    // inceptionThreads(50).start() .. print hellow form thread 50 to 1

    // demoSleepFallacy() // almost all the times, Scala is awesome, but there could be  Scala sucks
    demoFixedSleepFallacy() // Scala is awesome all times
  }

}
