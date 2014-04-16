package org.easy.test.criteria;

import org.easy.criteria.BaseDAO;
import org.easy.criteria.CriteriaComposer;
import org.easy.test.criteria.model.criteria.PersonCriteria;
import org.easy.test.criteria.model.generated.*;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Tuple;
import java.util.List;

import static org.easy.criteria.CriteriaComposer.ComparisonOperator.IN;
import static org.junit.Assert.*;

public class BaseDaoTest {

    private static BaseDAO baseDao = null;

    private static boolean initContextByMe = false;
    private static Logger log = LoggerFactory.getLogger(BaseDaoTest.class);

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
            baseDao = new BaseDAO(DbHelper.getEntityManager());

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        /*if (initContextByMe)
            DbHelper.close();*/
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test Order By. Same as 2
     * <p>
     * select person.name, person.id, course.name from person
     * inner join person_type
     * inner join course_session
     * inner join course
     * where persontype.name=? and (course.name in (? , ?))
     * order by course.name desc, person.name desc
     */
    @Test
    public void testOrderBy2c() {
        CriteriaComposer<Person> student = PersonCriteria.findStudent();
        student.select(Person_.name, Person_.id);
        student.orderBy(Person_.name, false, 2);

        CriteriaComposer<Course> course = student.join(Person_.courseSessions).join(CourseSession_.course);
        course.select(Course_.name);
        course.where(Course_.name, IN, "course1", "course3");
        course.orderBy(Course_.name, false, 1);

        List<Tuple> result = baseDao.findAllTuple(student, true, null);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(11, result.size());
        assertEquals("course3", result.get(0).get("Course.name"));
        assertEquals("student9", result.get(0).get("Person.name"));
    }


    @Test
    public void testFindChild() {
        //Person student = baseDao.findById(Person.class, 1L);
        Person teacher = new Person();
        teacher.setId(1L);
        List<Course> courses = baseDao.findByAssociation(Course.class, Course_.teacher, teacher);

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals("course1", courses.get(0).getName());
    }


    @Test
    public void testFindByPropertyUnique() {
        List<Person> persons = baseDao.findByProperty(Person.class, Person_.name, "student1");
        assertNotNull(persons);
        assertEquals(1, persons.size());
        assertEquals("student1", persons.get(0).getName());
    }


}

