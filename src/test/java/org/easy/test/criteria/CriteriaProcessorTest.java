package org.easy.test.criteria;

import org.easy.criteria.CriteriaComposer;
import org.easy.criteria.CriteriaComposer.ComparisonOperator;
import org.easy.criteria.CriteriaProcessor;
import org.easy.test.criteria.model.Auditable;
import org.easy.test.criteria.model.PersonTypeEnum;
import org.easy.test.criteria.model.generated.*;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Tuple;
import javax.persistence.criteria.JoinType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.easy.criteria.CriteriaComposer.AggregateFunction.COUNT;
import static org.easy.criteria.CriteriaComposer.AggregateFunction.SUM;
import static org.easy.criteria.CriteriaComposer.ComparisonOperator.*;
import static org.easy.criteria.CriteriaComposer.NegationOperator.NOT;
import static org.junit.Assert.*;

public class CriteriaProcessorTest {

    private static CriteriaProcessor criteriaProcessor = null;

    private static boolean initContextByMe = false;
    private static Logger log = LoggerFactory.getLogger(CriteriaProcessorTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        log.debug("setUpBeforeClass");

        // try loading Spring
        try {
            if (!DbHelper.isPopulatedWith(DbHelper.allDataFiles)) {
                int errorCount = DbHelper.initDb(DbHelper.CONNECTION_URL, DbHelper.allDataFiles);
                assertEquals(0, errorCount);
                initContextByMe = true;
            }
            criteriaProcessor = new CriteriaProcessor(DbHelper.getEntityManager());

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {/*
        if (initContextByMe)
			DbHelper.close();*/
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * select * from course
     */
    @Test
    public void testAllCourses1() {
        // create a criteria to find all courses.
        CriteriaComposer<Course> courseCriteria = CriteriaComposer.from(Course.class);
        List<Course> result = criteriaProcessor.findAllEntity(courseCriteria);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertEquals("course1", result.get(0).getName());
    }

    /**
     * #1- Use of auditable interface make it easy to set and get data from
     * audtiable fields. Theses fields are usually common for all tables.
     * <p>
     * #2- The other approach is creating auditable base class of all the entities.
     * EasyCriteria supports this approach too
     * <p>
     * TODO: Write an intercepter to set auditable values.
     */
    @Test
    public void testAuditPersonType() {
        CriteriaComposer<PersonType> studentCriteria = CriteriaComposer.from(PersonType.class);
        studentCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        PersonType studentType = criteriaProcessor.findUniqueEntity(studentCriteria, null);

        assertNotNull(studentType);
        assertTrue(studentType instanceof Auditable);

        Auditable auditable = (Auditable) studentType;

        assertEquals(new Long(1L), auditable.getLastUpdateBy());
    }

    /**
     * select * from course where course.last_update_date_time between ? and ?
     */
    @Test
    public void testBetweenDates() {
        Date thatDate = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            thatDate = simpleDateFormat.parse("2011-01-02 00:00:00");
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage());
            fail(e.getMessage());
        }

        Date toDate = new Date(System.currentTimeMillis());


        CriteriaComposer<Course> criteria = CriteriaComposer.from(Course.class);
        criteria.where(Course_.lastUpdateDateTime, ComparisonOperator.BETWEEN, thatDate, toDate);

        List<Course> result = criteriaProcessor.findAllEntity(criteria, true, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("course2", result.get(0).getName());
    }

    /**
     * Find count total student.
     * <p>
     * There are two way to do it. #1 Using "count" API of CriteriaProcessor; #2 using aggregation function of
     * CriteriaComposer
     * <p>
     * select count(distinct person0_.id) from person where person_type_id=?
     * select person_type_id from person_type where persontype.name=STUDENT
     */
    @Test
    public void testCountStudent1() {
        // find student type first.
        CriteriaComposer<PersonType> studentTypeCriteria = CriteriaComposer.from(PersonType.class);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        PersonType studentType = criteriaProcessor.findUniqueEntity(studentTypeCriteria, null);

        // student criteria
        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.where(Person_.personType, EQUAL, studentType);

        // count
        long result = criteriaProcessor.count(studentCriteria, true);

        assertEquals(9, result);
    }

    /**
     * Find all Students
     * <p>
     * select * from person inner join person_type on
     * person.person_type_id=persontype.id where persontype.name=STUDENT
     */
    @Test
    public void testFindAllStudent1() {
        CriteriaComposer<Person> student = CriteriaComposer.from(Person.class);
        CriteriaComposer<PersonType> personTypeStudent = student.join(JoinType.INNER, Person_.personType);
        personTypeStudent.where(PersonType_.name, EQUAL, PersonTypeEnum.STUDENT);

        List<Person> result = criteriaProcessor.findAllEntity(student, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(9, result.size());
    }

    /**
     * Find all Students.
     * Same as 1 but using DOT chaining
     */
    @Test
    public void testFindAllStudent1b() {
        // using DOT chaining
        CriteriaComposer<Person> student = CriteriaComposer.from(Person.class);
        student.join(JoinType.INNER, Person_.personType).
                where(PersonType_.name, EQUAL, PersonTypeEnum.STUDENT);

        List<Person> result = criteriaProcessor.findAllEntity(student, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(9, result.size());
    }

    /**
     * Find all Students.
     * Same as 1 but criteria is defined in PersonCritera class. This is the
     * preferred way.
     * Makes service more readable.
     */
    @Test
    public void testFindAllStudent2() {

        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);

        CriteriaComposer<PersonType> studentTypeCriteria = studentCriteria.join(JoinType.INNER, Person_.personType);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        List<Person> result = criteriaProcessor.findAllEntity(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(9, result.size());
    }

    /**
     * Find all Students
     * Same as 1 but using two queries.
     * select person_type_id as typeId from person_type where persontype.name=STUDENT
     * select * from person where person_type_id=typeId
     */
    @Test
    public void testFindAllStudent3() {
        CriteriaComposer<PersonType> studentTypeCriteria = CriteriaComposer.from(PersonType.class);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        PersonType studentType = criteriaProcessor.findUniqueEntity(studentTypeCriteria, null);

        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.where(Person_.personType, EQUAL, studentType);

        List<Person> result = criteriaProcessor.findAllEntity(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(9, result.size());
    }

    /**
     * Find all students enrolled in course1
     * <p>
     * select * from person
     * inner join person_type
     * inner join course_session
     * inner join course
     * where persontype.name=STUDENT and course3.name=COURSE1
     */
    @Test
    public void testFindAllStudentEnrolledInClass1() {
        CriteriaComposer<PersonType> studentTypeCriteria = CriteriaComposer.from(PersonType.class);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.join(JoinType.INNER, Person_.personType, studentTypeCriteria);
        studentCriteria.join(JoinType.INNER, Person_.courseSessions).
                join(JoinType.INNER, CourseSession_.course).
                where(Course_.name, EQUAL, "course1");

        List<Person> result = criteriaProcessor.findAllEntity(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(6, result.size());
        assertEquals("student1", result.get(0).getName());
    }

    /**
     * Same as ...[1] but using dot operator to chain the method calls.
     */
    @Test
    public void testFindAllStudentEnrolledInClass2() {
        CriteriaComposer<PersonType> studentTypeCriteria = CriteriaComposer.from(PersonType.class);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.join(JoinType.INNER, Person_.personType, studentTypeCriteria);

        studentCriteria.join(JoinType.INNER, Person_.courseSessions).
                join(JoinType.INNER, CourseSession_.course).
                where(Course_.name, EQUAL, "course1");

        List<Person> result = criteriaProcessor.findAllEntity(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(6, result.size());
        assertEquals("student1", result.get(0).getName());
    }

    /**
     * Same as ...[1] but criteria is supplied externally, makes service more
     * readable.
     */
    @Test
    public void testFindAllStudentEnrolledInClass3() {
        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        CriteriaComposer<PersonType> studentTypeCriteria = studentCriteria.join(JoinType.INNER, Person_.personType);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        studentCriteria.join(JoinType.INNER, Person_.courseSessions).
                join(JoinType.INNER, CourseSession_.course).
                where(Course_.name, EQUAL, "course1");

        List<Person> result = criteriaProcessor.findAllEntity(studentCriteria,
                true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(6, result.size());
        assertEquals("student1", result.get(0).getName());
    }

    /**
     * Same as ...[1] but using more fine grained criteria API.
     * <p>
     * select courseId from course where course.name=?
     * select * from person inner join person_type
     * inner join course_session
     * where persontype.name=STUDENT and course.id=courseId
     */
    @Test
    public void testFindAllStudentEnrolledInClass4() {
        CriteriaComposer<Course> courseCriteria = CriteriaComposer.from(Course.class);
        courseCriteria.where(Course_.name, ComparisonOperator.EQUAL, "course1");

        Course course = criteriaProcessor.findUniqueEntity(courseCriteria, null);

        CriteriaComposer<PersonType> studentTypeCriteria = CriteriaComposer.from(PersonType.class);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);


        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.join(JoinType.INNER, Person_.personType, studentTypeCriteria);
        studentCriteria.join(JoinType.INNER, Person_.courseSessions).
                where(CourseSession_.course, EQUAL, course);

        List<Person> result = criteriaProcessor.findAllEntity(studentCriteria);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(6, result.size());
        assertEquals("student1", result.get(0).getName());
    }

    /**
     * Tuple search with selected columns
     * <p>
     * select person.id, person.name from person
     * inner join person_type
     * where persontype.name=STUDENT
     */
    @Test
    public void testFindAllStudentTuple1() {

        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.select(Person_.id, Person_.name);
        studentCriteria.join(JoinType.INNER, Person_.personType).
                where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        List<Tuple> result = criteriaProcessor.findAllTuple(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(9, result.size());
        assertEquals("student1", result.get(0).get("Person.name"));
    }

    /**
     * Tuple search with selected columns. Same as 1 but select has user defined
     * alias.
     */
    @Test
    public void testFindAllStudentTuple2() {

        CriteriaComposer<PersonType> studentTypeCriteria = CriteriaComposer.from(PersonType.class);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.select(Person_.id, "id");
        studentCriteria.select(Person_.name, "name");
        studentCriteria.join(JoinType.INNER, Person_.personType,
                studentTypeCriteria);

        List<Tuple> result = criteriaProcessor.findAllTuple(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(9, result.size());
        assertEquals("student1", result.get(0).get("name"));
    }

    /**
     * Find unique student
     * <p>
     * select personTypeId from person_type where persontype.name=STUDENT fetch
     * first 2 rows only
     * select * from person where person_type_id=personTypeId and
     * person.name=STUDENT1 fetch first 2 rows only
     */
    @Test
    public void testFindUniqueStudent() {
        //query 1
        CriteriaComposer<PersonType> studentTypeCriteria = CriteriaComposer.from(PersonType.class);
        studentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        PersonType studentType = criteriaProcessor.findUniqueEntity(studentTypeCriteria, null);

        //query 2
        CriteriaComposer<Person> personCriteria = CriteriaComposer.from(Person.class);
        personCriteria.where(Person_.personType, EQUAL, studentType);
        personCriteria.and();
        personCriteria.where(Person_.name, EQUAL, "student1");

        Person result = criteriaProcessor.findUniqueEntity(personCriteria, null);

        assertNotNull(result);
        assertEquals("student1", result.getName());
    }


    /**
     * OrderBy
     * <p>
     * select * from course
     * where course.last_update_date_time between ? and ?
     * order by course.name desc
     */
    @Test
    public void testOrderBy() {
        Date thatDate = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            thatDate = simpleDateFormat.parse("2011-01-02 00:00:00");
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage());
            fail(e.getMessage());
        }

        Date toDate = new Date(System.currentTimeMillis());

        CriteriaComposer<Course> criteria = CriteriaComposer.from(Course.class);
        criteria.where(Course_.lastUpdateDateTime, ComparisonOperator.BETWEEN, thatDate, toDate);
        criteria.orderBy(Course_.name, false, 1);

        List<Course> result = criteriaProcessor.findAllEntity(criteria, true, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("course3", result.get(0).getName());
    }

    /**
     * Test Order By
     * OrderBy can be provided for any and for multiple entities who are part of
     * a criteria.
     * OrderBy can be ranked independent of join order.
     * If we change rank for Course_name to 2 then this orderBy will be ignored
     * since higher level
     * entity Person has the orderBy with the same rank.
     * <p>
     * select person.name, person.id, course.name from person
     * inner join person_type
     * inner join course_session
     * inner join course
     * where persontype.name=? and (course.name in (? , ?))
     * order by course.name desc, person.name desc
     */
    @Test
    public void testOrderBy2() {

        //select student
        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.select(Person_.name, Person_.id);
        studentCriteria.orderBy(Person_.name, false, 2);
        studentCriteria.join(JoinType.INNER, Person_.personType).
                where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        //Note that dot chaining canot continue any more since the new join is on person table not on person type table.

        //select course
        CriteriaComposer<Course> courseCriteria = studentCriteria.join(Person_.courseSessions).join(CourseSession_.course);
        courseCriteria.select(Course_.name);
        courseCriteria.where(Course_.name, IN, "course1", "course3");
        courseCriteria.orderBy(Course_.name, false, 1);

        List<Tuple> result = criteriaProcessor.findAllTuple(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(11, result.size());
        assertEquals("course3", result.get(0).get("Course.name"));
        assertEquals("student9", result.get(0).get("Person.name"));
    }

    /**
     * Test Order By. Same as 2
     * DOT chaining some time is not easy to debug. But can be more readable.
     */
    @Test
    public void testOrderBy2b() {
        CriteriaComposer<PersonType> stydentTypeCriteria = CriteriaComposer.from(PersonType.class);
        stydentTypeCriteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.
                select(Person_.name).
                select(Person_.id).
                orderBy(Person_.name, false, 2).
                join(Person_.personType, stydentTypeCriteria);

        studentCriteria.
                join(Person_.courseSessions).
                join(CourseSession_.course).
                select(Course_.name).
                where(Course_.name, IN, "course1", "course3").
                orderBy(Course_.name, false, 1);

        List<Tuple> result = criteriaProcessor.findAllTuple(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(11, result.size());
        assertEquals("course3", result.get(0).get("Course.name"));
        assertEquals("student9", result.get(0).get("Person.name"));
    }


    /**
     * Test Order By. Same as 2
     * <p>
     * Framework is flexible. You can write criteria in any format you want.
     */
    @Test
    public void testOrderBy2c() {
        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.select(Person_.name, Person_.id);
        studentCriteria.orderBy(Person_.name, false, 2);
        studentCriteria.join(JoinType.INNER, Person_.personType).
                where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        CriteriaComposer<Course> course = studentCriteria.join(Person_.courseSessions).join(CourseSession_.course);
        course.select(Course_.name);
        course.where(Course_.name, IN, "course1", "course3");
        course.orderBy(Course_.name, false, 1);

        List<Tuple> result = criteriaProcessor.findAllTuple(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(11, result.size());
        assertEquals("course3", result.get(0).get("Course.name"));
        assertEquals("student9", result.get(0).get("Person.name"));
    }


    /**
     * Test Order By. If there is no attribute is selected then it throws exception
     * <p>
     * Framework is flexible. You can write criteria in any format you want.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testOrderBy3() {
        CriteriaComposer<Person> student = CriteriaComposer.from(Person.class);
        student.join(JoinType.INNER, Person_.personType).
                where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

        CriteriaComposer<Course> course = student.join(Person_.courseSessions).join(CourseSession_.course);
        course.where(Course_.name, IN, "course1", "course3");

        List<Tuple> result = criteriaProcessor.findAllTuple(student, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(6, result.size());
        assertEquals("course3", result.get(0).get("Course.name"));
        assertEquals("student9", result.get(0).get("Person.name"));
    }


    /**
     * select count(course.id) from course
     */
    @Test
    public void testSelectCount() {
        CriteriaComposer<Course> forCourse = CriteriaComposer.from(Course.class).select(COUNT, Course_.id);

        Tuple result = criteriaProcessor.findUniqueTuple(forCourse);

        assertNotNull(result);
        assertEquals(3L, result.get("count.Course.id"));
    }


    /**
     * sum(course.unit) from course
     */
    @Test
    public void testSelectSum() {
        CriteriaComposer<Course> forCourse = CriteriaComposer.from(Course.class).select(SUM, Course_.unit);

        Tuple result = criteriaProcessor.findUniqueTuple(forCourse);

        assertNotNull(result);
        assertEquals(10L, result.get("sum.Course.unit"));
    }


    /**
     * select person.name, sum(course.unit) from person inner join course_session inner join course group by person.name
     */
    @Test
    public void testGroupBy() {
        CriteriaComposer<Person> forStudentUnitCount = CriteriaComposer.from(Person.class).select(Person_.name).groupBy(Person_.name);
        forStudentUnitCount.join(Person_.courseSessions).join(CourseSession_.course).select(SUM, Course_.unit);

        List<Tuple> result = criteriaProcessor.findAllTuple(forStudentUnitCount);

        assertNotNull(result);
        assertEquals(9, result.size());
        assertEquals("student1", result.get(0).get("Person.name"));
        assertEquals(10L, result.get(0).get("sum.Course.unit"));

        for (int i = 1; i < result.size(); i++) {
            assertTrue(!result.get(i).get("Person.name").equals(result.get(0).get("Person.name")));
        }
    }


    @Test
    public void testNotIn() {
        CriteriaComposer<Person> forPerson = CriteriaComposer.from(Person.class).where(Person_.name, NOT, IN, "teacher1", "teacher2", "teacher3");

        List<Person> result = criteriaProcessor.findAllEntity(forPerson);
        assertNotNull(result);
        assertEquals(9, result.size());

        for (Person person : result) {
            assertEquals(false, person.getName().contains("teacher"));
        }
    }


    @Test
    public void testLessThan() {
        List<Course> result = criteriaProcessor.findAllEntity(CriteriaComposer.from(Course.class).where(Course_.unit, LESS_THAN, 5));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("course1", result.get(0).getName());
        assertEquals("course3", result.get(1).getName());

    }

    @Test
    public void testGreaterThan() {
        List<Course> result = criteriaProcessor.findAllEntity(CriteriaComposer.from(Course.class).where(Course_.unit, GREATER_THAN, 3));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course2", result.get(0).getName());
        assertEquals(5, result.get(0).getUnit().intValue());
    }


    @Test
    public void testDelayedOrderBy() {
        //select course where course name is in ...
        CriteriaComposer<Course> courseCriteria = CriteriaComposer.from(Course.class);
        courseCriteria.select(Course_.name).where(Course_.name, IN, "course1", "course3");

        //slect student who has taken these courses
        CriteriaComposer<Person> studentCriteria = CriteriaComposer.from(Person.class);
        studentCriteria.select(Person_.name, Person_.id);
        studentCriteria.join(Person_.personType).
                where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);
        studentCriteria.join(Person_.courseSessions).join(CourseSession_.course, courseCriteria);


        //------2.0 alpha test. See orderBy is done seperate from the above criteria.
        setOrderByForDelayOrderByTest(studentCriteria);

        List<Tuple> result = criteriaProcessor.findAllTuple(studentCriteria, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(11, result.size());
        assertEquals("course3", result.get(0).get("Course.name"));
        assertEquals("student9", result.get(0).get("Person.name"));
    }

    /**
     * Some time  orderBy need to be done separately from the main criteria.
     * This information may be provided dynamically. This can be done now like this.
     *
     * @param studentCriteria
     */
    private void setOrderByForDelayOrderByTest(
            CriteriaComposer<Person> studentCriteria) {
        studentCriteria.orderBy(Person_.name, false, 2);
        studentCriteria.join(Person_.courseSessions).join(CourseSession_.course).
                orderBy(Course_.name, false, 1);
    }


    /**
     * select person.name, sum(course2_.unit)
     * from person
     * join course_session, inner join course
     * group by person.name
     * having sum(course2_.unit)=2
     */
    @Test
    public void testHaving() {
        CriteriaComposer<Person> forStudentUnitCount = CriteriaComposer.from(Person.class).
                select(Person_.name).groupBy(Person_.name);
        forStudentUnitCount.join(Person_.courseSessions).
                join(CourseSession_.course).
                select(SUM, Course_.unit).
                having(SUM, Course_.unit, EQUAL, 2);

        List<Tuple> result = criteriaProcessor.findAllTuple(forStudentUnitCount);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("student7", result.get(0).get("Person.name"));
        assertEquals(2L, result.get(0).get("sum.Course.unit"));

        for (int i = 1; i < result.size(); i++) {
            assertTrue(!result.get(i).get("Person.name").equals(result.get(0).get("Person.name")));
        }
    }
}
