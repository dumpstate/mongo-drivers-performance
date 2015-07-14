package com.evojam.mongodb.client.performance.mongocore

import scala.collection.JavaConversions._
import scala.concurrent.Future

import com.evojam.mongodb.client.performance.{Config, Collection}
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document

trait MongoCoreCollection extends Collection[Document, MongoCollection[Document]] with Config {
  override lazy val genDocuments =
    allDocuments.map(_.map(_._1))

  override def getCollection() =
    new MongoClient()
      .getDatabase(DbName)
      .getCollection(ColName)

  override def insertBefore(collection: MongoCollection[Document], docs: List[Document]) =
    collection.insertMany(docs)

  override def dropAfter(collection: MongoCollection[Document]) =
    collection.drop()
}
