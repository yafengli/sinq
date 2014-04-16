package org.easy.test.criteria.model.criteria;

import org.easy.criteria.CriteriaComposer;
import org.easy.criteria.CriteriaComposer.ComparisonOperator;
import org.easy.test.criteria.model.PersonTypeEnum;
import org.easy.test.criteria.model.generated.PersonType;
import org.easy.test.criteria.model.generated.PersonType_;

public class PersonTypeCriteria
{
	public static CriteriaComposer<PersonType> forAdminType()
	{
		CriteriaComposer<PersonType> criteria = CriteriaComposer.from(PersonType.class);
		criteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.ADMIN);

		return criteria;
	}

	public static CriteriaComposer<PersonType> forStudentType()
	{
		CriteriaComposer<PersonType> criteria = CriteriaComposer.from(PersonType.class);
		criteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.STUDENT);

		return criteria;
	}

	public static CriteriaComposer<PersonType> forTeacherType()
	{
		CriteriaComposer<PersonType> criteria = CriteriaComposer.from(PersonType.class);
		criteria.where(PersonType_.name, ComparisonOperator.EQUAL, PersonTypeEnum.TEACHER);

		return criteria;
	}
}
