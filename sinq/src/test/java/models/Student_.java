package models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * User: YaFengLi
 * Date: 12-12-14
 * Time: 下午5:36
 */
@StaticMetamodel(Student.class)
public class Student_ {
    public static volatile SingularAttribute<Student, Long> id;
    public static volatile SingularAttribute<Student, String> name;
    public static volatile SingularAttribute<Student, String> address;
    public static volatile SingularAttribute<Student, Integer> age;
    public static volatile SingularAttribute<Student, Teacher> teacher;
    public static volatile SetAttribute<Student, Book> books;
}
