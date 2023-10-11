import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.Request.RequestDecoderBin;
import Message.Request.Request;
import Message.Response.Response;
import Message.Response.ResponseEncoderBin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) throws Exception {
        // Validate arguments
        // <port#>
        if (args.length != 1) {
            System.out.println("Improper args provided.");
            return;
        }
        final int port = Integer.parseInt(args[0]);



        while(true){
            // Start socket
            DatagramSocket sock = new DatagramSocket(port);  // UDP socket for receiving
            DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
            System.out.println("Listening on port: " + port);

            // Wait + receive request
            sock.receive(packet);

            // Decode request
            MessageDecoder decoder = new RequestDecoderBin();
            Request request = decoder.decode(packet);

            // Print n, hex values, host, and port
            System.out.println("\n" + "Received request:");
            System.out.println(request);

            // Generate string value of short n, "***" if not possible
            String text;
            try{
                text = String.valueOf(request.n);
            } catch (Exception e){
                text = "****";
            }
            Response response = new Response(text);

            // Print string hex
            System.out.println("Sending response: ");
            System.out.println(response);

            // Encode response
            MessageEncoder<Response> encoder = new ResponseEncoderBin();
            byte[] codedRequest = encoder.encode(response);
            DatagramPacket message = new DatagramPacket(codedRequest, codedRequest.length,
                    request.host, request.port);

            // Send response back to client
            sock.send(message);
            sock.close();
        }
    }
}