package kz.biostat.lishostmanager.web.lab.webservice;

import kz.biostat.lishostmanager.comport.modelHost.DaoException;
import kz.biostat.lishostmanager.comport.entity.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;
import kz.biostat.lishostmanager.comport.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static kz.biostat.lishostmanager.comport.xml.ReadXMLFile.getLisMessageXML;

public class XmlWork {

    private String strXml;
    private String responseMessage = "";
    private Document documentXml;

    public XmlWork(String strXml) {
        this.strXml = strXml;
        createDocument();
    }

    public List<WorkOrder> parseStringMessage() throws DaoException {
        List<WorkOrder> listWorOrders = null;
        try {
            Element rootElement = this.documentXml.getDocumentElement();
            rootElement.normalize();
            //System.out.println("Root element :" + rootElement.getNodeName());
            String parameters = (getValueOfElement(rootElement, "parameters"));
            String incomeNumber = rootElement.getAttribute("id");
            String senderName = rootElement.getAttribute("sender");
            Model model = new ModelImpl();
            model.checkExistParameters(parameters);
            Message message = model.getMessageBySenderAndIncomeNember(incomeNumber, senderName);

            listWorOrders = separateParamsTOWorkOrders(parameters);
            for (WorkOrder workOrder : listWorOrders) {
                workOrder.setMid(Integer.parseInt(rootElement.getAttribute("id")));
                //workOrder.setInstrument(getValueOfElement(rootElement, "instrumentId"));
                workOrder.setSid(getValueOfElement(rootElement, "sid"));
                workOrder.setRack(getValueOfElement(rootElement, "rack"));
                workOrder.setPosition(Integer.parseInt(getValueOfElement(rootElement, "position")));
                workOrder.setSampleType(getValueOfElement(rootElement, "sampleType"));
                workOrder.setPatientName(getValueOfElement(rootElement, "patientName"));
                workOrder.setPatientNumber(getValueOfElement(rootElement, "patientNumber"));
                workOrder.setDateBirth(parseDate(getValueOfElement(rootElement, "dateBirth")));
                workOrder.setSex(parseSex(getValueOfElement(rootElement, "sex")));
                workOrder.setDateCollection(parseDate(getValueOfElement(rootElement, "dateCollection")));
                workOrder.setLaborantName(getValueOfElement(rootElement, "laborantName"));
                // workOrder.setTests(getTests(rootElement));
                workOrder.setAddParamsFromString(getAddParams(rootElement));
            }
            //System.out.println("addPar:\n" + getAddParams(rootElement));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
        return listWorOrders;
    }

    public WorkOrder parseStringMessageTemp() throws DaoException {
        WorkOrder workOrder = null;
        try {
            Element rootElement = this.documentXml.getDocumentElement();
            rootElement.normalize();
            //System.out.println("Root element :" + rootElement.getNodeName());
            String tests = (getValueOfElement(rootElement, "parameters"));
            workOrder = new WorkOrder();
            // for (WorkOrder workOrder : listWorOrders) {
            workOrder.setMid(Integer.parseInt(rootElement.getAttribute("id")));
            //workOrder.setMid(Integer.parseInt(rootElement.getAttribute("sender")));
            // workOrder.setInstrument(DriverCobas411.INSTRUMENT_NAME);
            workOrder.setSid(getValueOfElement(rootElement, "sid"));
            workOrder.setRack(getValueOfElement(rootElement, "rack"));
            workOrder.setPosition(Integer.parseInt(getValueOfElement(rootElement, "position")));
            workOrder.setSampleType(getValueOfElement(rootElement, "sampleType"));
            workOrder.setPatientName(getValueOfElement(rootElement, "patientName"));
            workOrder.setPatientNumber(getValueOfElement(rootElement, "patientNumber"));
            workOrder.setDateBirth(parseDate(getValueOfElement(rootElement, "dateBirth")));
            workOrder.setSex(parseSex(getValueOfElement(rootElement, "sex")));
            workOrder.setDateCollection(parseDate(getValueOfElement(rootElement, "dateCollection")));
            workOrder.setLaborantName(getValueOfElement(rootElement, "laborantName"));
            workOrder.setTests(tests);
            workOrder.setAddParamsFromString(getAddParams(rootElement));
            // }
            //System.out.println("addPar:\n" + getAddParams(rootElement));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
        return workOrder;
    }

    public ParamsMessage parseStringMessageToParams() throws DaoException {
        ParamsMessage paramsMessage = new ParamsMessage();
        try {
            Element rootElement = this.documentXml.getDocumentElement();
            rootElement.normalize();
            paramsMessage.id = rootElement.getAttribute("id");
            paramsMessage.sender = rootElement.getAttribute("sender");
            paramsMessage.parameters = getValueOfElement(rootElement, "parameters");
            paramsMessage.sid = getValueOfElement(rootElement, "sid");
            paramsMessage.rack = getValueOfElement(rootElement, "rack");
            paramsMessage.position = getValueOfElement(rootElement, "position");
            paramsMessage.sampleType = getValueOfElement(rootElement, "sampleType");
            paramsMessage.patientName = getValueOfElement(rootElement, "patientName");
            if (paramsMessage.patientName != null && paramsMessage.patientName.length() > 50) {
                paramsMessage.patientName = paramsMessage.patientName.substring(0, 50);
            }
            paramsMessage.patientNumber = getValueOfElement(rootElement, "patientNumber");
            paramsMessage.dateBirth = getValueOfElement(rootElement, "dateBirth");
            paramsMessage.sex = getValueOfElement(rootElement, "sex");
            paramsMessage.dateCollection = getValueOfElement(rootElement, "dateCollection");
            paramsMessage.laborantName = getValueOfElement(rootElement, "laborantName");
            paramsMessage.addParams = getAddParamsToMap(rootElement);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return paramsMessage;
    }

    public boolean validateWorkOrder(String strXml) {
        StringBuilder strBuilder = new StringBuilder();
        try {
            Element rootElement = this.documentXml.getDocumentElement();
            rootElement.normalize();
            System.out.println("Root element :" + rootElement.getNodeName());
            /*
             workOrder.setMid(rootElement.getAttribute("id"));
             workOrder.setInstrument(getValueOfElement(rootElement, "instrumentId"));
             workOrder.setSid(getValueOfElement(rootElement, "sid"));
             workOrder.setRack(getValueOfElement(rootElement, "rack"));
             workOrder.setPosition(Integer.parseInt(getValueOfElement(rootElement, "position")));
             workOrder.setSampleType(getValueOfElement(rootElement, "sampleType"));
             workOrder.setPatientName(getValueOfElement(rootElement, "patientName"));
             workOrder.setPatientNumber(getValueOfElement(rootElement, "patientNumber"));
             workOrder.setDateBirth(parseDate(getValueOfElement(rootElement, "dateBirth")));
             workOrder.setSex(parseSex(getValueOfElement(rootElement, "sex")));
             workOrder.setDateCollection(parseDate(getValueOfElement(rootElement, "dateCollection")));
             workOrder.setLaborantName(getValueOfElement(rootElement, "laborantName"));
             workOrder.setTests(getTests(rootElement));
             workOrder.setAddParamsFromString(getAddParams(rootElement));
             */
            //System.out.println("addPar:\n" + getAddParams(rootElement));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateBySchema() {
        // 1. Поиск и создание экземпляра фабрики для языка XML Schema
        SchemaFactory factory
                = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        // 2. Компиляция схемы
        // Схема загружается в объект типа java.io.File, но вы также можете использовать 
        // классы java.net.URL и javax.xml.transform.Source
        //Path path =
        File schemaLocation = new File("xsd\\" + "messageAddWorkOrder" + ".xsd");
        Path path = FileSystems.getDefault().getPath("xsd/" + "messageAddWorkOrder" + ".xsd");
        //System.out.println("path = " + path.toAbsolutePath());

        try {
//            Schema schema = factory.newSchema(schemaLocation);
            Schema schema = factory.newSchema(path.toFile());
            // 3. Создание валидатора для схемы
            Validator validator = schema.newValidator();
            // String xmlLocation = "xsd\\messageAddWorkOrderExample.xml";
            // 4. Разбор проверяемого документа
            Source source = new StreamSource(new StringReader(this.strXml));
//        Source source = new StreamSource(new File(xmlLocation));
            // 5. Валидация документа
            validator.validate(source);
            //System.out.println("xml is valid.");
        } catch (Exception ex) {
            responseMessage = "xml is not valid because ";
            responseMessage += ex.getMessage();
            //System.out.println("xml is not valid because ");
            //System.out.println(ex.getMessage());
            return false;
        }
        return true;

    }

    public void validateTempMethod() throws SAXException, IOException {
        String fileBase = "message";
//String fileBase = "shiporder";
        // 1. Поиск и создание экземпляра фабрики для языка XML Schema
        SchemaFactory factory
                = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        // 2. Компиляция схемы
        // Схема загружается в объект типа java.io.File, но вы также можете использовать 
        // классы java.net.URL и javax.xml.transform.Source
//        File schemaLocation = new File("S:\\DOCS\\ELSI_TECH\\_Командировки\\07_Усть-Каменогорск\\04_09февр_2015\\message.xsd");
//        File schemaLocation = new File("S:\\DOCS\\ELSI_TECH\\_Командировки\\07_Усть-Каменогорск\\04_09февр_2015\\xml_work\\" + fileBase + ".xsd");
        File schemaLocation = new File("xsd\\" + "messageAddWorkOrder" + ".xsd");
        Schema schema = factory.newSchema(schemaLocation);

        // 3. Создание валидатора для схемы
        Validator validator = schema.newValidator();
//        String xmlLocation = "S:\\DOCS\\ELSI_TECH\\_Командировки\\07_Усть-Каменогорск\\04_09февр_2015\\xml_work\\" + fileBase + ".xml";
        String xmlLocation = "xsd\\messageAddWorkOrderExample.xml";
        // 4. Разбор проверяемого документа
        //Source source = new StreamSource(new StringReader(this.strXml));
        Source source = new StreamSource(new File(xmlLocation));

        // 5. Валидация документа
        try {
            validator.validate(source);
            System.out.println("xml is valid.");
        } catch (SAXException ex) {
            System.out.println("xml is not valid because ");
            System.out.println(ex.getMessage());
        }

    }

    private static String getValueOfElement(Element parentElement, String nameOfChildElement) {
        return parentElement.getElementsByTagName(nameOfChildElement).item(0).getTextContent();
    }

    private void createDocument() {
        StringBuilder strBuilder = new StringBuilder();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(strXml));
            //System.out.println("strXml = " + strXml);
            this.documentXml = dBuilder.parse(is);
        } catch (Exception ex) {
            System.out.println("ex = " + ex.getMessage());
        }
    }

    public String getAttributeValue(String attribute) {
        return this.documentXml.getDocumentElement().getAttribute(attribute);
    }

    public String getIncomeNumber() {
        return getAttributeValue("id");
    }

    public String getSenderName() {
        return getAttributeValue("sender");
    }

    public static java.util.Date parseDate(String str) throws LisParseException {
        try {
            if (str.length() != 8) {
                throw new LisParseException("data format must be 'yyyyMMdd'");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.parse(str);
        } catch (ParseException ex) {
            throw new LisParseException(ex);
        }
    }

    public static int parseSex(String str) throws LisParseException {
        if (str.equals("1") || str.equals("2") || str.equals("3")) {
            return Integer.parseInt(str);
        } else {
            throw new LisParseException("sex format: 1 - male, 2- female, 3 - unknown");
        }
    }

    private String getTests(Element rootElement) {
        String returnStr = "";
        Element testsElements = (Element) rootElement.getElementsByTagName("tests").item(0);
        NodeList testNodeList = testsElements.getElementsByTagName("testCode");
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < testNodeList.getLength(); i++) {
            Node nNode = testNodeList.item(i);
            Element testElement = (Element) nNode;
            if (!testElement.getTextContent().isEmpty()) {
                stb.append(",").append(testElement.getTextContent());
            }
        }
        if (stb.length() > 0) {
            returnStr = stb.substring(1);
        }
        return returnStr;
    }

    private String getAddParams(Element rootElement) {
        String returnStr = "";
        Element testsElements = (Element) rootElement.getElementsByTagName("addParams").item(0);
        NodeList testNodeList = testsElements.getElementsByTagName("param");
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < testNodeList.getLength(); i++) {
            Node nNode = testNodeList.item(i);
            Element testElement = (Element) nNode;
            String key = testElement.getAttribute("key");
            String value = testElement.getAttribute("value");
            stb.append(",").append(key).append("=").append(value);
        }
        if (stb.length() > 0) {
            returnStr = stb.substring(1);
        }
        return returnStr;
    }

    private Properties getAddParamsToMap(Element rootElement) {
        Properties properties = new Properties();
        String returnStr = "";
        Element testsElements = (Element) rootElement.getElementsByTagName("addParams").item(0);
        if (testsElements == null) {
            return null;
        }
        NodeList testNodeList = testsElements.getElementsByTagName("param");
        for (int i = 0; i < testNodeList.getLength(); i++) {
            Node nNode = testNodeList.item(i);
            Element testElement = (Element) nNode;
            String key = testElement.getAttribute("key");
            String value = testElement.getAttribute("value");
            properties.setProperty(key, value);
        }
        return properties;
    }

    public static List<WorkOrder> separateParamsTOWorkOrders(String parameters)
            throws ModelException {
        List<WorkOrder> listWo = new ArrayList<>();
        try {
            Model model = new ModelImpl();
            List<Test> listTests = new ArrayList<>();
            int[] paramsArray = Util.getIntArrayFromCSVString(parameters);
            for (int paramId : paramsArray) {
                Test testDefault = model.getParameterWithDeafaultTest(paramId).getTestDefault();
                listTests.add(testDefault);
            }
            Collections.sort(listTests);
            String instrName = "";
            int i = 0;
            WorkOrder wo = null;
            for (int j = 0; j < listTests.size(); j++) {
                Test testEl = listTests.get(j);
                if (!instrName.equals(testEl.getInstrument().getName())) {
                    if (wo != null) {
                        wo.setTests(wo.getTests().substring(1));
                        listWo.add(wo);
                    }
                    wo = new WorkOrder();
                    wo.setInstrument(testEl.getInstrument());
                }
                wo.setTests(wo.getTests() + "," + testEl.getCode());
                instrName = testEl.getInstrument().getName();
                if (j == listTests.size() - 1) {
                    wo.setTests(wo.getTests().substring(1));
                    listWo.add(wo);
                }
            }
        } catch (ModelException ex) {
            throw ex;
        }
        return listWo;
    }

    public String getStrXml() {
        return strXml;
    }

    public void setStrXml(String strXml) {
        this.strXml = strXml;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Document getDocumentXml() {
        return this.documentXml;
    }
}
