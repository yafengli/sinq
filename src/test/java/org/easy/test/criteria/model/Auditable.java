package org.easy.test.criteria.model;

import java.util.Date;

public interface Auditable
{
	public Date getLastUpdateDateTime();
	public void setLastUpdateDateTime(Date newDate);
	
	public Long getLastUpdateBy();
	public void setLastUpdateBy(Long personId);	
	
}
