package exercises

trait MyList[+A] {
  def head: A
  def tail: MyList[A]
  def add[B >: A](item: B): MyList[B]
  def printList: String
  override def toString: String = s"[$printList]"
  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B]
  def map[B](transformer: MyTransformer[A, B]): MyList[B]
  def filter(predicate: MyPredicate[A]): MyList[A]
  def ++[B >: A](list: MyList[B]): MyList[B]

}

object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException("Empty List")
  def tail: MyList[Nothing] = throw new NoSuchElementException("Empty List")
  def add[B >: Nothing](item: B): MyList[B] = new MyListImpl(item, Empty)
  def printList = ""
  def flatMap[B](transformer: MyTransformer[Nothing, MyList[B]]): MyList[B] = Empty
  def map[B](transformer: MyTransformer[Nothing, B]): MyList[B] = Empty
  def filter(predicate: MyPredicate[Nothing]): MyList[Nothing] = Empty
  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

}

class MyListImpl[+A](h: A, t: MyList[A]) extends MyList[A] {
  def head = h
  def tail = t
  def add[B >: A](item: B) = new MyListImpl(item, this)
  def printList = h + " " + tail.printList

  def filter(predicate: MyPredicate[A]): MyList[A] =
    if (predicate.test(h)) new MyListImpl(h, tail.filter(predicate))
    else tail.filter(predicate)

  def map[B](transformer: MyTransformer[A, B]): MyList[B] =
    new MyListImpl(transformer.transform(head), tail.map(transformer))

  def ++[B >: A](list: MyList[B]): MyList[B] =
    new MyListImpl(head, tail ++ list)

  def flatMap[B](transformer: MyTransformer[A, MyList[B]]) =
    transformer.transform(head) ++ tail.flatMap(transformer)
}

trait MyPredicate[-A] {
  def test(value: A): Boolean
}

trait MyTransformer[-A, B] {
  def transform(value: A): B
}

class EvenPredicate extends MyPredicate[Int] {
  override def test(value: Int): Boolean = value % 2 == 0
}

class EmptyStringPredicate extends MyPredicate[String] {
  override def test(str: String) = str.length == 0
}

/*
* 1. Generic trait MyPredicate[T] - def test(T) => Boolean - every subclass will have diff impl
* ex: class EvenPredicate extends MyPredicate[Int] has test method which tell n is even or not
* 2. trait Transformer[A, B] - convert a val from A -> B, every subclass will have diff impl
* have method def transform(A) => B
* ex: class StringToInt extends MyTransformer[String, Int]
* 3. MyList
*   - map(myTransformer) => new MyList
*   - filter(predicate) => MyList
*   - flatMap(transformer from A to MyList[B]) => MyList[B]
*
* ex:
* [1,2,3].map(n * 2) => [2,4,6]
* [1,2,3,4].filter(n % 2) => [2,4]
* [1,2,3].flatMap([n, n + 1]) => [1,2,2,3,3,4]
*
* HINT: define MyPredicate[T] with [-T] and Transformer[A, B] with [-A,B]
* Contravariant
*
* */

object OOExercise extends App {
    val list: MyList[Int] = Empty.add(5).add(2).add(10)
    println("List: " + list.toString)
  //  val listStr: MyList[String] = Empty.add("Foo").add("Baar").add("Bear").add("cat")
  //  println("List: " + listStr)
  //  println((new EvenPredicate).test(10))
  //  println((new EvenPredicate).test(13))
  //  println("Map: " + list.map( new MyTransformer[Int, Int] {
  //    override def transform(value: Int): Int = value * 2
  //  }).toString)

  println("FlatMap: " + list.flatMap(new MyTransformer[Int, MyList[Int]] {
    override def transform(item: Int): MyList[Int] =
      new MyListImpl(item, new MyListImpl(item + 1, Empty))
  }))

}
