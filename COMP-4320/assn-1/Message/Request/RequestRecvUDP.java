package Message.Request;

import Message.*;

import java.net.*;  // for DatagramSocket and DatagramPacket

public class RequestRecvUDP {

  public static Request receive(int port) throws Exception {

      DatagramSocket sock = new DatagramSocket(port);  // UDP socket for receiving      
      DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
      sock.receive(packet);
      
      // Receive binary-encoded request
      MessageDecoder decoder = new RequestDecoderBin();
      Request receivedMessage = decoder.decode(packet);
      
      sock.close();

      return receivedMessage;
  }
}
