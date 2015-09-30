/*
 * Copyright (C) 2014  The Async HBase Authors.  All rights reserved.
 * This file is part of Async HBase.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the StumbleUpon nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.hbase.async;

import org.jboss.netty.buffer.ChannelBuffer;

import org.hbase.async.generated.FilterPB;
import org.hbase.async.generated.HBasePB;

import com.google.protobuf.ByteString;

import com.mapr.fs.proto.Dbfilters.FirstKeyOnlyFilterProto;

/**
 * A filter that will only return the first KV from each row.
 * This filter can be used to more efficiently perform row count operations.
 */
public class FirstKeyOnlyFilter extends ScanFilter {

  private static final byte[] NAME =
      Bytes.UTF8("org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter");

  public FirstKeyOnlyFilter() { }


  @Override
  byte[] name() {
    return NAME;
  }

  @Override
  byte[] serialize() {
    return FilterPB
        .FirstKeyOnlyFilter
        .newBuilder()
        .build()
        .toByteArray();
  }

  @Override
  void serializeOld(ChannelBuffer buf) {
    // Write the filter name
    buf.writeByte((byte) name().length);            // 1
    buf.writeBytes(name());                         // name().length
  }

  @Override
  int predictSerializedSize() {
    return 1 + name().length;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  // MapR addition
  public static final int kFirstKeyOnlyFilter              = 0xf1482e61;

  @Override
  protected ByteString getState() {
    return FirstKeyOnlyFilterProto.newBuilder()
            .build().toByteString();
  }

  @Override
  protected String getId() {
    return getFilterId(kFirstKeyOnlyFilter);
  }


}
