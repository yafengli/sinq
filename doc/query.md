## Select
+ `select(cols:Column*)`

## From
+ `from(tables:Table*)`

## Join
+ Inner Join:`join(table:Table)`
+ Left Join:`joinLeft(table:Table)`
+ Right Join:`joinRight(table:Table)`
+ Full Join:`joinFull(table:Table)`

## On
+ `on(condition: Condition)`

## Condition
+ `Eq`/`Ge`/`Le`/`Gt`/`Lt`/`Between`/`NotEq`/`NotBetween`/`In`

## Group By
+ `groupBy(cols:Column*)`

## Having
+ `groupBy(having:Condition,cols:Column*)`

## Order By
+ `orderBy(order: Order)`

## Limit Offset
+ `limit(limit: Int, offset: Int)`

## SQL字符串与参数值
+ `sql()`、`params()`

## 完整的例子

		val sinq = SinqStream("h2")
		val query = sinq.select(_USER.id,Sum(_USER.age))
						.from(_USER).join(ADDRESS).on(Eq(_USER.a_id,ADDRESS.id))
						.where(Ge(_USER.id,1).and(Le(_USER.age,10).or(Gt(_USER.age,2)))
						.groupBy(Le(_USER.id,10),_USER.id)
						.orderBy(Order(ASC, _USER.id))
						.limit(10,0) 
		println(query.sql())
		//SQL: select u.id,sum(u.age) from t_user t inner join t_address a on t.a_id = a.id where u.id >= ? and (u.age <= ? or u.age > ?) group by u.id having u.id <= ? order by u.id asc limit 10 offset 0
		println(query.params())
		//参数: List(1, 10, 20)
		
	