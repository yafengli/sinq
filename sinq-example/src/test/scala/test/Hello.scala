package test


object Hello extends App {

  select().from().where().s

  def select(cols: Column*): From = {
    val f: From = null
    f
  }

}

trait Table

trait Column

trait Condition

trait Order

trait From extends Result {
  def from(tables: Table*): Where
}

trait Where extends Result {
  def where(condition: Condition = null): GroupBy

  def join(table: Table): Join

  def joinLeft(table: Table): Join

  def joinRight(table: Table): Join

  def joinFull(table: Table): Join
}

trait Join {
  def on(c: Condition): Where
}

trait GroupBy extends Result{
  def groupBy(cols: Column): Having
}

trait Having extends Result {
  def having(c: Condition): Result
}

trait Result {
  def orderBy(order: Order): Result

  def limit(limit: Int, offset: Int): Result

  def sql(): String

  def params(): List[Any]

  def single(): Option[Any]

  def single[T](ct: Class[T]): Option[T]

  def collect(): List[Any]

  def collect[T](t: Class[T]): List[T]
}
