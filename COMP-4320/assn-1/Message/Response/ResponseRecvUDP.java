package Message.Response;

import Message.MessageDecoder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ResponseRecvUDP {

  public static Response receive(int port) throws Exception {

      DatagramSocket sock = new DatagramSocket(port);  // UDP socket for receiving      
      DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
      sock.receive(packet);
      
      // Receive binary-encoded request
      MessageDecoder decoder = new ResponseDecoderBin();

      Response receivedMessage = decoder.decode(packet);
      
      sock.close();

      return receivedMessage;
  }
}
