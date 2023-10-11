package Message.Request;

import Message.MessageEncoder;

import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress


public class RequestSendUDP {

  public static int send(String destination, int svPort, Request request) throws Exception {

      
      InetAddress destAddr = InetAddress.getByName(destination);  // Destination address

      DatagramSocket sock = new DatagramSocket(); // UDP socket for sending

      MessageEncoder<Request> encoder = new RequestEncoderBin();

      byte[] codedRequest = encoder.encode(request); // Encode request
      
      DatagramPacket message = new DatagramPacket(codedRequest, codedRequest.length,
						  destAddr, svPort);

      // Return port to listen for response
      int port = sock.getLocalPort();
      sock.send(message);
      sock.close();
      return port;
  }
}
