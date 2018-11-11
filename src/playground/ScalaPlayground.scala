package playground

trait MyList[+T] {
  def head : T
  def tail: MyList[T]
  def add[B >: T](item: B): MyList[B]
  def printList: String
  override def toString = "[  " + printList + "]"
  def map[B](transformer: T => B): MyList[B]
  def filter(predicate: T => Boolean): MyList[T]
  def flatMap[B](transformer: T => MyList[B]): MyList[B]
  def ++[B >: T](list: MyList[B]): MyList[B]
}

object Empty extends MyList[Nothing] {
  def head = throw new NoSuchElementException
  def tail = throw new NoSuchElementException
  def add[B >: Nothing](item: B) = new MyListImpl(item, Empty)
  def printList: String = ""
  def map[B](transformer: Nothing => B) = Empty
  def filter(predicate: Nothing => Boolean) = Empty
  def flatMap[B](transformer: Nothing => MyList[B]) = Empty
  def ++[B >: Nothing](list: MyList[B]) = list
}

class MyListImpl[T](h: T, t: MyList[T]) extends MyList[T] {
  def head = h
  def tail = t
  def add[B >: T](item: B) = new MyListImpl(item, this)
  def printList = head + " " + tail.printList
  def map[B](transformer: T => B) =
    new MyListImpl(transformer(head), tail.map(transformer))
  def filter(predicate: T => Boolean) =
    if (predicate(head)) new MyListImpl(head, tail.filter(predicate))
    else tail.filter(predicate)
  def ++[B >: T](list: MyList[B]) =
    new MyListImpl[B](head, tail ++ list)

  //  flatMap([n, n + 1])
  def flatMap[B](transformer: T => MyList[B]) =
    transformer(head) ++ tail.flatMap(transformer)
}

object ScalaPlayground extends App {
  val intList: MyList[Int] = Empty.add(5).add(9).add(12)
  println("List: " + intList)
//  println("Map List: " + intList.map(new Function1[Int, Int] {
//    override def apply(item: Int) = item * 2
//  }))
  println("Map List: " + intList.map(_ * 2))

//  println("Odd list: " + intList.filter(new Function1[Int, Boolean] {
//    override def apply(item: Int): Boolean = item % 2 == 1
//  }))
  println("Odd list: " + intList.filter(_ % 2 == 1))
//  println("flatMap: " + intList.flatMap[Int](new Function1[Int, MyList[Int]] {
//    override def apply(item: Int): MyList[Int] =
//      new MyListImpl[Int](item, new MyListImpl[Int](item + 1, Empty))
//  }))
  println("flatMap: " + intList.flatMap[Int](item => new MyListImpl(item, new MyListImpl(item + 1, Empty))))

}
