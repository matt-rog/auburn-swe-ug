package Message.Response;


import Message.MessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ResponseDecoderBin implements MessageDecoder {

  private String encoding;  // Character encoding

  public ResponseDecoderBin() {
    encoding = "UTF-16";
  }

  public ResponseDecoderBin(String encoding) {
    this.encoding = encoding;
  }

  public Response decode(InputStream wire, InetAddress host, int port) throws IOException {
    DataInputStream src = new DataInputStream(wire);
    int stringLength = src.read(); // Returns an unsigned byte as an int
    if (stringLength == -1)
      throw new IOException();
    byte[] stringBuf = new byte[stringLength];
    src.readFully(stringBuf);
    String n = new String(stringBuf, StandardCharsets.UTF_16);
    return new Response(n, host, port);
  }

  public Response decode(DatagramPacket p) throws IOException {
    ByteArrayInputStream payload =
      new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
    return decode(payload, p.getAddress(), p.getPort());
  }
}
