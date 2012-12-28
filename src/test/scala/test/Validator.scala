package test

import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.{Validator => JValidator}
import org.xml.sax.SAXException

object Validator {
  def main(args: Array[String]) {
    val result =
      if (validate("F:\\Github\\sporm\\src\\test\\resources\\demo.xml", "f:\\exportdata.xsd"))
        "Validates!"
      else
        "Not valid."
    println(result)
  }

  def validate(xmlFile: String, xsdFile: String): Boolean = {
    try {
      val schemaLang = "http://www.w3.org/2001/XMLSchema"
      val factory = SchemaFactory.newInstance(schemaLang)
      val schema = factory.newSchema(new StreamSource(xsdFile))
      val validator = schema.newValidator()
      validator.validate(new StreamSource(xmlFile))
    } catch {
      case ex: SAXException => println(ex.getMessage()); return false
      case ex: Exception => ex.printStackTrace()
    }
    true
  }
}