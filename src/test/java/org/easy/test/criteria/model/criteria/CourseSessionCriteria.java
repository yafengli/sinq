package org.easy.test.criteria.model.criteria;

import org.easy.criteria.CriteriaComposer;
import org.easy.criteria.CriteriaComposer.ComparisonOperator;
import org.easy.test.criteria.model.generated.Course;
import org.easy.test.criteria.model.generated.CourseSession;
import org.easy.test.criteria.model.generated.CourseSession_;
public class CourseSessionCriteria
{

	public static CriteriaComposer<CourseSession> findSessionsFor(Course course)
	{
		CriteriaComposer<CourseSession> criteria = CriteriaComposer.from(CourseSession.class);
		criteria.where(CourseSession_.course, ComparisonOperator.EQUAL, course);

		return criteria;
	}

}
