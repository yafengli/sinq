package org.easy.test.criteria.model.criteria;

import java.util.Date;

import org.easy.criteria.CriteriaComposer;
import org.easy.criteria.CriteriaComposer.ComparisonOperator;
import org.easy.test.criteria.model.generated.Course;
import org.easy.test.criteria.model.generated.Course_;

public class CourseCriteria
{

	public static CriteriaComposer<Course> findCouorseUpdatedBetween(Date thatDate, Date toDate)
	{
		CriteriaComposer<Course> criteria = CriteriaComposer.from(Course.class);
		criteria.where(Course_.lastUpdateDateTime, ComparisonOperator.BETWEEN, thatDate, toDate);

		return criteria;
	}

	public static CriteriaComposer<Course> findCourseByName(String courseName)
	{
		CriteriaComposer<Course> criteria = CriteriaComposer.from(Course.class);
		criteria.where(Course_.name, ComparisonOperator.EQUAL, courseName);

		return criteria;
	}

}
