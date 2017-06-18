package entity;

import java.util.Date;

public class LogInstrument implements HostDictionary {

    private int id;
    private Instrument instrument;
    private String direction;
    private String message;
    //private String messageForHTML;
    private boolean temp;
    private Date insertDate;
    private static final int SIZE_OF_BIG_MESSAGE = 2000;

    public LogInstrument() {
    }

    public LogInstrument(Instrument instrument, String direction, String message, boolean temp) {
        this.instrument = instrument;
        this.direction = direction;
        this.message = message;
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    @Override
    public String toString() {
        return "LogInstrument{" + "insertDate=" + insertDate + ", id=" + id
                + ", instrument=" + instrument + ", direction=" + direction
                + ", message=" + message + '}';
    }

    public String getMessageForHTML() {
        return getMessageForHTMLBySize(0);
    }

    public String getShortMessageForHTML() {
        return getMessageForHTMLBySize(SIZE_OF_BIG_MESSAGE);
    }

    /**
     *
     * @param sizeOfMessage - if 0 - full message
     * @return
     */
    public String getMessageForHTMLBySize(int sizeOfMessage) {
        //&LT;&GT;
        if (this.message == null) {
            return "";
        }
        String messageForHTML = this.message;

        if (sizeOfMessage > 0) {
            if (messageForHTML.length() > sizeOfMessage) {
                messageForHTML = messageForHTML.substring(0, sizeOfMessage);
            }
        }
        if (direction.equals("stack trace")) {
            messageForHTML = "<pre>" + messageForHTML + "</pre>";
        } else {
            messageForHTML = messageForHTML.replaceAll("<", "&LT");
            messageForHTML = messageForHTML.replaceAll(">", "&GT");
            messageForHTML = messageForHTML.replaceAll("&LTCR&GT", "&LTCR&GT<br/>");
            String[] controls = {"STX", "CR", "LF", "ETB", "ETX"};
            for (String contrl : controls) {
                messageForHTML = markByColor(messageForHTML, contrl);
            }
        }
        return messageForHTML;
    }

    public boolean isMessageTooBig() {
        return message.length() > SIZE_OF_BIG_MESSAGE;
    }

    public String getDirectionForHTML() {
        //&LT;&GT;
        String messageForHTML = this.direction;
        messageForHTML = messageForHTML.replaceAll("H<-M", "<span style='color: green;'>H<-M</span>");
        messageForHTML = messageForHTML.replaceAll("H->M", "<span style='color: red;'>H->M</span>");
        return messageForHTML;
    }

    private static String markByColor(String myMessage, String controlSymbols) {
        String replacedStr = String.format("&LT%s&GT", controlSymbols);
        String replacedStrBy = String.format("<span style='color: blue;'>&LT%s&GT</span>", controlSymbols);
        return myMessage.replaceAll(replacedStr, replacedStrBy);
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
