package kz.biostat.lishostmanager.comport.modelHost.query;

import kz.biostat.lishostmanager.comport.entity.*;

public class QueryCreator {

    public static Query createQueryToGetOneInstance(Object object, int id) {
        Query queryObject = null;
        if (object instanceof WorkOrder) {
            queryObject = new WorkOrderQuery(id);
        } else if (object instanceof InstrumentModel) {
            queryObject = new InstrumentModelQuery(id);
        } else if (object instanceof Instrument) {
            queryObject = new InstrumentQuery(id);
        } else if (object instanceof LogInstrument) {
            queryObject = new LogInstrumentQuery(id);
        } else if (object instanceof Message) {
            queryObject = new MessageQuery(id);
        } else if (object instanceof MessageHistory) {
            queryObject = new MessageHistoryQuery(id);
        } else if (object instanceof Parameter) {
            queryObject = new ParameterQuery(id);
        } else if (object instanceof Result) {
            queryObject = new ResultQuery(id);
        } else if (object instanceof Sender) {
            queryObject = new SenderQuery(id);
        } else if (object instanceof Test) {
            queryObject = new TestQuery(id);
        } else if (object instanceof Analysis) {
            queryObject = new AnalysisQuery(id);
        } else {
            throw new UnsupportedOperationException("Unsupported operation for object: " + object);
        }
        return queryObject;
    }
}
