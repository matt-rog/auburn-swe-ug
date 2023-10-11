package Message.Response;


import Message.MessageEncoder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseEncoderBin implements MessageEncoder<Response> {

  private String encoding;  // Character encoding

  public ResponseEncoderBin() {
    encoding = "UTF-16";
  }

  public ResponseEncoderBin(String encoding) {
    this.encoding = encoding;
  }

  public byte[] encode(Response response) throws Exception {

    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(buf);
    byte[] encodedResponse = response.n.getBytes(StandardCharsets.UTF_16);
    if (encodedResponse.length > 255)
      throw new Exception("Response exceeds encoded length limit");
    out.writeByte(encodedResponse.length);
    out.write(encodedResponse);
    out.flush();
    return buf.toByteArray();
  }
}
