package com.evojam.mongodb.client.performance.mongocore

import com.evojam.mongodb.client.performance.Collection
import com.evojam.mongodb.client.performance.Config._
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document

trait MongoCoreCollection extends Collection[Document, MongoCollection[Document]] {
  override lazy val documents =
    allDocuments.map(_.map(_._1))

  override def getCollection() =
    new MongoClient()
      .getDatabase(DbName)
      .getCollection(ColName)
}
