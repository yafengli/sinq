package models.ext

import javax.persistence.AttributeConverter

import org.eclipse.persistence.mappings.DatabaseMapping
import org.eclipse.persistence.mappings.converters.Converter
import org.eclipse.persistence.sessions.Session

class InetConverter extends Converter {
  override def isMutable: Boolean = true

  override def convertObjectValueToDataValue(o: scala.Any, session: Session): AnyRef = o.toString

  override def initialize(databaseMapping: DatabaseMapping, session: Session): Unit =

  override def convertDataValueToObjectValue(o: scala.Any, session: Session): InetObject = o
}


class InetJpaConverter extends AttributeConverter[InetObject, String] {
  override def convertToEntityAttribute(attribute: String): InetObject = {
    InetObject(attribute)
  }

  override def convertToDatabaseColumn(dbData: InetObject): String = {
    dbData.name
  }
}