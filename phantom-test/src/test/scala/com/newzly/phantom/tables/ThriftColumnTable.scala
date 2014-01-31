package com.newzly.phantom.tables

import org.apache.thrift.protocol.{TCompactProtocol, TBinaryProtocol}
import org.apache.thrift.transport.TMemoryInputTransport

import com.datastax.driver.core.Row
import com.newzly.phantom.column.ThriftColumn
import com.newzly.phantom.Implicits._
import com.newzly.phantom.keys.PrimaryKey
import com.newzly.phantom.helper.TestSampler
import com.newzly.phantom.thrift.ThriftTest
import com.twitter.scrooge.CompactThriftSerializer

case class Output(id: Int, name: String, struct: ThriftTest)

sealed class ThriftColumnTable extends CassandraTable[ThriftColumnTable, Output] {

  def meta = ThriftColumnTable

  object id extends IntColumn(this) with PrimaryKey
  object name extends StringColumn(this)
  object ref extends ThriftColumn[ThriftColumnTable, Output, ThriftTest](this) {
    val serializer = new CompactThriftSerializer[ThriftTest] {
      def codec = ThriftTest
    }
  }

  def fromRow(row: Row): Output = {
    Output(id(row), name(row), ref(row))
  }
}

object ThriftColumnTable extends ThriftColumnTable with TestSampler[ThriftColumnTable, Output] {}