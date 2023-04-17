package pceft.sdk.eftclient.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * This socket class uses sock channels, selectors and keys. It will run on a separate thread.
 */
public class AsyncSocket extends Thread {

    public Selector selector;
    private final InetSocketAddress sockAddress;
    private final SocketEventListener _listener;
    private final MessageParser parser = new MessageParser();
    private IOException IoException = null;
    private ClosedSelectorException ClosedException = null;
    private Exception GeneralException = null;
    public SocketChannel channel;

    public AsyncSocket(String host, int port, SocketEventListener listener) {
        sockAddress = new InetSocketAddress(host, port);
        _listener = listener;
    }

    /**
     * Overridden from the Runnable interface
     */
    @Override
    public void run() {
        try {
            selector = Selector.open();
            channel = SocketChannel.open(sockAddress);
            channel.configureBlocking(false);

            channel.register(selector, SelectionKey.OP_CONNECT);
            if (!channel.isConnected()) {
                channel.connect(sockAddress);
            }
            while (!isInterrupted()) {

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
        } catch (ClosedSelectorException ex) {
            ClosedException = ex;
        } catch (IOException ioex) {
            IoException = ioex;
            ioex.printStackTrace();
        } catch (Exception e) {
            GeneralException = e;
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }

    public boolean isConnected() {
        if (isInterrupted() || selector == null)
            return false;

        return selector.isOpen();
    }

    public void close() {
        interrupt();
        ClosedException = new ClosedSelectorException();
    }

    private void closeSocket() {
        try {
            channel.close();
            selector.close();
        } catch (IOException ex) {
            IoException = ex;
        } catch (ClosedSelectorException exc) {
            ClosedException = exc;
        } catch (Exception e) {
            GeneralException = e;
        }
    }

    public Exception getException() {
        if (IoException != null)
            return IoException;
        if (ClosedException != null)
            return ClosedException;
        if (GeneralException != null)
            return GeneralException;

        return null;
    }

    private void read(SelectionKey key) throws IOException, ClosedSelectorException {
        if (isInterrupted())
            throw new ClosedSelectorException();

        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer readBuffer = ByteBuffer.allocate(2048);
        readBuffer.clear();
        int length;
        try {
            length = channel.read(readBuffer);
        } catch (IOException ex) {
            key.cancel();
            throw ex;
        }
        if (length == -1) {
            key.cancel();
            return;
        }
        readBuffer.flip();
        byte[] buff = new byte[2048];

        readBuffer.get(buff, 0, length);
        String s = new String(buff);
        if (s.charAt(0) != '#') {
            // Not PCEFTPOS expected format, return and keep listening
            return;
        }
        if (s.length() < 5) {
            // Incomplete message, or incorrect format. Return and keep listening
            return;
        }
        // String may be multiple messages i.e "#0099...#0007L "
        // ...
        ArrayList<String> messages = MessageSplit(s);
        for (String msg : messages) {
            if (msg.length() > 0) {
                _listener.socketReceive(msg);
            }
        }
    }

    private ArrayList<String> MessageSplit(String s) {
        ArrayList<String> msgs = new ArrayList<>();
        while (s != null && s.length() > 0) {
            if (s.charAt(0) != '#') {
                // Drop msg
                s = null;
            } else if (s.length() < 5) {
                // Drop msg
                s = null;
            }
            if (s != null) {
                int lengthMsg = Integer.parseInt(s.substring(1, 5));
                msgs.add(s.substring(0, lengthMsg));
                if (lengthMsg >= s.length()) {
                    s = null;
                } else {
                    s = s.substring(lengthMsg);
                }
            }
        }

        return msgs;
    }

    void writeRaw(String rawRequestString) throws IOException, ClosedSelectorException {
        SelectionKey key = selector.keys().iterator().next();
        writeRaw(key, rawRequestString);
    }

    void writeRaw(SelectionKey key, String rawRequestString) throws IOException, ClosedSelectorException {
        if (isInterrupted())
            throw new ClosedSelectorException();

        SocketChannel channel = (SocketChannel) key.channel();

        channel.write(ByteBuffer.wrap(rawRequestString.getBytes()));
        key.interestOps(SelectionKey.OP_READ);
    }

    void write(EFTRequest request) throws IOException, ClosedSelectorException, NoSuchElementException {
        SelectionKey key = selector.keys().iterator().next();
        write(key, request);
    }

    private void write(SelectionKey key, EFTRequest request) throws IOException, ClosedSelectorException {
        writeRaw(key, parser.EFTRequestToString(request));
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (!channel.isConnected()) {
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            } else {
                channel.connect(sockAddress);
            }
        }
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_WRITE);
    }
}
