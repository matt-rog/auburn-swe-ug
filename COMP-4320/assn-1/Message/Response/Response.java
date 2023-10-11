package Message.Response;

import Message.Message;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Response extends Message<String> {

    public Response(String n)  {
          super(n);
      }
    public Response(String n, InetAddress host, int port)  {
        super(n, host, port);
    }

    @Override
    public String[] valueToHex(){
        // Convert n to list of string hex values
        // n -> bytes
        byte[] n_bytes = n.getBytes(StandardCharsets.UTF_16);

        // bytes -> list of hex
        String[] output = new String[n_bytes.length];
        int i = 0;
        for (byte b : n_bytes) {
            output[i] = "0x" + String.format("%02X", b);
            i++;
        }
        return output;
    }
}
