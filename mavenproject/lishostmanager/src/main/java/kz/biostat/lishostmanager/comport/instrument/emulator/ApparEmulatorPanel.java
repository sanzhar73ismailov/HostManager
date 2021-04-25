package kz.biostat.lishostmanager.comport.instrument.emulator;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public abstract class ApparEmulatorPanel {

    String panelName;
    OutputStream outputStream = null;
    JPanel panel = new JPanel();
    List<JButton> buttons = new ArrayList<>();
    public static final String QUERY_ACTION_COMMAND = "Query send";
    public static final String RESULT_ACTION_COMMAND = "Result send";
    public static final String SEND_MT_ACTION_COMMAND = "SendMT";
    JTextField tf;

    public void addActionListener(ActionListener actListener) {
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (JButton jButton : buttons) {
            jButton.addActionListener(actListener);
        }
    }

    public void addButtonsToPanel() {
        JTextField tf = new JTextField(this.panelName);
        tf.setEditable(false);
        this.panel.add(tf);
        for (JButton jButton : buttons) {
            this.panel.add(jButton);
        }
    }

    public abstract String querySend() throws InstrumentException;

    public abstract String resultSend() throws InstrumentException;

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public JPanel getPanel() {
        return panel;
    }

    public List<JButton> getButtons() {
        return buttons;
    }
    
    public abstract String toWorkWithArray(InputStream inputStream, byte firstByte, JTextArea logArea) throws InstrumentException, IOException;
}



