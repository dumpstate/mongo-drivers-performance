package com.evojam.mongodb.client.performance.mongoasync

import org.bson.Document

import com.evojam.mongodb.client.performance.Collection
import com.evojam.mongodb.client.performance.Config._
import com.mongodb.async.client.{MongoCollection, MongoClients}

trait MongoAsyncCollection extends Collection[Document, MongoCollection[Document]] {
  override lazy val documents =
    allDocuments.map(_.map(_._1))

  override def getCollection() = MongoClients.create()
    .getDatabase(DbName)
    .getCollection(ColName)
}
