package kz.biostat.lishostmanager.comport.modelHost;

import kz.biostat.lishostmanager.comport.entity.HostDictionary;
import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.LogInstrument;
import kz.biostat.lishostmanager.comport.entity.Message;
import kz.biostat.lishostmanager.comport.entity.Parameter;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.Sender;
import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface Model {

    <T extends HostDictionary> T getObject(int id, T obj) throws ModelException;

    <T extends HostDictionary> int insertObject(T obj) throws ModelException;

    <T extends HostDictionary> int removeObject(int id, T obj) throws ModelException;

    <T extends HostDictionary> int removeObjects(Properties properties, T obj) throws ModelException;

    <T extends HostDictionary> int updateObject(T obj) throws ModelException;

    <T extends HostDictionary> List<T> getObjects(Properties properties, T obj) throws ModelException;

    void setWorkOrderAsServed(int id) throws ModelException;

    void setWorkOrderAsFree(int id) throws ModelException;

    void changeWorkOrderStatus(int id, int status) throws ModelException;

    Test getTestByTestCode(String testCode, int instrumentId) throws ModelException;

    WorkOrder getWorkOrderBySidAndInstrument(String sid, int instrumentId) throws ModelException;

    List<WorkOrder> getWorkOrdersByInstrumentWithFeeStatus(int instrumentId) throws ModelException;

    void addLog(int instrumentId, String direction, String message, boolean temp) throws ModelException;

    WorkOrder getWorkOrderByRackPositionInstrumentDateWithFreeStatus(String rack, int position, int instrumentId, Date date) throws ModelException;

    WorkOrder getWorkOrderBySidInstrumentWithFreeStatus(String sid, int instrumentId) throws ModelException;

    List<Test> getTestsByInstrument(int instrument, String orderByArg) throws ModelException;

    List<WorkOrder> getWorkOrdersByInstrument(int instrumentId) throws ModelException;

    List<LogInstrument> getLogsByInstrument(int instrumentId, String orderByArg, int limit) throws ModelException;

    List<LogInstrument> getLogsByInstrumentNoTemp(int instrumentId, String orderByArg, int limit) throws ModelException;

    int deleteWorkOrdersByInstruemnt(int instrumentId) throws ModelException;

    List<HostDictionary> codeTests(int instrumentId) throws ModelException;

    List<Instrument> getInstruments() throws ModelException;

    Map<String, String> getSettings() throws ModelException;

    Sender getSenderByName(String name) throws ModelException;

    Message getMessageBySenderAndIncomeNember(String senderName, String IncomeNumber) throws ModelException;

    int insertMessage(Message message, String textMessageXml) throws ModelException;

    void updatetMessageHistoryByMessage(Message message, String textMessageXml) throws ModelException;

    void checkExistParameters(String parameters) throws ModelException;

    String getTestsFromParameters(String parameters) throws ModelException;

    List<Test> getTestsListFromParameters(String parameters) throws ModelException;

    List<Test> getAllTestsListFromParameters(String parameters) throws ModelException;

    List<WorkOrder> getWorkorderListFromTests(List<Test> testList) throws ModelException;

    String mergeTests(String oldTests, String newTests) throws ModelException;

    Parameter getParameterWithDeafaultTest(int paramId) throws ModelException;

    boolean twoWorkOrdersEqualsByRackAndPosition(WorkOrder wo1, WorkOrder wo2);

    boolean isExistWorkOrderWithSuchSidAndInstrument(WorkOrder wo) throws ModelException;

    List<Result> getResultsByListWorkOrders(List<WorkOrder> listWorkOrders) throws ModelException;

    List<Result> getResultsByInstrumentAndSid(String instrument, String sid) throws ModelException;

    /**
     * Получение результатов по инструменту и дате
     */
    List<Result> getResultsByInstrumentAndDate(String instrument, Date date) throws ModelException;

    /**
     * Для результатов получаемых только по сиду (односторонних приборов)
     */
    List<Result> getResultsBySid(String sid) throws ModelException;

    Instrument getInstrumentFromResult(Result result) throws ModelException;

    void insertResults(List<Result> results) throws ModelException;

    int deleteTemLogsByInstrument(Instrument instrument) throws ModelException;

    int deleteTemLogsAll() throws ModelException;

    int getVersionOfLastResultByInstrumentAndSid(String instrument, String sid, String testCode) throws ModelException;

    List<Parameter> getParameters() throws ModelException;

}
