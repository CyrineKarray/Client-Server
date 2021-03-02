import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur {

    private static int A[][], B[][], produit[][];

    public static void main(String[] test) throws IOException {

        final  ServerSocket socketserver;
        final Socket socket_service;
        final ObjectInputStream in;
        final ObjectOutputStream out;
        final Scanner sc = new Scanner(System.in); //lire les entrées  à partir du clavier.

        try {
            socketserver = new ServerSocket(5000);
            System.out.println("wating for client connexion...");

            socket_service = socketserver.accept();
            System.out.println("client connected...");

            in = new ObjectInputStream(socket_service.getInputStream());
            out = new ObjectOutputStream(socket_service.getOutputStream());

            Thread envoi= new Thread(new Runnable() {
                @Override
                public void run() {

                    produit = multiplyMatrix(A,B);
                    try {
                        out.writeObject(produit);
                        System.out.println("Result sent");
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            Thread recevoir= new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        A = (int[][]) in.readObject();
                        B = (int[][]) in.readObject();
                        envoi.start();
                        while (A != null) {
                            A = (int[][]) in.readObject();
                            B = (int[][]) in.readObject();

                        }
                        System.out.println("client déconnecté");
                        out.close();
                        socket_service.close();
                        socketserver.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });

            recevoir.start();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int[][] multiplyMatrix(int[][] A, int[][] B) {
        int[][] product = new int[A.length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                for (int k = 0; k < A.length; k++) {
                    product[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return product;
    }
}
