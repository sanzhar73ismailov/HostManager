package socket;

import instrument.ASCII;

import java.net.*;
import java.io.*;
import java.util.Date;

public class EchoServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Server running");

//        if (args.length != 1) {
//            System.err.println("Usage: java EchoServer <port number>");
//            System.exit(1);
//        }

        //int portNumber = Integer.parseInt(args[0]);
        //int portNumber = 64001;
        int portNumber = 950;
        System.out.println("before accept");
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);

                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine;
            System.out.println("after accept");
//            while ((inputLine = in.readLine()) != null) {
//                out.println("from server: "+inputLine);
//            }
            String message;
            while (true) {
                inputLine = in.readLine();
                if(inputLine.startsWith("hi")) {
                    String strStart = new String(new byte[]{ASCII.STX, 1});
                    String strEnd = new String(new byte[]{ASCII.CR, ASCII.LF});
                    message = strStart + "from server: " + new Date();
                    out.println(message);
                    System.out.println("message = " + message);
                    Thread.sleep(2000);
                }
                System.out.println("true ");

            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
