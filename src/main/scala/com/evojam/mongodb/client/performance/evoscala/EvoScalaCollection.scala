package com.evojam.mongodb.client.performance.evoscala

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import com.evojam.mongodb.client._
import com.evojam.mongodb.client.performance.{Config, Collection}
import org.bson.Document

trait EvoScalaCollection extends Collection[Document, MongoCollection] with Config {
  override lazy val genDocuments =
    allDocuments.map(_.map(_._1))

  override def getCollection() = MongoClients.create()
    .database(DbName)
    .collection(ColName)

  override def insertBefore(collection: MongoCollection, docs: List[Document]) =
    Await.result(collection.insertAll(docs), Duration.Inf)

  override def dropAfter(collection: MongoCollection) =
    Await.result(collection.drop(), Duration.Inf)
}
