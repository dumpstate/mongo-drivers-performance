package com.evojam.mongodb.client.performance

import org.scalameter.api._

import com.evojam.mongodb.client.performance.util.DocumentGenerator

trait Config extends DocumentGenerator {
  val DbName = "performancetest"
  val ColName = "collection"

  val InsertBeforeFind = 100

  val sizes: Gen[Int] = Gen.range("sizes")(100, 1000, 100)

  val docSize = 3//nextDocSize

  val schema = nextSchema(size = docSize)

  lazy val allDocuments = sizes
    .map(size => documents(Some(size), schema))
    .map(doc => (doc, docs2ReactiveMongoDocument(doc, schema)))
    .map(tpl => tpl._1.zip(tpl._2))
}
