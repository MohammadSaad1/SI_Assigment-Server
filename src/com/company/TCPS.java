package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Dora Di
 *
 * 1. Create a server socket and bind it to a specific port number
 * 2. Listen for a connection from the client and accept it. This results in a client socket, created on the server, for the connection.
 * 3. Read data from the client via an InputStream obtained from the client socket
 * 4. Send data to the client via the client socket’s OutputStream.
 * 5. Close the connection with the client.
 *
 * The steps 3 and 4 can be repeated many times depending on the protocol agreed between the server and the client.
 */

public class TCPS
{
    public static final int PORT = 6666;
    public static ServerSocket serverSocket = null; // Server gets found
    public static Socket openSocket = null;         // Server communicates with the client

    public static Socket configureServer() throws UnknownHostException, IOException
    {
        // get server's own IP address
        String serverIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Server ip: " + serverIP);

        // create a socket at the predefined port
        serverSocket = new ServerSocket(PORT);

        // Start listening and accepting requests on the serverSocket port, blocked until a request arrives
        openSocket = serverSocket.accept();
        System.out.println("Server accepts requests at: " + openSocket);

        return openSocket;
    }

    public static void connectClient(Socket openSocket) throws IOException
    {


         int amount = 1000;
         int accountNumber = 2324;

        String request, response;

        // two I/O streams attached to the server socket:
        Scanner in;         // Scanner is the incoming stream (requests from a client)
        PrintWriter out;    // PrintWriter is the outcoming stream (the response of the server)
        in = new Scanner(openSocket.getInputStream());
        out = new PrintWriter(openSocket.getOutputStream(),true);
        // Parameter true ensures automatic flushing of the output buffer

        // Server keeps listening for request and reading data from the Client,
        // until the Client sends "stop" requests
        while(in.hasNextLine())
        {
            request = in.nextLine();


                System.out.println( request);
               if ( Integer.parseInt(request.split(" ")[0]) != accountNumber)
               {

                   out.println("Account number is wrong.");
                   openSocket.close();


                   break;
               }
               else
               {

                   if(amount + Integer.parseInt(request.split(" ")[1]) < 0)
                   {
                       out.println("Operation not possible, balance too low");

                   }

                   else {
                       amount = amount + Integer.parseInt(request.split(" ")[1]);

                       System.out.println(amount);
                       response = new StringBuilder(amount).reverse().toString();

                       out.println(amount);

                       System.out.println("Balance: " + amount);

                   }
               }


            }
        }


    public static void main(String[] args) throws IOException
    {
        try
        {
            openSocket = configureServer();
            connectClient(openSocket);
        }
        catch(Exception e)
        {
            System.out.println(" Connection fails: " + e);
        }
        finally
        {
            openSocket.close();
            System.out.println("Connection to client closed");

            serverSocket.close();
            System.out.println("Server port closed");
        }

    }
}