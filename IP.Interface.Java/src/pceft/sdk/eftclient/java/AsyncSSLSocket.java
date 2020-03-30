package pceft.sdk.eftclient.java;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * This socket class uses sock channels, selectors and keys. It will run on a separate thread.
 */

public class AsyncSSLSocket implements Runnable {

    public Selector selector;
    public SSLSocketListener sslListener = null;
    private InetSocketAddress sockAddress;
    public SocketEventListener _listener;
    private MessageParser parser = new MessageParser();
    private IOException IoException = null;
    private String IoExceptionMessage = "";
    private ClosedSelectorException ClosedException = null;
    private Exception GeneralException = null;
    private String ClosedExceptionMessage = "";
    public SocketChannel channel;
    public SSLSocket socket;
    private BufferedWriter bufferedWriter;
    private InputStream inputData;
    public AsyncSSLSocket(String host, int port, SocketEventListener listener) {
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
            //System.out.println("Thread Was Interupted");
        } catch (ClosedSelectorException ex) {
            ClosedException = ex;
            ClosedExceptionMessage = ex.getMessage();
            ex.printStackTrace();
        } catch (IOException ioex) {
            IoException = ioex;
            IoExceptionMessage = ioex.getMessage();
            ioex.printStackTrace();
        } catch (Exception e) {
            GeneralException = e;
            e.printStackTrace();
        }
        finally {
               close();
        }
    }

    public boolean isConnected() {
        if (ClosedException == null && IoException == null) {
            if (selector != null)
                return selector.isOpen();
            else
                return false;
        } else
            return false;
    }


    void close() {
        try {
            selector.close();
        } catch (IOException ex) {
            IoException = ex;
            IoExceptionMessage = ex.getMessage();
        } catch (ClosedSelectorException exc) {
            ClosedException = exc;
            ClosedExceptionMessage = exc.getMessage();
        } catch (Exception e) {
            GeneralException = e;
        }
        if (sslListener != null)
            sslListener.onDisconnect();

    }

    public Exception getException() {
        if (IoException != null)
            return IoException;
        if (ClosedException != null)
            return ClosedException;
        if (GeneralException != null)
            return GeneralException;
        else return null;
    }

    private void read(SelectionKey key) throws IOException {
        byte[] readBuffer = new byte[2048];
        int length;
        try {
            length = inputData.read(readBuffer);
            //if (length > 0) {
            //    System.out.println("RESPONSE LENGTH: " + length);
            //    System.out.println("RESPONSE: " + new String(readBuffer, 0, length));
            //}
        } catch (IOException ex) {
            key.cancel();
            throw ex;
        }
        if (length == -1) {
            key.cancel();
            return;
        }
        String s = new String(readBuffer, 0, length);
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
        for (String msg : messages
        ) {
            if (msg.length() > 0) {

                _listener.socketReceive(msg);
            }
        }
        //throw new IOException(); //<-Test
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

    void writeRaw(SelectionKey key, String rawRequestString) throws IOException{
        System.out.println("REQUEST OUT: " + rawRequestString);
        bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));

        inputData = socket.getInputStream();

        bufferedWriter.write(rawRequestString,0,rawRequestString.length());
        bufferedWriter.flush();
        key.interestOps(SelectionKey.OP_READ);
    }

    void initSocket() throws IOException {
        SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) f.createSocket(sockAddress.getHostName(), sockAddress.getPort());
        socket.setKeepAlive(true);
        if (!socket.isConnected()) {
            socket.connect(sockAddress);
        }
        socket.startHandshake();
    }


    void write(SelectionKey key, EFTRequest request) throws IOException {
        String message = parser.EFTRequestToString(request);
        //System.out.println("REQUEST OUT: " + message.toString());
        bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));

        inputData = socket.getInputStream();
        bufferedWriter.write(message,0,message.length());
        bufferedWriter.flush();
        key.interestOps(SelectionKey.OP_READ);
    }

    private void connect(SelectionKey key) throws IOException {
        SSLSocketFactory f =
                (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {
            SSLSocket c =
                    (SSLSocket) f.createSocket(sockAddress.getHostName(), sockAddress.getPort());
            c.setKeepAlive(true);
            socket = c;
            if (!c.isConnected()) {
                c.connect(sockAddress);
                boolean test = c.isConnected();
                System.out.println("Connected = " + test);
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }

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
