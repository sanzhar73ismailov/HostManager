package entity;

import java.util.Date;

public class Message  implements HostDictionary {

    private int id;
    private Sender sender;
    private String incomeNumber;
    private boolean close;
    private Date insertDatetime;

    public Message() {
    }

    public Message(int id, Sender sender, String incomeNumber, boolean close, Date insertDatetime) {
        this.id = id;
        this.sender = sender;
        this.incomeNumber = incomeNumber;
        this.close = close;
        this.insertDatetime = insertDatetime;
    }

    public Date getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(Date insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public String getIncomeNumber() {
        return incomeNumber;
    }

    public void setIncomeNumber(String incomeNumber) {
        this.incomeNumber = incomeNumber;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", sender=" + sender + ", incomeNumber=" + incomeNumber + ", close=" + close + ", insertDatetime=" + insertDatetime + '}';
    }
    
}
