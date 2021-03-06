package instrument.astm;

import entity.WorkOrder;
import instrument.InstrumentException;
import java.util.List;
public interface RecordProducer {

    Record createHeaderRecord() throws InstrumentException;

    Record createPatientRecord(WorkOrder wo) throws InstrumentException;

    List<Record> createOrderRecords(WorkOrder wo) throws InstrumentException;

    Record createTerminatorRecord() throws InstrumentException;

    RecordResult getResultRecord(Record record) throws InstrumentException;

    String getSidFromOrderRecord(Record record) throws InstrumentException;
    
    List<Record> getRecordsFromFrames(List<Frame> listFrames) throws InstrumentException;
    
    MessageAstm createMessageFromWorkOrder(WorkOrder workOrder) throws InstrumentException;
}
