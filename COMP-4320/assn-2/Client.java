import java.io.*;   // for Input/OutputStream
import java.net.*;  // for Socket
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
 
public class Client
{
    public static void main(String[] args) throws Exception 
    { // Client [host] [port]

        // Store response durations
        long[] durations = new long[5];

        // Instantiate input
        InetAddress serverHost = InetAddress.getByName(args[0]);  // Destination address
        int serverPort = Integer.parseInt(args[1]);

        // Create TCP socket with host on port
        Socket socket = new Socket(serverHost, serverPort);

        int i = 0;
        while(i < 5)
        {

            // Get n and validate input
            short n = 0;
            boolean validInput = false;
            while(!validInput)
            {
                try
                {
                    System.out.print("\nPlease enter a number in the range [-32768, 32767]: ");
                    n = Short.parseShort(System.console().readLine());
                    validInput = true;
                } 
                catch (Exception e)
                {
                    System.out.println("Enter a proper number.");
                }
            }
            System.out.println("Sending N=" + n + " to " + serverHost.toString() + " at port " + serverPort + ".");
            
            // Convert n to bytes
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.putShort(n);
            byte[] requestBytes = buffer.array();
            
            // Print byte array as hex
            String requestHex = "Hex= ";
            for(byte b : requestBytes)
            {
                requestHex += ("0x" + String.format("%02X", b) + " ");
            }
            System.out.println(requestHex);
            
            long sendTime = System.currentTimeMillis(); // Start timer
            
            // Send request
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(requestBytes);
            
            // Receive response
            InputStream input = socket.getInputStream();            
            DataInputStream responseStream = new DataInputStream(input);
            byte[] responseBytes = new byte[1000];
            int bytesRead = responseStream.read(responseBytes);
            
            // Record response time
            long duration = System.currentTimeMillis() - sendTime;
            durations[i] = duration;

            // Trim response bytes
            responseBytes = Arrays.copyOfRange(responseBytes, 0, bytesRead);
            
            String response = new String(responseBytes, StandardCharsets.UTF_16);
            System.out.println("Received S=\"" + response + "\" (" + bytesRead + " bytes, " + duration + " ms).");
            
            // Print hex digits of response
            String responseHex = "Hex= ";
            for(byte b : responseBytes)
            {
                responseHex += ("0x" + String.format("%02X", b) + " ");
            }
            System.out.println(responseHex);

            i++;
            
        }
        
        socket.close();
        System.out.println("\nTCP client socket closed.");

        // Print latency stats
        Arrays.sort(durations);
        long min = durations[0];
        long max = durations[4];
        long avg = (long) Arrays.stream(durations).sum() / 5;
        System.out.println("Min: " + min + " ms | Max: " + max + " ms | Avg: " + avg + " ms");

    }
}