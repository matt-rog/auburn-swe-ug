package Message.Response;

import Message.MessageEncoder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ResponseSendUDP {

  public static void send(String destination, int port, Response response) throws Exception {

      
      InetAddress destAddr = InetAddress.getByName(destination);  // Destination address

      DatagramSocket sock = new DatagramSocket(); // UDP socket for sending
      
      // Use the encoding scheme given on the command line (args[2])
      MessageEncoder<Response> encoder = new ResponseEncoderBin();

      byte[] codedRequest = encoder.encode(response); // Encode request
      
      DatagramPacket message = new DatagramPacket(codedRequest, codedRequest.length,
						  destAddr, port);
      sock.send(message);
      
      sock.close();
  }
}
