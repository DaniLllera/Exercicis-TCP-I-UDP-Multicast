package TCP;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ThreadSevidorLista implements Runnable {
    Socket clientSocket;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    Llista salida, entrada;

    boolean acabat;

    public ThreadSevidorLista(Socket clientSocket) {
        this.clientSocket = clientSocket;
        acabat = false;
    }

    @Override
    public void run() {
        try {

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            while (!acabat) {
                entrada = (Llista) in.readObject();

                salida = generaResposta(entrada);

                out.writeObject(salida);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Llista generaResposta(Llista llista) {
        Set<Integer> set = new TreeSet<>(llista.getNumberList());

        List<Integer> arrayList = new ArrayList<>(set);

        Llista ret = new Llista(llista.getNom(), arrayList);
        acabat = false;

        return ret;
    }
}