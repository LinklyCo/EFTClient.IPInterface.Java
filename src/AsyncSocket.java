import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * This socket class uses sock channels, selectors and keys. It will run on a separate thread.
 */
public class AsyncSocket implements Runnable {

    Selector selector;
    private InetSocketAddress sockAddress;
    private SocketEventListener _listener;
    private MessageParser parser = new MessageParser();

    public AsyncSocket(String host, int port, SocketEventListener listener) {
        sockAddress = new InetSocketAddress(host, port);
        _listener = listener;
    }

    /**
     * Overridden from the Runnable interface
     */
    @Override
    public void run() {
        SocketChannel channel;
        try {
            selector = Selector.open();
            channel = SocketChannel.open();
            channel.configureBlocking(false);

            channel.register(selector, SelectionKey.OP_CONNECT);
            channel.connect(sockAddress);

            while (!Thread.interrupted()) {

                selector.select(1000);
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isConnectable()) {
                        connect(key);
                    }
                    if (key.isWritable()) {
                        write(key, null);
                    }
                    if (key.isReadable()) {
                        read(key);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
    }


    void close() {
        try {
            selector.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer readBuffer = ByteBuffer.allocate(2048);
        readBuffer.clear();
        int length;
        try {
            length = channel.read(readBuffer);
        } catch (IOException ex) {
            System.out.println("Reading problem, closing connection");
            key.cancel();
            channel.close();
            return;
        }
        if (length == -1) {
            System.out.println("Nothing was read from the client");
            channel.close();
            key.cancel();
            return;
        }
        readBuffer.flip();
        byte[] buff = new byte[2048];

        readBuffer.get(buff, 0, length);
        String s = new String(buff);
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
    }

    void write(SelectionKey key, EFTRequest request) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String message = parser.EFTRequestToString(request);
        channel.write(ByteBuffer.wrap(message.getBytes()));
        key.interestOps(SelectionKey.OP_READ);
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (!channel.isConnected()) {
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            } else
                channel.connect(sockAddress);
        }
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_WRITE);
    }
}
