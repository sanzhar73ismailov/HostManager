/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.instrument.astm;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanzhar.ismailov
 */
public abstract class MessageAstm {

    protected List<Record> records;
    protected TypeMessage typeMessage;
    protected RecordProducer recordProducer;

    public MessageAstm() {
    }

    public MessageAstm(List<Record> records) {
        this.records = records;
        try {
            defineTypeMessage();
        } catch (InstrumentException ex) {
            Logger.getLogger(MessageAstm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(TypeMessage typeMessage) {
        this.typeMessage = typeMessage;
    }

    public void defineTypeMessage() throws InstrumentException {
        for (Record record : records) {
            if (record.getTypeRecord() == TypeRecord.QUERY) {
                typeMessage = TypeMessage.QUERY_FROM_INSTRUMENT;
                break;
            } else if (record.getTypeRecord() == TypeRecord.RESULT) {
                typeMessage = TypeMessage.RESULT_FROM_INSTRUMENT;
                break;
            }
        }

    }

    public abstract String getSidFromQueryRecord();

    public abstract List<Frame> getFrames();

}
