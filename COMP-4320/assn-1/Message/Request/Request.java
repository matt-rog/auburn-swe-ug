package Message.Request;

import Message.*;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Request extends Message<Short> {

    public Request(short n)  {
          super(n);
      }
    public Request(short n, InetAddress host, int port)  {
        super(n, host, port);
    }

    @Override
    public String[] valueToHex(){
        // Convert n to list of string hex values
        // n -> bytes
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(this.n);
        byte[] n_bytes = buffer.array();

        // bytes -> list of hex
        String[] output = new String[2];
        int i = 0;
        for (byte b : n_bytes) {
            output[i] = "0x" + String.format("%02X", b);
            i++;
        }
        return output;
    }
}
