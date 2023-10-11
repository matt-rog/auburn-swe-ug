package Message.Request;


import Message.MessageDecoder;
import Message.Request.Request;

import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class RequestDecoderBin implements MessageDecoder {

  private String encoding;  // Character encoding

  public RequestDecoderBin() {
    encoding = "UTF-16";
  }

  public RequestDecoderBin(String encoding) {
    this.encoding = encoding;
  }

  public Request decode(InputStream wire, InetAddress host, int port) throws IOException {
    DataInputStream src = new DataInputStream(wire);
    short n  = src.readShort();
    return new Request(n, host, port);
  }

  public Request decode(DatagramPacket p) throws IOException {
    ByteArrayInputStream payload =
      new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
    return decode(payload, p.getAddress(), p.getPort());
  }
}
