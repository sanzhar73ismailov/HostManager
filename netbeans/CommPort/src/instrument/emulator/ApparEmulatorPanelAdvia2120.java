package instrument.emulator;

import instrument.ASCII;
import instrument.InstrumentException;
import instrument.advia2120.Advia2120Exception;
import instrument.advia2120.Advia2120Message;
import static instrument.emulator.ApparEmulatorPanel.QUERY_ACTION_COMMAND;
import static instrument.emulator.ApparEmulatorPanel.RESULT_ACTION_COMMAND;
import static instrument.emulator.ApparEmulatorPanel.SEND_MT_ACTION_COMMAND;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class ApparEmulatorPanelAdvia2120 extends ApparEmulatorPanel {

    public static String SEND_TOKEN = "Send Token";

    public ApparEmulatorPanelAdvia2120() {
        super.panelName = "Advia2120";
        buttons.add(new JButton(QUERY_ACTION_COMMAND));
        buttons.add(new JButton(RESULT_ACTION_COMMAND));
        buttons.add(new JButton(SEND_MT_ACTION_COMMAND));
        buttons.add(new JButton(SEND_TOKEN));
        addButtonsToPanel();
    }

    @Override
    public String querySend() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String resultSend() {
        String str = null;
        try {
            Advia2120Message message = new Advia2120Message("123456789").getTestResultMessage();
            byte[] bytes = message.getRawMessage();
            outputStream.write(bytes);
            str = "H<-M" + message.getMessageAsString() + "\n";
            System.out.println("str = " + str);
        } catch (Exception ex) {
            str = ex.getMessage() + "\n";
            Logger.getLogger(ApparEmulatorPanelCobas411.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    @Override
    public String toWorkWithArray(InputStream inputStream, byte firstByte, JTextArea logArea) throws InstrumentException, IOException {
        byte incomeValue = 0;
        int indexOfByteArray = 0;
        byte[] byteArray = null;
        if (firstByte == instrument.ASCII.STX) {
            byteArray = new byte[10000];
            indexOfByteArray = 0;
            byteArray[indexOfByteArray++] = firstByte;
            logArea.append("\r\n" + ASCII.getASCIICodeAsString(firstByte));
        } else {
            throw new Advia2120Exception("firstByte is not STX. Value: " + firstByte);
        }
        while (true) {
            incomeValue = (byte) inputStream.read();
            logArea.append(ASCII.getASCIICodeAsString(incomeValue));
            if (incomeValue != instrument.ASCII.ETX) {
                byteArray[indexOfByteArray++] = incomeValue;
            } else {
                byteArray[indexOfByteArray++] = incomeValue;
                byte[] neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                Advia2120Message mess = new Advia2120Message(neewByteArray);
                return mess.getMessageAsString();
            }
        }
    }
}
