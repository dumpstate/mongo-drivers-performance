package com.evojam.mongodb.client.performance.evoscala

import com.evojam.mongodb.client._
import com.evojam.mongodb.client.performance.Collection
import com.evojam.mongodb.client.performance.Config._
import org.bson.Document

trait EvoScalaCollection extends Collection[Document, MongoCollection] {
  override lazy val documents =
    allDocuments.map(_.map(_._1))

  override def getCollection() = MongoClients.create()
    .database(DbName)
    .collection(ColName)
}
