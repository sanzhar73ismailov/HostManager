package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import kz.biostat.lishostmanager.comport.entity.*;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class TestModel {

    public static void main(String[] args) throws ModelException, ParseException {
        //testGetSenderByName();
        //testGetMessageBySenderAndIncomeNumber();
        //testGetIdsParameters();
        //testGetTestsFromParameters();
        //testCheckExistParameters();
        //testGetParameterWithDeafaultTest();
//        testGetWorkorderListFromTests();
        //testGetAllTestsListFromParameters();
        //testGetWorkOrderBySidAndInstrument();
        //testIsExistWorkOrderWithSuchSidAndInstrument();
        //testGetTestByTestCode();
        //testGetResultsByListWorkOrders();
        //testGetInstrumentFromResult();
//        testAddLog();
        //testNumberGenerator();
        //createResults();
        //createDublicateResults();
         //testGetParameter();
        //getAllInstruments();
        //  testUpdateParameter();
       // testGetAllAnalyses();
        //testGetMessageById();
        //   testGetSettings();
        
        testGetResultsByInstrumentAndDate();

    }

    public static void testUpdateParameter() throws ModelException {
        Model model = new ModelImpl();
//        List<Parameter> params = model.getParameters();
        Parameter parameter = model.getObject(1, new Parameter());
        System.out.println("parameter = " + parameter);
        System.out.println("==================");
        parameter.setName("ТТГ123");
        model.updateObject(parameter);

        parameter = model.getObject(1, new Parameter());
        System.out.println("parameter = " + parameter);

        //for (Parameter param : params) {
        //   System.out.println("param = " + param);
        //}
    }

    public static void testGetParameter() throws ModelException {
        Model model = new ModelImpl();
        // Analysis a = model.getObject(1, new Analysis());
        //System.out.println("a = " + a);
        if (1 == 0) {
            return;
        }

        List<Parameter> params = model.getParameters();
        for (Parameter param : params) {
            System.out.println("param = " + param);
        }
    }
    


    public static void getAllInstruments() throws ModelException {
        Model model = new ModelImpl();
        List<Instrument> instruments = model.getInstruments();
        System.out.println("instruments = " + instruments);
    }

    public static void testNumberGenerator() throws ModelException {
        String str = "t" + System.currentTimeMillis() + "";
        System.out.println("t" + str);
        System.out.println("s = " + str.length());
    }

    public static void testGetSenderByName() throws ModelException {
        Model model = new ModelImpl();
        Sender s = model.getSenderByName("elsimed1");
        System.out.println("s = " + s);
    }

    public static void testGetMessageBySenderAndIncomeNumber() throws ModelException {
        Model model = new ModelImpl();
        Message s = model.getMessageBySenderAndIncomeNember("host_manager", "123123");
        System.out.println("s = " + s);
    }

    public static void testGetIdsParameters() throws ModelException {
        ModelImpl model = new ModelImpl();
        int[] arr = model.getIdsParameters();
        System.out.println("arr = " + Arrays.toString(arr));
    }

    public static void testGetTestsFromParameters() throws ModelException {
        ModelImpl model = new ModelImpl();
        String testsStr = model.getTestsFromParameters("1,2,3,4009,4028");
        System.out.println("testsStr = " + testsStr);
    }

    private static void testCheckExistParameters() throws ModelException {
        ModelImpl model = new ModelImpl();
        model.checkExistParameters("g1,2,3,213123,10909,4,23");
    }

    private static void testGetParameterWithDeafaultTest() throws ModelException {
        ModelImpl model = new ModelImpl();
        Parameter p = model.getParameterWithDeafaultTest(2);
        System.out.println("p = " + p);
    }

    private static void testGetAllTestsListFromParameters() throws ModelException {
        String params = "4002,4003,4006,4007";
        ModelImpl model = new ModelImpl();
        List<Test> listTest = model.getAllTestsListFromParameters(params);
        for (Object test : listTest) {
            System.out.println("-----");
            System.out.println("test = " + test);
        }

    }

    private static void testGetWorkorderListFromTests() throws ModelException {
        ModelImpl model = new ModelImpl();
        List<Test> listTests = new ArrayList<>();
        Test t1 = null;
        Test t2 = null;
        Test t3 = null;
        Test t4 = null;
        Test t5 = null;
        if (1 == 0) {
            t1 = new Test(1, new Instrument(7, null), "code1", null, null, null, 0);
            t2 = new Test(2, new Instrument(2, null), "code2", null, null, null, 0);
            t3 = new Test(3, new Instrument(2, null), "code3", null, null, null, 0);
            t4 = new Test(4, new Instrument(3, null), "code4", null, null, null, 0);
            t5 = new Test(5, new Instrument(4, null), "code5", null, null, null, 0);
        }
        t1 = (Test) model.getObject(34, new Test());
        t2 = (Test) model.getObject(35, new Test());
        t3 = (Test) model.getObject(65, new Test());
        t4 = (Test) model.getObject(66, new Test());
        t5 = (Test) model.getObject(191, new Test());
        listTests.add(t1);
        listTests.add(t2);
        listTests.add(t3);
        listTests.add(t4);
        listTests.add(t5);
        for (Object test : listTests) {
            System.out.println("-----");
            System.out.println("test = " + test);
        }
        System.out.println("!!!!!!!!!!!!!!!!!");
        List list = model.getWorkorderListFromTests(listTests);
        for (int i = 0; i < list.size(); i++) {
            Object wo = list.get(i);
            System.out.println(i + ", wo = " + wo);

        }
    }

    private static void testGetWorkOrderBySidAndInstrument() throws ModelException {
        ModelImpl model = new ModelImpl();
        //WorkOrder wo = model.getWorkOrderBySidAndInstrument("100", 1);
        // System.out.println("wo = " + wo);
        String s1 = null;
        String s2 = "";
        System.out.println(s1.equals(s2));

    }

    private static void testIsExistWorkOrderWithSuchSidAndInstrument() throws ModelException {
        ModelImpl model = new ModelImpl();
        WorkOrder wo = new WorkOrder();
        wo.setSid("100");
        wo.setInstrument(new Instrument(3, null));
        boolean b = model.isExistWorkOrderWithSuchSidAndInstrument(wo);
        System.out.println("b = " + b);

    }

    private static void testGetTestByTestCode() throws ModelException {
        ModelImpl model = new ModelImpl();
        Test test = model.getTestByTestCode("20", 2);
        System.out.println("test = " + test);
    }

    private static void testGetResultsByListWorkOrders() throws ModelException {
        ModelImpl model = new ModelImpl();
        WorkOrder w1 = new WorkOrder();
        w1.setId(161);;
        List<WorkOrder> listWo = new ArrayList<>();
        listWo.add(w1);
        List<Result> l = model.getResultsByListWorkOrders(listWo);
        for (Result result : l) {
            System.out.println("\nresult = " + result);
        }
    }

    private static void testGetInstrumentFromResult() throws ModelException {
        ModelImpl model = new ModelImpl();
        Result res = new Result();
        res.setId(7);
        //WorkOrder wo = new WorkOrder();
        //wo.setId(111);
        res.setWorkOrderId(111);
        Instrument inst = model.getInstrumentFromResult(res);
        System.out.println("inst = " + inst);
    }

    private static void testAddLog() throws ModelException {
        ModelImpl model = new ModelImpl();
        model.addLog(1, "dir2", "message1222", false);

    }

    private static void createResults() throws ModelException {
        ModelImpl model = new ModelImpl();

        String sid = "t232";
        int woId = 161;
        String instrName = "immulite2000";
        /*
         private int id;
         private int workOrderId;
         private String testCode;
         private int parameterId;
         private String value;
         private String units;
         private String referenseRanges;
         private String abnormalFlags;
         private String initialRerun;
         private String comment;
         private String status;
         private String rawText;
         private String addParams;
         private String instrument;
         private String sid;
         private int version;
         */
        List<Result> listRes = new ArrayList<>();

        Result res1 = new Result();
        res1.setWorkOrderId(woId);
        res1.setTestCode("ACT");
        res1.setParameterId(2032);
        res1.setValue("ACTVAL");
        res1.setInstrument(instrName);
        res1.setSid(sid);

        Result res2 = new Result();
        res2.setWorkOrderId(woId);
        res2.setTestCode("CCP");
        res2.setParameterId(2033);
        res2.setValue("CCPVAL");
        res2.setInstrument(instrName);
        res2.setSid(sid);

        Result res3 = new Result();
        res3.setWorkOrderId(woId);
        res3.setTestCode("DHS");
        res3.setParameterId(2037);
        res3.setValue("DHSVAL");
        res3.setInstrument(instrName);
        res3.setSid(sid);

        listRes.add(res1);
        listRes.add(res2);
        listRes.add(res3);

        model.insertResults(listRes);

    }

    private static void createDublicateResults() throws ModelException {
        ModelImpl model = new ModelImpl();

        String sid = "t232";
        int woId = 161;
        String instrName = "immulite2000";
        /*
         private int id;
         private int workOrderId;
         private String testCode;
         private int parameterId;
         private String value;
         private String units;
         private String referenseRanges;
         private String abnormalFlags;
         private String initialRerun;
         private String comment;
         private String status;
         private String rawText;
         private String addParams;
         private String instrument;
         private String sid;
         private int version;
         */
        List<Result> listRes = new ArrayList<>();

        Result res1 = new Result();
        res1.setWorkOrderId(woId);
        res1.setTestCode("ACT");
        res1.setParameterId(2032);
        res1.setValue("ACTVAL1");
        res1.setInstrument(instrName);
        res1.setSid(sid);

        Result res2 = new Result();
        res2.setWorkOrderId(woId);
        res2.setTestCode("CCP");
        res2.setParameterId(2033);
        res2.setValue("CCPVAL1");
        res2.setInstrument(instrName);
        res2.setSid(sid);

        Result res3 = new Result();
        res3.setWorkOrderId(woId);
        res3.setTestCode("DHS");
        res3.setParameterId(2037);
        res3.setValue("DHSVAL1");
        res3.setInstrument(instrName);
        res3.setSid(sid);

        listRes.add(res1);
        listRes.add(res2);
        listRes.add(res3);

        Result res22 = new Result();
        res22.setWorkOrderId(woId);
        res22.setTestCode("CCP");
        res22.setParameterId(2033);
        res22.setValue("CCPVAL2");
        res22.setInstrument(instrName);
        res22.setSid(sid);

        Result res33 = new Result();
        res33.setWorkOrderId(woId);
        res33.setTestCode("DHS");
        res33.setParameterId(2037);
        res33.setValue("DHSVAL2");
        res33.setInstrument(instrName);
        res33.setSid(sid);

        listRes.add(res22);
        listRes.add(res33);

        model.insertResults(listRes);

    }

    private static void testGetAllAnalyses() throws ModelException {
        ModelImpl model = new ModelImpl();
        List<Analysis> listAnalyses = model.getObjects(new Properties(), new Analysis());
        System.out.println("listAnalyses = " + listAnalyses);
    }

    private static void testGetResultsByInstrumentAndDate() throws ModelException, ParseException{
        ModelImpl model = new ModelImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2016-04-01");
        
        List<Result> listResults = model.getResultsByInstrumentAndDate("advia2120", date);
        for (int i = 0; i < listResults.size(); i++) {
            Result res = listResults.get(i);
            System.out.println(i + ") res = " + res);
            
        }
    }
    
    
    private static void testGetMessageById() throws ModelException {
        ModelImpl model = new ModelImpl();
        Message message = (Message) model.getObject(39, new Message());
        // WorkOrder message =  (WorkOrder) model.getObject(111, new WorkOrder());
        System.out.println("message = " + message);
    }
    
    private static void testGetSettings() throws ModelException {
        ModelImpl model = new ModelImpl();
        Map<String, String> mapSett =  model.getSettings();
        // WorkOrder message =  (WorkOrder) model.getObject(111, new WorkOrder());
        System.out.println(mapSett);
    }
}
