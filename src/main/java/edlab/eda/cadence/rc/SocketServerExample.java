package edlab.eda.cadence.rc;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketServerExample {

  public static void main(String[] args) throws IOException {
    
    ServerSocket socket = new ServerSocket();
    socket.bind(null);
    
    System.out.println(socket.getInetAddress());
    System.out.println(socket.getLocalPort());
    
    socket.accept();
    
    System.out.println("fuubar");
    
    socket.close();
    

  }

}
