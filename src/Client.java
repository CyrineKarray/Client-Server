import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static int[][] produit;
    private static int N;

    public static void main(String[] args) {
        final Socket clientSocket;
        final ObjectInputStream in;
        final ObjectOutputStream out;
        final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier

        try {
          /*
            * 127.0.0.1 est l'adresse local de la machine
          */
            clientSocket = new Socket("127.0.0.1",5000);

            //flux pour envoyer
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            Thread recevoir = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        produit = (int[][]) in.readObject();
                        while (produit == null) {
                            produit = (int[][]) in.readObject();
                        }
                        displayProduct(produit);
                        System.out.println("Server disconnected");
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread envoyer = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        try {
                            System.out.println("Veuillez saisir N: ");
                            N = sc.nextInt();
                            int[][] A = new int[N][N];
                            int[][] B = new int[N][N];
                            for (int i = 0; i < N; i++) {
                                for (int j = 0; j < N; j++) {
                                    System.out.format("Veuillez taper : A[%d][%d] ", i, j);
                                    A[i][j] = sc.nextInt();
                                    System.out.format("Veuillez taper  : B[%d][%d] ", i, j);
                                    B[i][j] = sc.nextInt();
                                }
                            }

                            out.writeObject(A);
                            out.writeObject(B);
                            recevoir.start();
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            envoyer.start();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void displayProduct(int[][] product) {
        System.out.println("Product of two matrices is: ");
        for (int i = 0; i < product.length; i++) {
            for (int j = 0; j < product.length; j++) {
                System.out.format("| %d | ", product[i][j]);
            }
            System.out.println("\n");
        }
    }
}