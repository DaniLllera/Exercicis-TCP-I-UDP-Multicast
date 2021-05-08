package TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServidorLista {
    int port;

    public ServidorLista(int port ) {
        this.port = port;
    }

    public void listen() {
        ServerSocket serverSocket;
        Socket clientSocket ;

        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                clientSocket = serverSocket.accept();
                ThreadSevidorLista threadSevidorLista = new ThreadSevidorLista(clientSocket);
                Thread client = new Thread(threadSevidorLista);
                client.start();
            }
        } catch ( IOException ex) {
            Logger.getLogger(ServidorLista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ServidorLista srv = new ServidorLista(5558);
        srv.listen();
    }
}
