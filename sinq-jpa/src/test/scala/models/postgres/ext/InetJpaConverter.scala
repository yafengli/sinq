package models.postgres.ext

import java.net.InetAddress
import javax.persistence.{AttributeConverter, Converter}

import org.postgresql.util.PGobject

@Converter
class InetJpaConverter extends AttributeConverter[InetAddress, PGobject] {
  override def convertToEntityAttribute(attribute: PGobject): InetAddress = {
    InetAddress.getByName(attribute.getValue)
  }

  override def convertToDatabaseColumn(dbData: InetAddress): PGobject = {
    val obj = new PGobject
    obj.setType("inet")
    obj.setValue(dbData.getHostName)
    obj
  }
}