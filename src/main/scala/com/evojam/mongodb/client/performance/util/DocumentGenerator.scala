package com.evojam.mongodb.client.performance.util

import scala.collection.JavaConversions._
import scala.util.Random

import org.bson.Document
import reactivemongo.bson.{BSONString, BSONBoolean, BSONDouble, BSONDocument}

trait DocumentGenerator {

  private val MinArraySize = 100
  private val MaxArraySize = 3000

  private val MinDocSize = 5
  private val MaxDocSize = 50

  private val MinPropNameLength = 5
  private val MaxPropNameLength = 20

  private val MaxPropLength = 300

  object JsonType extends Enumeration {
    type JsonType = Value
    // TODO handle Document and Array
    val Number, String, Boolean = Value
  }

  private def nextArraySize(size: Option[Int]) =
    size.getOrElse(Random.nextInt(MaxArraySize - MinArraySize) + MinArraySize)

  def nextDocSize() =
    Random.nextInt(MaxDocSize - MinDocSize) + MinDocSize

  private def nextPropName() =
    Random.alphanumeric
      .take(Random.nextInt(MaxPropNameLength - MinPropNameLength) + MinPropNameLength)
      .mkString

  private def nextJsonType() =
    Random.nextInt(3) match {
      case 0 => JsonType.Number
      case 1 => JsonType.Boolean
      case _ => JsonType.String
    }

  private def nextJsonValue(propType: JsonType.Value) = propType match {
    case JsonType.Number => Random.nextDouble()
    case JsonType.Boolean => Random.nextBoolean()
    case JsonType.String => Random.alphanumeric.take(MaxPropLength).mkString
  }

  def nextSchema(
    map: Map[String, JsonType.Value] = Map.empty,
    size: Int = 1): Map[String, JsonType.Value] = size match {
      case size if size > 0 => {
        nextSchema(map + (nextPropName() -> nextJsonType()), size - 1)
      }
      case _ => map
  }

  def nextDocument(schema: Map[String, JsonType.Value]): Document = {
    val doc = new Document()
    schema.foreach {
      case (propName, propType) => doc.append(propName, nextJsonValue(propType))
    }
    doc
  }

  def documents(arraySize: Option[Int], schema: Map[String, JsonType.Value]): List[Document] =
    (for(i <- 1 to nextArraySize(arraySize))
      yield nextDocument(schema)).toList

  def documents(arraySize: Option[Int]): List[Document] =
    documents(arraySize, nextSchema(size = nextDocSize()))

  def docs2ReactiveMongoDocument(docs: List[Document], schema: Map[String, JsonType.Value]): List[BSONDocument] =
    docs.map(doc2ReactiveMongoDocument(_, schema))

  def doc2ReactiveMongoDocument(doc: Document, schema: Map[String, JsonType.Value]): BSONDocument = {
    var bson = BSONDocument.empty
    schema.foreach {
      case (name, jsonType) => jsonType match {
        case JsonType.Number =>
          bson = bson.add(BSONDocument(List((name, BSONDouble(doc.getDouble(name))))))
        case JsonType.Boolean =>
          bson = bson.add(BSONDocument(List((name, BSONBoolean(doc.getBoolean(name))))))
        case JsonType.String =>
          bson = bson.add(BSONDocument(List((name, BSONString(doc.getString(name))))))
      }
    }
    bson
  }

  def arbitraryProperty(docs: List[Document]): String =
    docs.headOption
      .map(doc => asScalaSet(doc.keySet())
        .headOption.getOrElse(throw new Exception("Document must have at least one property")))
      .getOrElse(throw new Exception("Threre must be at lease one document in a list."))
}
