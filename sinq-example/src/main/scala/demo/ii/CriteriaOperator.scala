package demo.ii

object CriteriaOperator extends Enumeration {
  type CriteriaOperator = Value
  val EQUAL, LIKE, IN, BETWEEN, IS_NULL, LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL = Value
}
