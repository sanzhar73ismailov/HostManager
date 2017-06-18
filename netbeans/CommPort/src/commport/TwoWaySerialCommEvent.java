package commport;

import static instrument.ASCII.*;
import cobas.Block;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 *
 */
public class TwoWaySerialCommEvent {

    SerialPort serialPort;
    OutputStream out;
//    public static byte SOH = 1;
//    public static byte STX = 2;
//    public static byte ETX = 3;
//    public static byte EOT = 4;
//    public static byte LF = 10;
//    public static byte CR = 13;
    public static byte[] CONTROL_SYMBOLS = {SOH, STX, ETX, EOT, LF, CR};

    public TwoWaySerialCommEvent() {
        super();
    }

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);

//                InputStream in = serialPort.getInputStream();
                InputStream in = serialPort.getInputStream();
                out = serialPort.getOutputStream();

                (new Thread(new SerialWriter(out))).start();

                serialPort.addEventListener(new SerialReader(in, out));
                serialPort.notifyOnDataAvailable(true);

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
     * Handles the input coming from the serial port. A new line character is
     * treated as the end of a block in this example.
     */
    public static class SerialReader implements SerialPortEventListener {

        private InputStream in;
        private OutputStream myOut;
        private byte[] buffer = new byte[1024];
        Block request = new Block(null, null, null);
        boolean sohReaded = false;
        boolean headerReaded = false;
        String blockCode = null;

        public SerialReader(InputStream in, OutputStream myOut) {
            this.in = in;
            this.myOut = myOut;
        }

        @Override
        public void serialEvent(SerialPortEvent arg0) {

            int data;

            try {
                int len = 0;
                while ((data = in.read()) > -1) {
                    if (data == '\n') {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                String str = new String(buffer, 0, len);
                System.out.print(str);

                if (str.startsWith(SOH_STRING)) {
                    sohReaded = true;
                    return;
                } else if (str.startsWith("09_COBAS_INTEGRA")) {
                    if (str.length() == 22) {
                        blockCode = str.substring(20);
                        if (blockCode.equals("00")) {
                            headerReaded = true;
                        } else {
                            System.out.println("unknown block code: " + blockCode);
                            return;
                        }
                    } else if (str.startsWith(STX_STRING)) {
                        
                    } else {
                        sohReaded = false;
                        System.out.println("something wrong comes: " + str);
                    }
                } else {
                    System.out.println("something wrong comes: " + str);
                    if (1 == 1) {
                        return;
                    }
                }
                if (1 == 1) {
                    System.out.println("poshel!!!");
                    String HEADER = "header\n";
                    String DATA1 = "data1\n";
                    String DATA2 = "data2\n";
                    String DATA3 = "data3\n";

                    String BLOCK_CHECK = "block\n";
                    BLOCK_CHECK += new String(new byte[]{EOT});
                    BLOCK_CHECK += "\n";

                    String ALL_STR = HEADER + DATA1 + DATA2 + DATA3 + BLOCK_CHECK;

                    /*
                     myOut.write(HEADER.getBytes());
                     myOut.write(DATA1.getBytes());
                     myOut.write(DATA2.getBytes());
                     myOut.write(DATA3.getBytes());
                     myOut.write(BLOCK_CHECK.getBytes());
                     */
                    myOut.write(ALL_STR.getBytes());

                    // myOut.flush();

                }
//                byte [] bArrays = {SOH, };
                //String otvet = "OTVET: " + new String(CONTROL_SYMBOLS) + "AAA";
//                String otvet = "OTVET1\r\n";
//                String otvet2 = "OTVET2\r\n";
//                myOut.write(otvet.getBytes());
//                myOut.write(otvet2.getBytes());
                //  myOut.flush();

                //   BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new ))
                //   this.out.write(c);

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static class SerialWriter implements Runnable {

        OutputStream out;

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                int c = 0;
                while ((c = System.in.read()) > -1) {
                    this.out.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) {
        try {
            (new TwoWaySerialCommEvent()).connect("COM1");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
