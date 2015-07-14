package com.evojam.mongodb.client.performance.mongoasync

import scala.collection.JavaConversions._
import scala.concurrent.{Await, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import com.mongodb.async.SingleResultCallback
import org.bson.Document

import com.evojam.mongodb.client.performance.{Config, Collection}
import com.mongodb.async.client.{MongoCollection, MongoClients}

trait MongoAsyncCollection extends Collection[Document, MongoCollection[Document]] with Config {
  override lazy val genDocuments =
    allDocuments.map(_.map(_._1))

  override def getCollection() = MongoClients.create()
    .getDatabase(DbName)
    .getCollection(ColName)

  override def insertBefore(collection: MongoCollection[Document], docs: List[Document]) = {
    val insert: Promise[Void] = Promise()
    collection.insertMany(docs, new SingleResultCallback[Void] {
      override def onResult(t: Void, throwable: Throwable) {
        if (throwable == null) {
          insert.success(t)
          ()
        } else {
          insert.failure(throwable)
          ()
        }
      }
    })
    Await.result(insert.future.map(_ => ()), Duration.Inf)
  }

  override def dropAfter(collection: MongoCollection[Document]) = {
    val drop: Promise[Void] = Promise()
    collection.drop(new SingleResultCallback[Void] {
      override def onResult(t: Void, throwable: Throwable) {
        if (throwable == null) {
          drop.success(t)
          ()
        } else {
          drop.failure(throwable)
          ()
        }
      }
    })
    Await.result(drop.future.map(_ => ()), Duration.Inf)
  }
}
