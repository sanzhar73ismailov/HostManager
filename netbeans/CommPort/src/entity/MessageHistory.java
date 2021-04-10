package entity;

import java.util.Date;

public class MessageHistory implements HostDictionary {

    private int id;
    private Message message;
    private String text;
    private Date insertDatetime;

    public MessageHistory() {
    }

    public MessageHistory(int id, Message message, String text, Date insertDatetime) {
        this.id = id;
        this.message = message;
        this.text = text;
        this.insertDatetime = insertDatetime;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(Date insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "MessageHistory{" + "id=" + id + ", message=" + message + ", text=" + text + ", insertDatetime=" + insertDatetime + '}';
    }
}
