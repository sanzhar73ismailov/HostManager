package kz.biostat.lishostmanager.comport.instrument.emulator;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;

//import gnu.io.CommPort;
//import gnu.io.CommPortIdentifier;
//import gnu.io.SerialPort;
import kz.biostat.lishostmanager.comport.instrument.ASCII;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ApparatusEmulator implements ActionListener {

    public static final String COMPORT = "COM1";//COM1 - где сидит мокса на ноуте
    public static final int millSec = 1000; // c какой частотой посылать сообщения
    public static final boolean USE_MOXA_FALSE = false; // 
    public static final int PORT = 950; // 
    InputStream inputStream = null;
    OutputStream outputStream = null;
    JTextField textField = new JTextField(70);
    JTextArea textArea = new JTextArea(100, 70);
    List<JButton> listButtons = new ArrayList<>();
    // JButton buttonSend = new JButton("Send Text From Text Field");
    JButton buttonSendMt = new JButton("SendMT");
    JButton buttonACK = new JButton("ACK send");
    JButton buttonENQ = new JButton("ENQ send");
    JButton buttonNACK = new JButton("NACK send");
    JButton buttonSTX = new JButton("STX send");
    JButton buttonETX = new JButton("ETX send");
    JButton buttonEOT = new JButton("EOT send");
    JButton buttonSOH = new JButton("SOH send");
    JButton buttonCR = new JButton("CR send");
    JButton buttonLF = new JButton("LF send");
    JButton buttonCRLF = new JButton("CRLF send");
    //JButton buttonQuery = new JButton("Query send");
    // JButton buttonResult = new JButton("Result send");
    JPanel panelSendControlChars;
    JPanel westPanel;
    ApparEmulatorPanel apparEmulatorPanel;
    JTextArea logArea = new JTextArea();
    ServerSocket serverSocket;
//    CommPort commPort = null;

    public ApparatusEmulator(ApparEmulatorPanel apparEmulatorPanel) {
        this.apparEmulatorPanel = apparEmulatorPanel;
        buildGui();
    }

    private void buildGui() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Clear Action");
        JMenuItem menuItem = new JMenuItem("Clear message area");
        JMenuItem menuItem2 = new JMenuItem("Clear log area");
        menuItem.setActionCommand("Clear message area");
        menuItem.addActionListener(this);
        menuItem2.addActionListener(this);

        menuBar.add(menu);
        menu.add(menuItem);
        menu.add(menuItem2);
        frame.setJMenuBar(menuBar);

        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        textArea.setEditable(false);
        westPanel = new JPanel();
        panelSendControlChars = new JPanel();

        BoxLayout experimentLayout = new BoxLayout(panelSendControlChars, BoxLayout.Y_AXIS);
        panelSendControlChars.setLayout(experimentLayout);
        addButtonsToPanel();
        westPanel.add(apparEmulatorPanel.getPanel());
        apparEmulatorPanel.addActionListener(this);

        panel.setLayout(new BorderLayout());
        frame.add(panel);

        JPanel panelSouth = new JPanel(new GridLayout(2, 1));
        panelSouth.setSize(700, 300);
        // logArea.setSize(700,300);
        panelSouth.add(textField);
        //panelSouth.add(new JScrollPane(logArea));

        JPanel panelCenter = new JPanel(new GridLayout(2, 1));

        panelCenter.add(scrollPane);
        panelCenter.add(new JScrollPane(logArea));

        panel.add(panelSouth, BorderLayout.SOUTH);
        panel.add(panelCenter, BorderLayout.CENTER);
        panel.add(westPanel, BorderLayout.WEST);
        panel.add(panelSendControlChars, BorderLayout.EAST);

        for (JButton jButton : listButtons) {
            jButton.addActionListener(this);
            jButton.setMinimumSize(new Dimension(150, 30));
            jButton.setMaximumSize(new Dimension(150, 30));
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setVisible(true);
    }

    private void addButtonsToPanel() {
        panelSendControlChars.add(buttonACK);
        panelSendControlChars.add(buttonENQ);
        panelSendControlChars.add(buttonNACK);
        panelSendControlChars.add(buttonSTX);
        panelSendControlChars.add(buttonETX);
        panelSendControlChars.add(buttonEOT);
        panelSendControlChars.add(buttonSOH);
        panelSendControlChars.add(buttonCR);
        panelSendControlChars.add(buttonLF);
        panelSendControlChars.add(buttonCRLF);

        listButtons.add(buttonACK);
        listButtons.add(buttonENQ);
        listButtons.add(buttonNACK);
        listButtons.add(buttonSTX);
        listButtons.add(buttonETX);
        listButtons.add(buttonEOT);
        listButtons.add(buttonSOH);
        listButtons.add(buttonCR);
        listButtons.add(buttonLF);
        listButtons.add(buttonCRLF);
    }

    public void myConnect(String portName) {
//        System.out.println("Start Instrument Working");
//        CommPortIdentifier portIdentifier = null;
//        commPort = null;
//
//        SerialPort serialPort = null;
//        serverSocket = null;
//        try {
//            if (USE_MOXA_FALSE) {
//                portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
//                if (portIdentifier.isCurrentlyOwned()) {
//                    System.out.println("Error: Port is currently in use");
//                    return;
//                } else {
//                    commPort = portIdentifier.open(this.getClass().getName(), 2000);
//                    if (commPort instanceof SerialPort) {
//                        serialPort = (SerialPort) commPort;
//                        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
//                                SerialPort.STOPBITS_1,
//                                SerialPort.PARITY_NONE);
//                    } else {
//                        System.out.println("Error: Only serial ports are handled by this example.");
//                        return;
//                    }
//                }
//                inputStream = serialPort.getInputStream();
//                outputStream = serialPort.getOutputStream();
//                apparEmulatorPanel.setOutputStream(outputStream);
//            } else {
//                serverSocket = new ServerSocket(PORT);
//                System.out.println("Listen for HOST");
//                Socket socket = serverSocket.accept();
//                inputStream = socket.getInputStream();
//                outputStream = socket.getOutputStream();
//            }
//
//            byte incomeValue = 0;
//            while (true) {
//                if (1 == 1) {
//                    String str = null;
//                    incomeValue = (byte) inputStream.read();
//                    if (incomeValue == ASCII.STX) {
//                        str = apparEmulatorPanel.toWorkWithArray(inputStream, incomeValue, logArea);
//                        textArea.append("H->M " + str + "\r\n");
//                    } else {
//                        logArea.append(ASCII.getASCIICodeAsString(incomeValue));
//                    }
//                }
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(ApparatusEmulator.class.getName()).log(Level.SEVERE, null, ex);
//            logArea.append("------- " + new Date() + " \n");
//            logArea.append(ex.getMessage() + " \n");
//            closeObjects();
//            myConnect(COMPORT);
//        } finally {
//            closeObjects();
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String command = e.getActionCommand();
            String strToTextAreaAppend = "";
            if (outputStream == null) {
                textArea.append("client is not connected " + "\r\n");
                return;
            }
            switch (command) {
                case "SendMT":
                    outputStream.write(Byte.parseByte(textField.getText()));
                    strToTextAreaAppend += "H<-M" + textField.getText();
                    break;
                case "ACK send":
                    outputStream.write(ACK);
                    strToTextAreaAppend += "H<-M" + " ACK";
                    break;
                case "ENQ send":
                    outputStream.write(ENQ);
                    strToTextAreaAppend += "H<-M" + " ENQ";
                    break;
                case "NACK send":
                    outputStream.write(NACK);
                    strToTextAreaAppend += "H<-M" + " NACK";
                    break;
                case "STX send":
                    outputStream.write(STX);
                    strToTextAreaAppend += "H<-M" + " STX";
                    break;
                case "ETX send":
                    outputStream.write(ETX);
                    strToTextAreaAppend += "H<-M" + " ETX";
                    break;
                case "EOT send":
                    outputStream.write(EOT);
                    strToTextAreaAppend += "H<-M" + " EOT";
                    break;
                case "SOH send":
                    outputStream.write(SOH);
                    strToTextAreaAppend += "H<-M" + " SOH";
                    break;
                case "CR send":
                    outputStream.write(CR);
                    strToTextAreaAppend += "H<-M" + " CR";
                    break;
                case "LF send":
                    outputStream.write(LF);
                    strToTextAreaAppend += "H<-M" + " LF";
                    break;
                case "CRLF send":
                    outputStream.write(CR);
                    outputStream.write(LF);
                    strToTextAreaAppend += "H<-M" + " CRLF";
                    break;
                case "Send Text From Text Field":
                    outputStream.write(textField.getText().getBytes());
                    strToTextAreaAppend += "H<-M" + textField.getText();
                case "Query send":
                    try {
                        strToTextAreaAppend += apparEmulatorPanel.querySend();
                    } catch (InstrumentException ex) {
                        Logger.getLogger(ApparatusEmulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Result send":
                    try {
                        strToTextAreaAppend += apparEmulatorPanel.resultSend();
                    } catch (InstrumentException ex) {
                        Logger.getLogger(ApparatusEmulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Clear message area":
                    textArea.setText("");
                    break;
                case "Clear log area":
                    logArea.setText("");
                    break;
            }
            outputStream.flush();
            textArea.append(strToTextAreaAppend + "\r\n");

        } catch (IOException ex) {
            Logger.getLogger(ApparatusEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeObjects() {
//        try {
//            if (commPort != null) {
//                commPort.close();
//            }
//            if (inputStream != null) {
//                System.out.println("inputStream closing...");
//                inputStream.close();
//            }
//
//            if (outputStream != null) {
//                System.out.println("outputStream closing...");
//                outputStream.close();
//            }
//
//            if (serverSocket != null && !serverSocket.isClosed()) {
//                System.out.println("serverSocket closing...");
//                serverSocket.close();
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(ApparatusEmulator.class.getName()).log(Level.SEVERE, null, ex);
//            logArea.append("------- " + new Date() + " \n");
//            logArea.append(ex.getMessage() + " \n");
//        }
    }
}
