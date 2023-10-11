package Message.Request;


import Message.*;

import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

public class RequestEncoderBin implements MessageEncoder<Request> {

  private String encoding;  // Character encoding

  public RequestEncoderBin() {
    encoding = "UTF-16";
  }

  public RequestEncoderBin(String encoding) {
    this.encoding = encoding;
  }

  public byte[] encode(Request request) throws Exception {

    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(buf);
    out.writeShort(request.n);
    out.flush();
    return buf.toByteArray();
  }
}
