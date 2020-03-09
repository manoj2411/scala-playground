package lecture.advscalap3concurrency

object ThreadCommunication_02 extends App {

  /*   Producer Consumer Problem
         - both are running in parallel, inserting e to a list and they dont about each other
         - we want to somehow force the consumer to wait until producer finished
         - TODO: forcing threads to run in a guaranteed order
  * */

  class SimpleContainer {
    private var value = 0
    def isEmpty = value == 0
    def get: Int = {
      val currValue = value
      value = 0 // reset value to the default state
      currValue
    }
    def set(newValue: Int) = value = newValue
  }

  def naiveProducerConsumer: Unit = {
    val valueContainer = new SimpleContainer
    val producer = new Thread(() => {
      Thread.sleep(100)
      println("[producer] setting value ..")
      valueContainer.set(42)
    })

    val consumer = new Thread(() => {
      while(valueContainer.isEmpty) {
        println("[consumer] waiting ..")
      }
      println(s"[consumer] consumer value ${valueContainer.get}")
    })

    producer.start()
    consumer.start()
  }
  //  naiveProducerConsumer

  def smartProducerConsumer: Unit = {
    val valueContainer = new SimpleContainer

    val producer = new Thread(() => {
      println("[producer] work on task")
      Thread.sleep(2000)
      valueContainer.synchronized {
        println("[producer] setting value ..")
        valueContainer.set(42)
        valueContainer.notify()
      }
    })

    val consumer = new Thread(() => {
      valueContainer.synchronized {
        println("[consumer] waiting ..")
        valueContainer.wait()
      }
      // Now valueContainer.isEmpty will be false
      println(s"[consumer] consumer value ${valueContainer.get}")
    })

    producer.start()
    consumer.start()
  }

  smartProducerConsumer

}
