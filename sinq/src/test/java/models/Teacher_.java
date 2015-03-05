package models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * User: YaFengLi
 * Date: 12-12-14
 * Time: 下午5:36
 */
@StaticMetamodel(Teacher.class)
public class Teacher_ {
    public static volatile SingularAttribute<Teacher, Long> id;
    public static volatile SingularAttribute<Teacher, String> name;
    public static volatile SingularAttribute<Teacher, String> address;
    public static volatile SingularAttribute<Teacher, Integer> age;
    public static volatile SingularAttribute<Teacher, Husband> husband;
    public static volatile SetAttribute<Teacher, Student> students;
}
