package Multicast;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientVelocimetre {
     int mediadeloscinco;
     int c;
     MulticastSocket multisocket;
    boolean continueRunning = true;
    InetSocketAddress groupMulticast;
    NetworkInterface netIf;

    public static void main(String[] args) throws IOException {
        ClientVelocimetre clientVelocimetre = new ClientVelocimetre();
        clientVelocimetre.runClient();

    }

    public ClientVelocimetre() {
        try {
            multisocket = new MulticastSocket(5557);
            InetAddress multicastIP = InetAddress.getByName("224.0.11.11");
            groupMulticast = new InetSocketAddress(multicastIP, 5557);
            netIf = NetworkInterface.getByName("wlp0s20f3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        byte[] receivedData = new byte[1024];
        multisocket.joinGroup(groupMulticast, netIf);
        while (continueRunning) {
            DatagramPacket datagramPacket = new DatagramPacket(receivedData, 1024);
            multisocket.receive(datagramPacket);
            velocidad(datagramPacket.getData());
        }
        multisocket.leaveGroup(groupMulticast, netIf);
        multisocket.close();
    }

    private void velocidad(byte[] data) {
        System.out.println(ByteBuffer.wrap(data).getInt());
        mediadeloscinco += ByteBuffer.wrap(data).getInt();
        c++;
        if (c == 5) {
            System.out.println("Media de los 5 numeros: " + mediadeloscinco / 5);
            mediadeloscinco = 0;
            c = 0;
        }
    }
}