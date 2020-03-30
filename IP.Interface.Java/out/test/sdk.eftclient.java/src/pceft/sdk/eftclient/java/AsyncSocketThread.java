package pceft.sdk.eftclient.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class AsyncSocketThread {
    AsynchronousSocketChannel sockChannel;
    AsynchronousServerSocketChannel sockChannelSSL;
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
                System.out.println(String.format("Socket connect failed with: %s", exc.getMessage()));
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
                        //Not PCEFTPOS expected format, keep listening
                        return;
                    }
                    if (s.length() < 5) {
                        //Incomplete message, or not PCEFTPOS expected format, keep listening
                        return;
                    }
                    // String may be multiple messages i.e "#0099...#0007L "
                    // ...
                    //String[] messages = s.split("#");
                    ArrayList<String> messages = MessageSplit(s);
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
                    System.out.println(String.format("Failed to read message from the client with following message: %s", exc.getMessage()));
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    ArrayList<String> MessageSplit(String s){
        ArrayList<String> msgs = new ArrayList<String>();
        while (s != null && s.length() > 0)
        {
            if (s.charAt(0) != '#') {
                // Drop msg
                s = null;
            }
            else if (s.length() < 5) {
                // Drop msg
                s = null;
            }
            if (s != null)
            {
                int lengthMsg = Integer.parseInt(s.substring(1,5));
                msgs.add(s.substring(0,lengthMsg));
                if (lengthMsg >= s.length())
                {
                    s = null;
                }
                else{
                    s = s.substring(lengthMsg);
                }
            }
        }

        return msgs;
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
                System.out.println(String.format("Failed to write to the client with the following reason: %s", exc.getMessage()));
            }
        });
    }
}
