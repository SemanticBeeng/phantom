/*
 * Copyright 2013 - 2017 Outworkers Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.outworkers.phantom.tables

import com.outworkers.phantom.connectors.RootConnector
import com.outworkers.phantom.builder.query.InsertQuery
import com.outworkers.phantom.dsl._

import scala.concurrent.Future

case class ScalaPrimitiveMapRecord(
  id: UUID,
  map: Map[DateTime, BigDecimal]
)

class ScalaTypesMapTable extends CassandraTable[ConcreteScalaTypesMapTable, ScalaPrimitiveMapRecord] {

  object id extends UUIDColumn(this) with PartitionKey
  object map extends MapColumn[DateTime, BigDecimal](this)
}

abstract class ConcreteScalaTypesMapTable extends ScalaTypesMapTable with RootConnector {
  def store(
    rec: ScalaPrimitiveMapRecord
  ): InsertQuery.Default[ConcreteScalaTypesMapTable, ScalaPrimitiveMapRecord] = {
    insert
      .value(_.id, rec.id)
      .value(_.map, rec.map)
  }

  def findById(id: UUID): Future[Option[ScalaPrimitiveMapRecord]] = {
    select.where(_.id eqs id).one()
  }
}