package playground

trait MyList[+T] {
  def isEmpty: Boolean
  def head : T
  def tail: MyList[T]
  def add[B >: T](item: B): MyList[B]
  def printList: String
  override def toString = "[  " + printList + "]"
  def map[B](transformer: T => B): MyList[B]
  def filter(predicate: T => Boolean): MyList[T]
  def flatMap[B](transformer: T => MyList[B]): MyList[B]
  def ++[B >: T](list: MyList[B]): MyList[B]
  // HOFs
  def forEach(fn: T => Unit): Unit
  def sort(fn: (T, T) => Int): MyList[T]
}

object Empty extends MyList[Nothing] {
  def isEmpty = true
  def head = throw new NoSuchElementException
  def tail = throw new NoSuchElementException
  def add[B >: Nothing](item: B) = new MyListImpl(item, Empty)
  def printList: String = ""
  def map[B](transformer: Nothing => B) = Empty
  def filter(predicate: Nothing => Boolean) = Empty
  def flatMap[B](transformer: Nothing => MyList[B]) = Empty
  def ++[B >: Nothing](list: MyList[B]) = list
  // HOFs
  def forEach(fn: Nothing => Unit) = ()
  def sort(fn: (Nothing, Nothing) => Int): MyList[Nothing] = Empty
}

class MyListImpl[T](h: T, t: MyList[T]) extends MyList[T] {
  def isEmpty = false
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

  // HOFs
  def forEach(fn: T => Unit) = {
    fn(head)
    tail.forEach(fn)
  }

  def sort(compare: (T, T) => Int): MyList[T] = {
    def insert(x: T, sortedList: MyList[T]): MyList[T] = {
      if (sortedList.isEmpty) Empty.add(x)
      else if (compare(x, sortedList.head) <= 0 ) new MyListImpl(x, sortedList)
      else new MyListImpl(sortedList.head, insert(x, sortedList.tail))
    }
    val sortedTail = tail.sort(compare)
    insert(head, sortedTail)
  }


}

object MyListPlayground extends App {
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

  println("ForEach: ")
  intList.forEach(println)

  println("Sorting: " + intList.sort((a,b) => a - b) )

}
