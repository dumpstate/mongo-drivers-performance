package com.evojam.mongodb.client.performance.reactivemongo

import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.api.collections.bson.BSONCollection

import com.evojam.mongodb.client.performance.{Collection => PCollection}
import com.evojam.mongodb.client.performance.Config._
import reactivemongo.api._
import reactivemongo.bson.BSONDocument

trait ReactiveMongoCollection extends PCollection[BSONDocument, BSONCollection] {
  override lazy val documents =
    allDocuments.map(_.map(_._2))

  override def getCollection() = {
    val db: DefaultDB = new MongoDriver()
      .connection(List("localhost"))(DbName)
    db.collection(ColName)
  }
}
