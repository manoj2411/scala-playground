package playground

trait MyList[+T] {
  def head : T
  def tail: MyList[T]
  def add[B >: T](item: B): MyList[B]
  def printList: String
  override def toString = "[  " + printList + "]"
  def map[B](transformer: MyTransformer[T, B]): MyList[B]
  def filter(predicate: MyPredicate[T]): MyList[T]
  def flatMap[B](transformer: MyTransformer[T, MyList[B]]): MyList[B]
  def ++[B >: T](list: MyList[B]): MyList[B]
}

object Empty extends MyList[Nothing] {
  def head = throw new NoSuchElementException
  def tail = throw new NoSuchElementException
  def add[B >: Nothing](item: B) = new MyListImpl(item, Empty)
  def printList: String = ""
  def map[B](transformer: MyTransformer[Nothing, B]) = Empty
  def filter(predicate: MyPredicate[Nothing]) = Empty
  def flatMap[B](transformer: MyTransformer[Nothing, MyList[B]]) = Empty
  def ++[B >: Nothing](list: MyList[B]) = list
}

class MyListImpl[T](h: T, t: MyList[T]) extends MyList[T] {
  def head = h
  def tail = t
  def add[B >: T](item: B) = new MyListImpl(item, this)
  def printList = head + " " + tail.printList
  def map[B](transformer: MyTransformer[T,B]) =
    new MyListImpl(transformer.transform(head), tail.map(transformer))
  def filter(predicate: MyPredicate[T]) =
    if (predicate.test(head)) new MyListImpl(head, tail.filter(predicate))
    else tail.filter(predicate)
  def ++[B >: T](list: MyList[B]) =
    new MyListImpl[B](head, tail ++ list)

  //  flatMap([n, n + 1])
  def flatMap[B](transformer: MyTransformer[T, MyList[B]]) =
    transformer.transform(head) ++ tail.flatMap(transformer)
}

trait MyPredicate[-A] {
  def test(item: A): Boolean
}

trait MyTransformer[-A,B] {
  def transform(item: A): B
}

object ScalaPlayground extends App {
  val intList: MyList[Int] = Empty.add(5).add(10).add(12)
  println("List: " + intList)
  println("Map List: " + intList.map(new MyTransformer[Int, Int] {
    override def transform(item: Int) = item * 2
  }))
  println("Odd list: " + intList.filter(new MyPredicate[Int] {
    override def test(item: Int): Boolean = item % 2 == 1
  }))
  println("flatMap: " + intList.flatMap[Int](new MyTransformer[Int, MyList[Int]] {
    override def transform(item: Int): MyList[Int] =
      new MyListImpl[Int](item, new MyListImpl[Int](item + 1, Empty))
  }))

}
