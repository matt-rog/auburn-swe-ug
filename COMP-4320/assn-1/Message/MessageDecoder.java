package Message;

import java.io.*;   // for InputStream and IOException
import java.net.*;  // for DatagramPacket

public interface MessageDecoder {
  <T extends Message<?>> T decode(InputStream source, InetAddress inetAddress, int port) throws IOException;
  <T extends Message<?>> T decode(DatagramPacket packet) throws IOException;
}
