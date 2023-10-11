import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.Request.Request;
import Message.Request.RequestEncoderBin;
import Message.Response.Response;
import Message.Response.ResponseDecoderBin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    public static void main(String[] args) throws Exception {
        // Validate arguments
        // <host> <port#>
        if(args.length != 2) {
            System.out.println("Improper args provided.");
            return;
        }
        final String svHost = args[0];
        final int svPort = Integer.parseInt(args[1]);
        int clPort = 10099;



        while(true){

            // Get n and validate input
            System.out.print("\n" + "Please enter a number from [-32768, 32767]: ");
            short n;
            try{
                n = Short.parseShort(System.console().readLine());
            } catch (Exception e){
                System.out.println("Enter a proper number.");
                continue;
            }

            // Generate request object
            Request request = new Request(n);

            // Print hex bytes of n
            System.out.println("Sending number: ");
            System.out.println(request);

            // Establish socket
            DatagramSocket sock = new DatagramSocket(clPort); // UDP socket for sending

            // Encode request
            MessageEncoder<Request> encoder = new RequestEncoderBin();
            byte[] codedRequest = encoder.encode(request);

            // Send packet
            InetAddress destAddr = InetAddress.getByName(svHost);  // Destination address
            DatagramPacket message = new DatagramPacket(codedRequest, codedRequest.length,
                    destAddr, svPort);
            sock.send(message);
            long sendTime = System.currentTimeMillis(); // Start timer

            // Wait for response
            System.out.println("Listening for response on port: " + clPort);
            DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
            sock.receive(packet);
            long duration = System.currentTimeMillis() - sendTime; // End timer
            // Decode request
            MessageDecoder decoder = new ResponseDecoderBin();
            Response response = decoder.decode(packet);
            sock.close();

            // Print response
            System.out.println("Received response in " + duration + "ms: ");
            System.out.println(response);
        }
    }
}