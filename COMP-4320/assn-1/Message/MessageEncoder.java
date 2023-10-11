package Message;

public interface MessageEncoder<T extends Message<?>> {
  byte[] encode(T message) throws Exception;
}
