package models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * User: YaFengLi
 * Date: 12-12-14
 * Time: 下午5:36
 */
@StaticMetamodel(Book.class)
public class Book_ {
    public static volatile SingularAttribute<Book, Long> id;
    public static volatile SingularAttribute<Book, String> name;
    public static volatile SingularAttribute<Book, Integer> price;
    public static volatile SingularAttribute<Book, Student> student;
}
