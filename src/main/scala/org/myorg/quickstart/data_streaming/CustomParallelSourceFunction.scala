package org.myorg.quickstart.data_streaming

import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}

class CustomParallelSourceFunction extends ParallelSourceFunction[Long]{

  var count = 1L
  var isRunning = true

  override def cancel(): Unit = {
    isRunning = false
  }

  override def run(ctx: SourceFunction.SourceContext[Long]): Unit = {
    while(isRunning) {
      ctx.collect(count)
      count += 1
      Thread.sleep(1000)
    }
  }

}
