import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicInteger;


public class AsyncSocketThread {
    AsynchronousSocketChannel sockChannel;
    private MessageParser parser = new MessageParser();

    public AsyncSocketThread(String host, int port, final AtomicInteger messageRead, SocketEventListener listener) throws IOException {
        sockChannel = AsynchronousSocketChannel.open();

        sockChannel.connect(new InetSocketAddress(host, port), sockChannel, new CompletionHandler<Void, AsynchronousSocketChannel>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel attachment) {
                startRead(attachment, messageRead, listener);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                System.out.println("Failed to connect to the client.");
            }
        });
    }

    private void startRead(final AsynchronousSocketChannel socketChannel, final AtomicInteger messageRead, SocketEventListener _listener) {
        final ByteBuffer buf = ByteBuffer.allocate(2048);
        try {
            socketChannel.read(buf, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                @Override
                public void completed(Integer result, AsynchronousSocketChannel attachment) {

                    messageRead.getAndIncrement();
                    String s = new String(buf.array());
                    if (s.charAt(0) != '#') {
                        throw new IllegalArgumentException("Incorrect message format");
                    }
                    if (s.length() < 5) {
                        throw new IllegalArgumentException("Message is too short");
                    }
                    // String may be multiple messages i.e "#0099...#0007L "
                    // ...
                    String[] messages = s.split("#");
                    for (String msg : messages
                            ) {
                        if (msg.length() > 0) {

                            _listener.socketReceive(msg);
                        }
                    }
                    buf.clear();
                    socketChannel.read(buf, socketChannel, this);
                }

                @Override
                public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                    System.out.println("Failed to read message from the client.");
                    exc.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Probably Read Pending Exception");
        }
    }

    void startWrite(final AsynchronousSocketChannel socketChannel, EFTRequest request, final AtomicInteger messageWritten) {
        String message = parser.EFTRequestToString(request);
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.put(message.getBytes());
        buf.flip();
        messageWritten.getAndIncrement();
        socketChannel.write(buf, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel attachment) {

            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                System.out.println("Failed to write to the client.");
                exc.printStackTrace();
            }
        });
    }
}
