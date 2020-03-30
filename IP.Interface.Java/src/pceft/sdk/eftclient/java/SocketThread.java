package pceft.sdk.eftclient.java;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean socketSend(EFTRequest eftRequest ) {
        try {
            String s = parser.EFTRequestToString(eftRequest);
            socket.getOutputStream().write( s.getBytes() );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String close() {
        try {
            socket.close();
            return "Socket closed successfully.";
        } catch (IOException e) {
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
                        //String[] messages = s.split("#");
                        ArrayList<String> messages = MessageSplit(s);
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
}