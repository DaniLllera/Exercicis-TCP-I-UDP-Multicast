package TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteTCPLista extends Thread {
    String hostname;
    int port;
    boolean continueConnected;
    Llista llista = null;
    Llista recivedList = null;
    List<Integer> lista;
    ObjectInputStream in;
    ObjectOutputStream out;

    public ClienteTCPLista(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Como te llamas");
        String nombre = scanner.next();
        System.out.println("Que numeros quieres ordenar (7 numeros)");

        int  n1 = scanner.nextInt(),n2= scanner.nextInt(),n3 = scanner.nextInt(),n4= scanner.nextInt(),n5 = scanner.nextInt(),n6= scanner.nextInt(),n7= scanner.nextInt();

        Socket socket;
        lista = new ArrayList<>();
        lista.add(n1);
        lista.add(n2);
        lista.add(n3);
        lista.add(n4);
        lista.add(n5);
        lista.add(n6);
        lista.add(n7);
        llista = new Llista(nombre, lista);

        System.out.println("HOLA!!" +" " + nombre);
        System.out.println("Lista de numeros(Sin ordenar)");
        lista.forEach(System.out::println);
        System.out.println();
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(llista);

            recivedList = (Llista) in.readObject();

            getRequest(recivedList);

            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getRequest(Llista serverData) {

        System.out.println(serverData.getNom() +" "+ "Aqui tienes tu lista sin repetidos y ordenada:");
        serverData.getNumberList().forEach(System.out::println);
    }

    private void close(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCPLista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        ClienteTCPLista clientTcp = new ClienteTCPLista("localhost", 5558);
        clientTcp.start();
    }
}
