package socket;

import java.io.*;
import java.net.*;
import java.util.Date;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        System.out.println("Client running");
//        if (args.length != 2) {
//            System.err.println(
//                "Usage: java EchoClient <host name> <port number>");
//            System.exit(1);
//        }

//        String hostName = args[0];
//        int portNumber = Integer.parseInt(args[1]);
//        String hostName = "127.0.0.1";
        String hostName = "localhost";
        //String hostName = "192.168.127.254";
        //int portNumber = Integer.parseInt(args[1]);
//        int portNumber = 64001;
        //int portNumber = 4002;
        int portNumber = 950;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
//            while ((userInput = stdIn.readLine()) != null) {
//                out.println(userInput);
//                System.out.println("echo: " + in.readLine());
//            }
//            
            while (true) {
                Thread.sleep(1);
                //out.println(userInput);
                System.out.println("from server: " + in.readLine());
//                userInput = stdIn.readLine();
                out.println(new Date());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
