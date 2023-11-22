require 'socket'

port = ARGV[0].to_i

# 
server = TCPServer.new port 
puts "Listening on port #{port}..."

client = server.accept    # Wait for a client to connect

5.times do
  
    n_bytes = client.read(2).reverse # Accept input
    
    # Error handling
    if n_bytes.length != 2 then
        n_utf8 = "***"
    else
        n_utf8 = n_bytes.unpack('s').first
    end

    puts "Received N=#{n_utf8} from #{client.peeraddr[2]} over #{client.peeraddr[1]}."
    
    # Convert to string and UTF-16 encode
    n_s_utf8 = n_utf8.to_s
    n_s_utf16 = n_s_utf8.encode('UTF-16')

    puts "Sending S=\"#{n_s_utf8}\" (#{n_s_utf16.bytes.length()} bytes).\n"
    
    # Print the hex digits
    print "Hex= "
    n_s_utf16.bytes.each do |byte|
        print "0x#{byte.to_s(16).rjust(2, '0')} "
    end
    print "\n\n"

    # Send data
    client.write(n_s_utf16)
end

client.close
server.close

puts "TCP server socket closed."