package com.evojam.mongodb.client.performance

import org.scalameter.Parameters
import org.scalameter.api.Gen

trait Collection[T, R] {
  val genDocuments: Gen[List[T]]

  def documents(size: Int): List[T] =
    genDocuments.generate(Parameters("sizes" -> size))

  def getCollection(): R

  def insertBefore(collection: R, docs: List[T]): Unit

  def dropAfter(collection: R): Unit
}
