package kz.biostat.lishostmanager.comport.instrument.mindrayBC3000;

import kz.biostat.lishostmanager.comport.instrument.Message;

public class MessageMindrayBC3000 extends Message {

    private String text;
    private Parameter parameter;
    private String sid;

    public MessageMindrayBC3000(byte[] rawMessage) {
        super(rawMessage);
        text = new String(rawMessage, 1, rawMessage.length - 2); // все что между STX и EOF
        type = ((char) rawMessage[1]) + "";
        parameter = new Parameter(text);
        if (parameter.id != null && !parameter.id.isEmpty()) {
            try {
                int tempId = Integer.valueOf(parameter.id);
                if (tempId == 0) {
                    this.sid = getGeneratedSidFromDate();
                } else {
                    this.sid = tempId + "";
                }
//                this.sid = "m" + tempId;
            } catch (NumberFormatException ex) {
                this.sid = getGeneratedSidFromDate();
            }
        } else {
            this.sid = getGeneratedSidFromDate();
        }

    }

    public String getSid() {
        return sid;
    }

    private String getGeneratedSidFromDate() {
        return "m" + parameter.year + parameter.month + parameter.day
                + parameter.hour + parameter.minutes;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean validateCheckSum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int calculateCheckSum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int extractCheckSumFromMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSidFromQueryRecord() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
