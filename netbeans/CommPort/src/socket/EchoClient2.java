package socket;

import instrument.ASCII;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class EchoClient2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(" start ");
//        Socket socket = new Socket("localhost", 950);
        Socket socket = new Socket("192.168.1.205", 950);
        InputStream inputStream = socket.getInputStream();
//        OutputStream outputStream = socket.getOutputStream();
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);
        //     out.println("hi");


        while (true) {
            // System.out.println("1");
            outputStream.write(ASCII.ENQ);
            System.out.println(new Date() + "inputStream.available()=" + inputStream.available());
//            System.out.println("<<<<");
//            System.out.println("inputStream.available() = " + inputStream.available());
            if (inputStream.available() > 0) {
                String str = ASCII.getASCIICodeAsString((byte) inputStream.read());
                System.out.println("2");
                // int val = inputStream.read();
//              System.out.println("val = " + val + "=" + (char) val);
//            System.out.println(">>>>>>");
                System.out.print(str);
                if (str.equals("<LF>")) {
                    System.out.println();
//                out.println("hi");
                }
            }
            Thread.sleep(10000);


        }


    }
}
