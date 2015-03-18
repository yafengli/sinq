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

## Group By
+ `groupBy(cols:Column*)`

## Having
+ `groupBy(having:Condition,cols:Column*)`

## Order By
+ `orderBy(order: Order)`

## Limit Offset
+ `limit(limit: Int, offset: Int)`

## 完整的例子：
+ `select(USER.id,Sum(USER.age)).from(USER).join(ADDRESS).on(Eq(USER.a_id,ADDRESS.id)).where(Ge(USER.id,1)).groupBy(Le(USER.id,10),USER.id).orderBy(Order(ASC, USER.id)).limit(10,0)`
+ 得到的SQL语句：`select u.id,sum(u.age) from t_user t inner join t_address a on t.a_id = a.id where u.id >= 1 group by u.id having u.id <= 10 order by u.id asc limit 10 offset 0`
