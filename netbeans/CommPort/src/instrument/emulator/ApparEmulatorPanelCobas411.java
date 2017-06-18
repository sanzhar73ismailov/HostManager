package instrument.emulator;

import instrument.ASCII;
import instrument.InstrumentException;
import instrument.cobas411.Cobas411Exception;
import instrument.cobas411.FrameCobas411;
import instrument.cobas411.MessageCobas411;
import static instrument.emulator.ApparEmulatorPanel.QUERY_ACTION_COMMAND;
import static instrument.emulator.ApparEmulatorPanel.RESULT_ACTION_COMMAND;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class ApparEmulatorPanelCobas411 extends ApparEmulatorPanel {

    public ApparEmulatorPanelCobas411() {
        super.panelName = "Cobas411";
        buttons.add(new JButton(QUERY_ACTION_COMMAND));
        buttons.add(new JButton(RESULT_ACTION_COMMAND));
        addButtonsToPanel();
    }

    @Override
    public String querySend() throws Cobas411Exception {
        String str = null;
        try {
            FrameCobas411 fr = MessageCobas411.createQueryMessageFromInstrumentForTesting().getFramesFromMessage().get(0);
            byte[] bytes = fr.getRawBytes();
            if (outputStream != null) {
                outputStream.write(bytes);
                str = "H<-M" + fr.getFrameAsString() + "\n";
            } else {
                str = "outputStream is null, client is not running";
            }
        } catch (IOException ex) {
            str = ex.getMessage() + "\n";
            throw new Cobas411Exception(ex);
        }
        return str;

    }

    @Override
    public String resultSend() throws Cobas411Exception {
        String str = null;
        try {
            FrameCobas411 fr = MessageCobas411.createResultsMessageFromInstrumentForTesting().getFramesFromMessage().get(0);
            byte[] bytes = fr.getRawBytes();
            if (outputStream != null) {
                outputStream.write(bytes);
                str = "H<-M" + fr.getFrameAsString() + "\n";
            } else {
                str = "outputStream is null, client is not running";
            }
        } catch (IOException ex) {
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
            logArea.append("\r\n" + ASCII.getASCIICodeAsString(incomeValue));
        } else {
            throw new Cobas411Exception("firstByte is not STX. Value: " + firstByte);
        }
        while (true) {
            incomeValue = (byte) inputStream.read();
            logArea.append(ASCII.getASCIICodeAsString(incomeValue));
            if (incomeValue != instrument.ASCII.ETX) {
                byteArray[indexOfByteArray++] = incomeValue;
            } else {
                byteArray[indexOfByteArray++] = incomeValue;
                byteArray[indexOfByteArray++] = (byte) inputStream.read();
                byteArray[indexOfByteArray++] = (byte) inputStream.read();
                byteArray[indexOfByteArray++] = (byte) inputStream.read();
                byteArray[indexOfByteArray++] = (byte) inputStream.read();
                byte[] neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                FrameCobas411 frameCobas411 = new FrameCobas411(neewByteArray);
                return frameCobas411.getFrameAsString();
            }
        }
    }
}
