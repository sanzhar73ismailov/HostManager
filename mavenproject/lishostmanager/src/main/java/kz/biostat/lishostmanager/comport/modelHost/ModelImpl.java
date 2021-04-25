package kz.biostat.lishostmanager.comport.modelHost;

import kz.biostat.lishostmanager.comport.entity.*;
import kz.biostat.lishostmanager.comport.entity.*;
import kz.biostat.lishostmanager.comport.util.Util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelImpl implements Model {

    private Dao dao;

    @Override
    public List<Result> getResultsBySid(String sid) throws ModelException {
        try {
            Properties prop = new Properties();
            prop.setProperty("sid", "='" + sid + "'");
            List<Result> list = dao.getObjects(prop, new Result(), "order by id desc");
            return getLastVersionResults(list);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public Map<String, String> getSettings() throws ModelException {
        Map<String, String> settings = new HashMap<>();
        try {
            Properties prop = new Properties();
            SqlNative sqlNative = SqlNative.getInstance();
            SqlResult sqlResult = sqlNative.executeQuery("select * from settings order by name");
            String[][] dataTable = sqlResult.getDataTable();
            for (String[] row : dataTable) {
                settings.put(row[0], row[1]);
            }
        } catch (Exception ex) {
            throw new ModelException(ex);
        }
        return settings;
    }

    enum showTempLogs {

        SHOW_WITHOUT_TEMP, SHOW_ALL, SHOW_ONLY_TEMP;
    }

    public ModelImpl() throws ModelException {
        try {
            this.dao = new DaoMysqlImpl();
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public HostDictionary getObject(int id, HostDictionary obj) throws ModelException {
        try {
            return dao.getObject(id, obj);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public int insertObject(HostDictionary obj) throws ModelException {
        try {
            return dao.insertObject(obj);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public int removeObject(int id, HostDictionary obj) throws ModelException {
        try {
            return dao.removeObject(id, obj);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public int removeObjects(Properties properties, HostDictionary obj) throws ModelException {
        try {
            return dao.removeObjects(properties, obj);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public int updateObject(HostDictionary obj) throws ModelException {
        try {
            return dao.updateObject(obj);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public List getObjects(Properties properties, HostDictionary obj) throws ModelException {
        try {
            return dao.getObjects(properties, obj);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public void setWorkOrderAsServed(int id) throws ModelException {
        try {
            dao.updatePropertyOfObject(id, new WorkOrder(), " set status=1 ");
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public void setWorkOrderAsFree(int id) throws ModelException {
        try {
            dao.updatePropertyOfObject(id, new WorkOrder(), " set status=0 ");
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public void changeWorkOrderStatus(int id, int status) throws ModelException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Test getTestByTestCode(String testCode, int instrumentId) throws ModelException {
        try {
            Test test = null;
            Properties prop = new Properties();
            prop.setProperty("code", "='" + testCode + "'");
            prop.setProperty("instrument_id", "='" + instrumentId + "'");
            List<Test> list = dao.getObjects(prop, new Test());
            if (!list.isEmpty() && list.size() == 1) {
                test = list.get(0);
            } else {
                new ModelException(String.format("there more than one tests "
                        + " with code %s and instrumentId %s", testCode, instrumentId));
            }
            return test;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public WorkOrder getWorkOrderBySidAndInstrument(String sid, int instrumentId) throws ModelException {
        try {
            WorkOrder wo = null;
            Properties prop = new Properties();
            prop.setProperty("sid", "='" + sid + "'");
            prop.setProperty("instrument_id", "='" + instrumentId + "'");
            List<WorkOrder> list = dao.getObjects(prop, new WorkOrder());
            if (!list.isEmpty() && list.size() == 1) {
                wo = list.get(0);
            } else {
                new ModelException(String.format("theere more than one workorders "
                        + " with sid %s and instrumentId %s", sid, instrumentId));
            }
            return wo;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public List getWorkOrdersByInstrumentWithFeeStatus(int instrumentId) throws ModelException {
        List<WorkOrder> list = null;
        Properties prop = new Properties();
        prop.setProperty("instrument_id", "=" + instrumentId);
        prop.setProperty("status", "=0");
        try {
            list = dao.getObjects(prop, new WorkOrder());
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        return list;
    }

    @Override
    public void addLog(int instrumentId, String direction, String message, boolean temp) throws ModelException {
        LogInstrument obj = new LogInstrument(new Instrument(instrumentId, ""), direction, message, temp);
        try {
            dao.insertObject(obj);
        } catch (DaoException ex) {
            System.out.println("message = " + message);
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public WorkOrder getWorkOrderByRackPositionInstrumentDateWithFreeStatus(String rack, int position, int instrumentId, Date date) throws ModelException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WorkOrder getWorkOrderBySidInstrumentWithFreeStatus(String sid, int instrumentId) throws ModelException {
        List<WorkOrder> list = null;
        Properties prop = new Properties();
        prop.setProperty("sid", "=" + sid);
        prop.setProperty("instrument_id", "=" + instrumentId);
        prop.setProperty("status", "=0");
        try {
            list = dao.getObjects(prop, new WorkOrder());
            if (list.size() > 1) {
                throw new ModelException(String.format("there more than one "
                        + "Workorder with sid: %s, and instrumentId: %s", sid, instrumentId));
            }
            if (list.isEmpty()) {
                throw new ModelException(String.format("there no "
                        + "Workorder with sid: %s, and instrumentId: %s", sid, instrumentId));
            }
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        return list.get(0);
    }

    @Override
    public List getTestsByInstrument(int instrument, String orderByArg) throws ModelException {
        try {
            Properties prop = new Properties();
            prop.setProperty("instrument_id", "=" + instrument);
            List<Test> list = dao.getObjects(prop, new Test(), " order by test_order");
            return list;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }

    }

    @Override
    public List getWorkOrdersByInstrument(int instrumentId) throws ModelException {
        Properties prop = new Properties();
        prop.setProperty("instrument_id", "=" + instrumentId);
        List<WorkOrder> list = null;
        try {
            list = dao.getObjects(prop, new WorkOrder());
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        return list;
    }

    @Override
    public List getLogsByInstrument(int instrumentId, String orderByArg, int limit) throws ModelException {
        return getLogsByInstrumentTempShowOrNot(instrumentId, orderByArg, limit, showTempLogs.SHOW_ALL);
    }

    @Override
    public List getLogsByInstrumentNoTemp(int instrumentId, String orderByArg, int limit) throws ModelException {
        return getLogsByInstrumentTempShowOrNot(instrumentId, orderByArg, limit, showTempLogs.SHOW_WITHOUT_TEMP);
    }

    @Override
    public int getVersionOfLastResultByInstrumentAndSid(String instrument, String sid, String testCode) throws ModelException {
        int versionLast = 0;
        try {
            versionLast = dao.getMaxResultVersion(instrument, sid, testCode);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        return versionLast;
    }

    @Override
    public List<Parameter> getParameters() throws ModelException {
        Properties prop = new Properties();
        List<Parameter> list = getObjects(prop, new Parameter());
        return list;
    }

    public List getLogsByInstrumentTempShowOrNot(int instrumentId, String orderByArg, int limit, showTempLogs tempShow) throws ModelException {
        List<LogInstrument> list = null;
        Properties prop = new Properties();
        prop.setProperty("instrument_id", "=" + instrumentId);
        switch (tempShow) {
            case SHOW_ONLY_TEMP:
                prop.setProperty("temp", "=" + 1);
                break;
            case SHOW_WITHOUT_TEMP:
                prop.setProperty("temp", "=" + 0);
                break;
        }
        try {
            list = dao.getObjects(prop, new LogInstrument(), orderByArg, limit);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        return list;
    }

    @Override
    public int deleteWorkOrdersByInstruemnt(int instrumentId) throws ModelException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<HostDictionary> codeTests(int instrumentId) throws ModelException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Instrument> getInstruments() throws ModelException {
        return this.getObjects(new Properties(), new Instrument());
    }

    @Override
    public Sender getSenderByName(String name) throws ModelException {
        Properties prop = new Properties();
        prop.setProperty("name", "='" + name + "'");
        List<Sender> list = null;
        try {
            list = dao.getObjects(prop, new Sender());
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        if (list.isEmpty()) {
            return null;
        } else if (list.size() > 1) {
            throw new ModelException("there more than one Sender with name " + name);
        }
        return list.get(0);
    }

    @Override
    public Message getMessageBySenderAndIncomeNember(String senderName, String incomeNumber) throws ModelException {
        Message message = null;
        Sender sender = getSenderByName(senderName);
        if (sender == null) {
            throw new ModelException(String.format("Sender with name %s is not registered", senderName));
        }
        List<Message> list = null;
        Properties prop = new Properties();
        prop.setProperty("sender_id", "=" + sender.getId());
        prop.setProperty("income_number", "='" + incomeNumber + "'");
        try {
            list = dao.getObjects(prop, new Message());
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        if (list.size() == 1) {
            message = list.get(0);
            message.setSender(sender);
        } else if (list.size() > 1) {
            throw new ModelException(String.format("there more than one message with name %s and incomeNumber %s", senderName, incomeNumber));
        }
        return message;

    }

    @Override
    public int insertMessage(Message message, String textMessage) throws ModelException {
        try {
            int insertId = dao.insertObject(message);
            message.setId(insertId);
            MessageHistory messageHistory = new MessageHistory();
            messageHistory.setMessage(message);
            messageHistory.setText(textMessage);
            return insertId;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public void updatetMessageHistoryByMessage(Message message, String textMessageXml) throws ModelException {
        try {
            MessageHistory msh = new MessageHistory();
            msh.setMessage(message);
            msh.setText(textMessageXml);
            dao.insertObject(msh);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public void checkExistParameters(String parameters) throws ModelException {
        if (!Util.isAllDigits(parameters.split(","))) {
            throw new ModelException("all parameters must be integers: " + parameters);
        }
        int[] intArrayParamsFromArg = getIntArrayFromCSVString(parameters);
        int[] paraIdArrayFromDb = getIdsParameters();
        StringBuilder sb = new StringBuilder();
        for (int parId : intArrayParamsFromArg) {
            if (!isExistIntInArray(parId, paraIdArrayFromDb)) {
                sb.append(",").append(parId);
            }
        }
        if (sb.length() > 0) {
            throw new ModelException("no such parameters: " + sb.substring(1));
        }
    }

    private boolean isExistIntInArray(int x, int[] intArray) {
        for (int i : intArray) {
            if (i == x) {
                return true;
            }
        }
        return false;
    }

    public int[] getIdsParameters() throws ModelException {
        try {
            List<Parameter> list = dao.getObjects(new Properties(), new Parameter());
            int[] intArray = new int[list.size()];
            for (int i = 0; i < intArray.length; i++) {
                intArray[i] = list.get(i).getId();
            }
            return intArray;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    private int[] getIntArrayFromCSVString(String str) {
        return Util.getIntArrayFromCSVString(str);
    }

    @Override
    public String getTestsFromParameters(String parameters) throws ModelException {
        List<Test> list = getTestsListFromParameters(parameters);
        StringBuilder sb = new StringBuilder();
        for (Test test : list) {
            sb.append(",").append(test.getCode());
        }
        return sb.substring(1);
    }

    @Override
    public String mergeTests(String oldTests, String newTests) throws ModelException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Parameter getParameterWithDeafaultTest(int paramId) throws ModelException {
        Parameter par = null;
        try {
            Properties prop = new Properties();
            prop.setProperty("parameter_id", "=" + paramId);
            prop.setProperty("use_default", "=1");
            List<Test> list = dao.getObjects(prop, new Test());
            if (list.isEmpty()) {
                throw new ModelException(String.format("no test with parameter %s, and use_default 1", paramId));
            } else if (list.size() > 1) {
                throw new ModelException(String.format("more than one tests with parameter %s, and use_default 1", paramId));
            }
            Test testDefault = list.get(0);
            par = dao.getObject(paramId, new Parameter());
            par.setTestDefault(testDefault);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
        return par;
    }

//    @Override
//    public List<Test> getTestsListFromParameters(String parameters) throws ModelException {
//        try {
//            int[] intArray = getIntArrayFromCSVString(parameters);
//            Properties prop = new Properties();
//            prop.setProperty("parameter_id", " in (" + parameters + ")");
//            prop.setProperty("use_default", "=1");
//            List<Test> list = dao.getObjects(prop, new Test());
//            return list;
//        } catch (DaoException ex) {
//            throw new ModelException(ex);
//        }
//    }
//    private String getCSVTestIds(List<Parameter> listParameters) {
//        StringBuilder stb = new StringBuilder();
//        for (int i = 0; i < listParameters.size(); i++) {
//            Parameter param = listParameters.get(i);
//            stb.append(param.getTestDefault().getId())
//        }
//        return null;
//    }
    @Override
    public List<Test> getTestsListFromParameters(String parameters) throws ModelException {
        try {
            //int[] intArray = getIntArrayFromCSVString(parameters);
            Properties prop = new Properties();
            prop.setProperty("id", " in (" + parameters + ")");
            // prop.setProperty("use_default", "=1");
            List<Parameter> listParameters = dao.getObjects(prop, new Parameter());
            List<Test> list = new ArrayList<>();
            for (Parameter param : listParameters) {
                list.add(param.getTestDefault());
            }
            return list;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public List<Test> getAllTestsListFromParameters(String parameters) throws ModelException {
        try {
            //int[] intArray = getIntArrayFromCSVString(parameters);
            Properties prop = new Properties();
            prop.setProperty("parameter_id", " in (" + parameters + ")");
            // prop.setProperty("use_default", "=1");
            List<Test> listTests = dao.getObjects(prop, new Test());
            return listTests;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public List<WorkOrder> getWorkorderListFromTests(List<Test> testList) throws ModelException {
        Set<Integer> setInstr = new HashSet<>();
        for (Test test : testList) {
            setInstr.add(test.getInstrument().getId());
        }
        Iterator<Integer> iter = setInstr.iterator();
        List<WorkOrder> listWorder = new ArrayList<>();
        while (iter.hasNext()) {
            int instrId = iter.next();
            WorkOrder wo = new WorkOrder();
            wo.setInstrument(new Instrument(instrId, null));
            StringBuilder sb = new StringBuilder();
            for (Test test : testList) {
                if (test.getInstrument().getId() == instrId) {
                    sb.append(",").append(test.getCode());
                }
            }
            wo.setTests(sb.substring(1));
            listWorder.add(wo);
        }
//        System.out.println("setInstr = " + setInstr);
//        for (int i = 0; i < listWorder.size(); i++) {
//            WorkOrder workOrder = listWorder.get(i);
//            System.out.println( "\n" + i+ " workOrder = " + workOrder);
//        }
        return listWorder;

    }

    @Override
    public boolean twoWorkOrdersEqualsByRackAndPosition(WorkOrder wo1, WorkOrder wo2) {
        if (wo1 != null && wo2 != null) {
            if (wo1.getSid() != null && wo2.getSid() != null) {
                if (wo1.getSid().equals(wo2.getSid())) {
                    return wo1.getPosition() == wo2.getPosition();
                }
            }
        }
        return false;
    }

    @Override
    public boolean isExistWorkOrderWithSuchSidAndInstrument(WorkOrder wo) throws ModelException {
        return getWorkOrderBySidAndInstrument(wo.getSid(), wo.getInstrument().getId()) != null;
    }

    @Override
    public List<Result> getResultsByListWorkOrders(List<WorkOrder> listWorkOrders) throws ModelException {
        try {
            Properties prop = new Properties();
            StringBuilder sb = new StringBuilder();
            for (WorkOrder wo : listWorkOrders) {
                sb.append(",").append(wo.getId());
            }
            prop.setProperty("workorder_id", " in (" + sb.substring(1) + ")");
            List<Result> list = dao.getObjects(prop, new Result(), "order by id desc");
            return getLastVersionResults(list);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public List<Result> getResultsByInstrumentAndSid(String instrument, String sid) throws ModelException {
        try {
            // int versionLast = dao.getMaxResultVersion(instrument, sid);
            Properties prop = new Properties();
            prop.setProperty("kz/biostat/lishostmanager/comport/instrument", "='" + instrument + "'");
            prop.setProperty("sid", "='" + sid + "'");
            //  prop.setProperty("version", "='" + versionLast + "'");
            List<Result> list = dao.getObjects(prop, new Result(), "order by id desc");
            return getLastVersionResults(list);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

        public List<Result> getResultsByInstrumentAndDate(String instrument, Date date) throws ModelException {
        try {
            // int versionLast = dao.getMaxResultVersion(instrument, sid);
            Properties prop = new Properties();
            prop.setProperty("kz/biostat/lishostmanager/comport/instrument", "='" + instrument + "'");
            //prop.setProperty("sid", "='" + sid + "'");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateParam = sdf.format(date);
            prop.setProperty("DATE_FORMAT(result.insert_datetime, '%Y-%m-%d')", "='" + dateParam  + "'");
            //  prop.setProperty("version", "='" + versionLast + "'");
            List<Result> list = dao.getObjects(prop, new Result(), "order by id asc");
            return list;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }
    
    
    private List<Result> getLastVersionResults(List<Result> list) {
        List<Result> listToReturn = new ArrayList<>();
        for (Result result : list) {
            if (!existResultInList(listToReturn, result)) {
                listToReturn.add(result);
            }
        }
        return listToReturn;
    }

    private boolean existResultInList(List<Result> list, Result result) {
        for (Result resElem1 : list) {
            if (result.getTestCode().equals(resElem1.getTestCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Instrument getInstrumentFromResult(Result result) throws ModelException {
        try {
            WorkOrder wo = dao.getObject(result.getWorkOrderId(), new WorkOrder());
            Instrument instr = dao.getObject(wo.getInstrument().getId(), new Instrument());
            return instr;
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public void insertResults(List<Result> results) throws ModelException {
        try {
            int versionLast = dao.getMaxResultVersion(results.get(0).getInstrument(), results.get(0).getSid());

            for (Result result : results) {
                result.setVersion(versionLast + 1);
                insertObject(result);
            }
        } catch (DaoException ex) {
            Logger.getLogger(ModelImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int deleteTemLogsByInstrument(Instrument instrument) throws ModelException {
        try {
            Properties prop = new Properties();
            prop.setProperty("instrument_id", "=" + instrument.getId());
            prop.setProperty("temp", "=1");
            return dao.delete("logs", prop);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public int deleteTemLogsAll() throws ModelException {
        try {
            Properties prop = new Properties();
            prop.setProperty("temp", "=1");
            return dao.delete("logs", prop);
        } catch (DaoException ex) {
            throw new ModelException(ex);
        }
    }
}
