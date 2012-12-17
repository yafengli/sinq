package models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * User: YaFengLi
 * Date: 12-12-14
 * Time: 下午5:36
 */
@StaticMetamodel(Husband.class)
public class Husband_ {
    public static volatile SingularAttribute<Husband, Long> id;
    public static volatile SingularAttribute<Husband, String> name;
    public static volatile SingularAttribute<Husband, Integer> age;
    public static volatile SingularAttribute<Husband, Teacher> teacher;
}
