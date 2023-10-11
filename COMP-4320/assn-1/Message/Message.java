package Message;

import java.net.InetAddress;

// Provides common structure and methods for requests and responses
public abstract class Message<T> {
    // Data transmitted on wire
    // T | Request: short, Response: string
    public T n;

    // Not transmitted on wire
    // Records host and port
    public InetAddress host;
    public int port;

    public Message(T n)  {
        this.n = n;
    }
    public Message(T n, InetAddress host, int port)  {
        this.n = n;
        this.host = host;
        this.port = port;
    }


    // Will need to process short and string differently
    abstract public String[] valueToHex();

    public String toString() {
        final String EOLN = java.lang.System.getProperty("line.separator");
        // Print value and hex equivalent
        String output = n.getClass().getName() + " = " + n
                + EOLN + "Hex = ";
        for(String hex : valueToHex()){
            output += hex + " ";
        }
        output += EOLN;

        if(host != null && port != 0){
            // This will only execute if the message is being received
            output += "Host Address = " + host.getHostAddress() + EOLN;
            output += "Port = " + port + EOLN;
        }
        return output;
    }
}
