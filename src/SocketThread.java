import java.io.IOException;
import java.net.Socket;

public class SocketThread extends Thread {

    private Socket socket;
    private SocketEventListener listener;
    private MessageParser parser = new MessageParser();

    public SocketThread(String inetAddr, int inetPort, SocketEventListener listener) {
        // Set the listener
        this.listener = listener;

        // Create a new socket
        try {
            socket = new Socket( inetAddr, inetPort );
            socket.setKeepAlive(true);
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public boolean socketSend(EFTRequest eftRequest ) {
        try {
            String s = parser.EFTRequestToString(eftRequest);
            socket.getOutputStream().write( s.getBytes() );
            return true;
        } catch( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }

    public String close() {
        try {
            socket.close();
            return "Socket closed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void run()  {
        boolean Exit = false;

        while( !Exit ) {
            try  {

                int len = socket.getReceiveBufferSize();
                if( len != 0 ) {
                    byte[] bytes = new byte[len];
                    int bytesRead = socket.getInputStream().read(bytes);
                    if (bytesRead > 0) {

                        String s = new String(bytes);
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

                                listener.socketReceive(msg);
                            }

                        }
                    }
                }

            } catch( Exception e )  {
                e.printStackTrace();
                //Exit = true;
            }
        }
//        try {
//            socket.close();
//        }
//        catch( Exception e ) {
//            e.printStackTrace();
//        }
    }
}