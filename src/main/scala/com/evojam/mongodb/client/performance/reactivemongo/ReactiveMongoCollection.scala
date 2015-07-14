package com.evojam.mongodb.client.performance.reactivemongo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

import reactivemongo.api.collections.bson.BSONCollection

import com.evojam.mongodb.client.performance.{Collection => PCollection, Config}
import reactivemongo.api._
import reactivemongo.bson.BSONDocument

trait ReactiveMongoCollection extends PCollection[BSONDocument, BSONCollection] with Config {
  override lazy val genDocuments =
    allDocuments.map(_.map(_._2))

  override def getCollection() = {
    val db: DefaultDB = new MongoDriver()
      .connection(List("localhost"))(DbName)
    db.collection(ColName)
  }

  override def insertBefore(collection: BSONCollection, docs: List[BSONDocument]) = {
    val res = Future.sequence(docs.map(collection.insert(_)))
    Await.result(res.map(_ => ()), Duration.Inf)
  }

  override def dropAfter(collection: BSONCollection) = {
    Await.result(collection.drop(), Duration.Inf)
  }
}
