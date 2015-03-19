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

## 完整的例子：
+ `select(USER.id,Sum(USER.age)).from(USER).join(ADDRESS).on(Eq(USER.a_id,ADDRESS.id)).where(Ge(USER.id,1).and(Le(USER.age,10).or(Gt(USER.age,2))).groupBy(Le(USER.id,10),USER.id).orderBy(Order(ASC, USER.id)).limit(10,0)`
+ SQL: __select u.id,sum(u.age) from t_user t inner join t_address a on t.a_id = a.id where u.id >= ? and (u.age <= ? or u.age > ?) group by u.id having u.id <= ? order by u.id asc limit 10 offset 0__
+ 参数: __List(1, 10, 20)__