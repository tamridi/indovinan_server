package com.example;
import java.util.*;
import java.net.*;
import java.io.*;

public class ServerStr
{
    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String risposta = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    int randomNum;
    boolean win = false;
    int tentativi = 1;
    int difficolta = 100;

    public Socket attendi(int port) {
        try {
            System.out.println("SERVER partito in esecuzione ...");
            server = new ServerSocket(port);
            client = server.accept();
            System.out.println("connesso con il client");

            server.close();

            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoClient = new DataOutputStream(client.getOutputStream());

            //outVersoClient.writeBytes("inserisci la difficoltà" + '\n');
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server !");
            System.exit(1);
        }
        return client;
    }

    public void comunica() {
        try {
            stringaRicevuta = inDalClient.readLine();
            System.out.println("Ricevuta la stringa dal client : " + stringaRicevuta);
            try {
                difficolta = Integer.parseInt(stringaRicevuta);
            } catch (Exception e) {
                System.out.println("difficoltà default: 100");
                difficolta = 100;
            }
            
            randomNum = randomNumber(difficolta);
            System.out.println("numero generato: " + randomNum);
            outVersoClient.writeBytes("inserisci un numero" + '\n');

            while (!win) {
            //attendo stringa dal client
            stringaRicevuta = inDalClient.readLine();
            System.out.println("Ricevuta la stringa dal client : " + stringaRicevuta);

            //elaboro la stringa
            risposta = elaboraStringa(stringaRicevuta);

            if (risposta.equals("quit")) {
                System.out.println("Chiusura connessione");
                client.close();
                System.exit(1);
            }

            //rispondo al client
            System.out.println("Invio la risposta al client ...");
            outVersoClient.writeBytes(risposta+ '\n');
        } 

        client.close();
        }catch (SocketException e) {
            System.out.println("Connessione chiusa dal client!");
            System.exit(1);

        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("something went wrong!");
            System.exit(1);
        }
    }

    public String elaboraStringa(String stringaRicevuta) {
        if (stringaRicevuta.equals("quit")) {
            return "quit";
        } else {
            try {
                int numero = Integer.parseInt(stringaRicevuta);
                if (numero == randomNum) {
                    win = true;
                    return "Hai indovinato! con " + tentativi + " tentativi";
                } else if (numero > randomNum) {
                    tentativi++;
                    return "Troppo alto!";
                } else {
                    tentativi++;
                    return "Troppo basso!";
                }
            } catch (Exception e) {
                return "Non hai inserito un numero!";
            }
        }
    }


    public int randomNumber( int difficolta) {
        Random rand = new Random();
        return rand.nextInt(difficolta);
    }
}