package kz.biostat.lishostmanager.web.lab.webservice;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.Message;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.ModeSystemWorking;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;
import kz.biostat.lishostmanager.comport.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HostManagerHelper {

    private Model model;

    public HostManagerHelper(Model model) {
        this.model = model;
    }

    public WorkOrder getWorkOrderFromParams(Message message, ParamsMessage params) throws LisParseException {
        try {
            WorkOrder wo = new WorkOrder();
            wo.setInstrument(new Instrument(2, null));
            wo.setMid(message.getId());
            wo.setSid(params.sid);
            wo.setRack(params.rack);
            wo.setPosition(Integer.parseInt(params.position));
            wo.setSampleType(params.sampleType);
            wo.setPatientName(params.patientName);
            wo.setPatientNumber(params.patientNumber);
            wo.setDateBirth(XmlWork.parseDate(params.dateBirth));
            wo.setSex(XmlWork.parseSex(params.sex));
            wo.setDateCollection(XmlWork.parseDate(params.dateCollection));
            wo.setLaborantName(params.laborantName);
            wo.setStatus(0);
            wo.setTests(model.getTestsFromParameters(params.parameters));
            wo.setAddParams(params.addParams);
            return wo;
        } catch (ModelException ex) {
            throw new LisParseException(ex);
        }
    }

    public List<WorkOrder> getWorkOrdersFromParams(Message message, ParamsMessage params, ModeSystemWorking modeSystemWorking) throws LisParseException {
        try {
            List<Test> listTest = null;
            if (1 == 1) { // пока выключил
                if (modeSystemWorking == ModeSystemWorking.BARCODE_NO) {
                    listTest = model.getAllTestsListFromParameters(params.parameters);//посылаем на все аппараты задания
                } else {
                    listTest = model.getTestsListFromParameters(params.parameters); //посылаем только на те что по умолчанию
                }
            }
           // listTest = model.getTestsListFromParameters(params.parameters); //посылаем только на те что по умолчанию

            //getAllTestsListFromParameters
            //get list WorkOrders depends on Instrument of Tests
            // получаем список заданий для различных аппаратов (кол-во зависит от тестов)
            List<WorkOrder> listWo = model.getWorkorderListFromTests(listTest);

            for (WorkOrder wo : listWo) {
                wo.setMid(message.getId());
                wo.setSid(params.sid);
                wo.setRack(params.rack);
                int position = 0;
                try {
                    position = Integer.parseInt(params.position);
                } catch (java.lang.NumberFormatException ex) {
                    System.out.println("ex = " + ex);
                }
                wo.setPosition(position);
                wo.setSampleType(params.sampleType);
                wo.setPatientName(params.patientName);
                wo.setPatientNumber(params.patientNumber);
                wo.setDateBirth(XmlWork.parseDate(params.dateBirth));
                wo.setSex(XmlWork.parseSex(params.sex));
                wo.setDateCollection(XmlWork.parseDate(params.dateCollection));
                wo.setLaborantName(params.laborantName);
                wo.setStatus(0);
                wo.setAddParams(params.addParams);
            }
            return listWo;
        } catch (ModelException ex) {
            throw new LisParseException(ex);
        }
    }

    public String getResutResponseXML(List<Result> listResults, Message message) throws LisParseException {
        String strToReturn = null;
        try {
            Model model = new ModelImpl();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("results");

            if (message == null) {
                rootElement.setAttribute("messageId", "");
            } else {
                rootElement.setAttribute("messageId", message.getIncomeNumber());
            }
            doc.appendChild(rootElement);
            for (int i = 0; i < listResults.size(); i++) {
                Result resultObj = listResults.get(i);
                // staff elements
                Element resultElement = doc.createElement("result");
                rootElement.appendChild(resultElement);

                resultElement.setAttribute("parameterId", resultObj.getParameterId() + "");;

                Element instrument = doc.createElement("instrument");
                //Instrument instr = model.getInstrumentFromResult(resultObj);
                //instrument.appendChild(doc.createTextNode(instr.getName()));
                instrument.appendChild(doc.createTextNode(resultObj.getInstrument()));
                resultElement.appendChild(instrument);

                Element testCode = doc.createElement("testCode");
                testCode.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getTestCode())));
                resultElement.appendChild(testCode);

                Element value = doc.createElement("value");
                value.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getValue())));
                resultElement.appendChild(value);

                Element units = doc.createElement("units");
                String unitsVal = Util.getAsciiPrintable(resultObj.getUnits());
                units.appendChild(doc.createTextNode(getEmptyStringIfNull(unitsVal)));
                resultElement.appendChild(units);

                Element referenseRanges = doc.createElement("referenseRanges");
                referenseRanges.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getReferenseRanges())));
                resultElement.appendChild(referenseRanges);

                Element abnormalFlags = doc.createElement("abnormalFlags");
                abnormalFlags.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getAbnormalFlags())));
                resultElement.appendChild(units);

                Element initialRerun = doc.createElement("initialRerun");
                initialRerun.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getInitialRerun())));
                resultElement.appendChild(initialRerun);

                Element rawText = doc.createElement("rawText");
                rawText.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getRawText())));
                //rawText.appendChild(doc.createTextNode(""));
                resultElement.appendChild(rawText);

                Element comment = doc.createElement("comment");
                comment.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getComment())));
                resultElement.appendChild(comment);

                Element status = doc.createElement("status");
                status.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getStatus())));
                resultElement.appendChild(status);

                Element addParams = doc.createElement("addParams");
                addParams.appendChild(doc.createTextNode(getEmptyStringIfNull(resultObj.getAddParams())));
                resultElement.appendChild(addParams);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);

            strToReturn = stringWriter.getBuffer().toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new LisParseException(ex);

        }
        return strToReturn;
    }

    public String getResutResponseXML(List<Result> listResults) throws LisParseException {
        return getResutResponseXML(listResults, null);
    }

    String getEmptyStringIfNull(String str) {
        return str == null ? "" : str;
    }

    private static String getUniqueTests(String str) {
        return getUniqueTests(str.split(","));
    }

    private static String getUpdatedTests(String testsOld, String testsNew) {
        String[] strArray = concat(testsOld.split(","), testsNew.split(","));
        return getUniqueTests(strArray);
    }

    private static String getUniqueTests(String[] strArray) {

        if (Util.isAllDigits(strArray)) {
            strArray = Util.getSortedAsDigits(strArray);
        } else {
            java.util.Arrays.sort(strArray);
        }
        Set<String> set = new LinkedHashSet<>();
        for (String string : strArray) {
            set.add(string);
        }
        Iterator iter = set.iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            sb.append(",").append(iter.next());
        }
        return sb.substring(1);
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    @Deprecated
    // xml c инструментом и тест кодами, но параметры пустрые
    public static String getLisMessageXMLWithInstrumentAndTests() {
        String str = "<?xml version=\"1.0\"?>\n"
                + "<message id=\"123\">\n"
                + "  <parameters>2,12,3,1,7,9</parameters>\n"
                + "  <instrumentId>cobas411</instrumentId>\n"
                + "  <sid>100</sid>\n"
                + "  <rack>0</rack>\n"
                + "  <position>5</position>\n"
                + "  <sampleType>SC</sampleType>\n"
                + "  <patientName>Ivanov Petr</patientName>\n"
                + "  <patientNumber>45345345</patientNumber>\n"
                + "  <dateBirth>19731201</dateBirth>\n"
                + "  <sex>3</sex>\n"
                + "  <dateCollection>20150207</dateCollection>\n"
                + "  <laborantName>Valentina</laborantName>\n"
                + "  <tests>\n"
                + "	  <testCode>12</testCode>\n"
                + "	  <testCode>13</testCode>\n"
                + "	  <testCode>15</testCode>\n"
                + "	</tests>\n"
                + "  <addParams>\n"
                + "	   <param key='dilution' value='1'/>\n"
                + "        <param key='priority' value='R'/>\n"
                + "  </addParams>\n"
                + "</message>";
        return str;
    }

    // xml c параметрами, но без инструмента и тест кодов 
    public static String getLisMessageXML() {
        String str = "<?xml version=\"1.0\"?>\n"
                //+ "<message id=\"123477q11qqq\" sender=\"host_manager\">\n"
                + "<message id=\"19 : 2015-03-20\" sender=\"host_manager\">\n"
                + "  <parameters>17,3,4</parameters>\n"
                + "  <sid>1003_5</sid>\n"
                + "  <rack>0</rack>\n"
                + "  <position>5</position>\n"
                + "  <sampleType>SC</sampleType>\n"
                + "  <patientName>Ivanov Petr</patientName>\n"
                + "  <patientNumber>45345345</patientNumber>\n"
                + "  <dateBirth>19731201</dateBirth>\n"
                + "  <sex>3</sex>\n"
                + "  <dateCollection>20150207</dateCollection>\n"
                + "  <laborantName>Valentina</laborantName>\n"
                + "  <addParams>\n"
                + "	   <param key='dilution' value='1'/>\n"
                + "        <param key='priority' value='R'/>\n"
                + "  </addParams>\n"
                + "</message>";
        return str;
    }

    public static String getLisMessageXMLForDimensionXpandByParams(String mid, String sid, String params) {
        String str = "<?xml version=\"1.0\"?>\n"
                //+ "<message id=\"123477q11qqq\" sender=\"host_manager\">\n"
                + "<message id=\"" + mid + "\" sender=\"host_manager\">\n"
                + "  <parameters>" + params + "</parameters>\n"
                + "  <sid>" + sid + "</sid>\n"
                + "  <rack>e</rack>\n"
                + "  <position>1</position>\n"
                + "  <sampleType>ser</sampleType>\n"
                + "  <patientName>Ivanov Petr</patientName>\n"
                + "  <patientNumber>45345345</patientNumber>\n"
                + "  <dateBirth>19731201</dateBirth>\n"
                + "  <sex>3</sex>\n"
                + "  <dateCollection>20150207</dateCollection>\n"
                + "  <laborantName>Valentina</laborantName>\n"
                + "  <addParams>\n"
                + "	   <param key='dilution' value='1'/>\n"
                + "        <param key='priority' value='R'/>\n"
                + "  </addParams>\n"
                + "</message>";
        return str;
    }

    // xml c параметрами, но без инструмента и тест кодов 
    public static String getLisMessageXMLForDimensionXpand() {
        String str = "<?xml version=\"1.0\"?>\n"
                //+ "<message id=\"123477q11qqq\" sender=\"host_manager\">\n"
                + "<message id=\"21 : 2015-05-15\" sender=\"host_manager\">\n"
                + "  <parameters>4001,4020</parameters>\n"
                + "  <sid>1004</sid>\n"
                + "  <rack>e</rack>\n"
                + "  <position>1</position>\n"
                + "  <sampleType>ser</sampleType>\n"
                + "  <patientName>Ivanov Petr</patientName>\n"
                + "  <patientNumber>45345345</patientNumber>\n"
                + "  <dateBirth>19731201</dateBirth>\n"
                + "  <sex>3</sex>\n"
                + "  <dateCollection>20150207</dateCollection>\n"
                + "  <laborantName>Valentina</laborantName>\n"
                + "  <addParams>\n"
                + "	   <param key='dilution' value='1'/>\n"
                + "        <param key='priority' value='R'/>\n"
                + "  </addParams>\n"
                + "</message>";
        return str;
    }

    public static String getRespXml() {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<results messageId=\"1\">\n"
                + "    <result parameterId=\"12\">\n"
                + "        <instrument>cobas411</instrument>\n"
                + "        <testCode>1</testCode>\n"
                + "        <value>12</value>\n"
                + "        <units>uM/L</units>\n"
                + "        <referenseRanges></referenseRanges>\n"
                + "        <abnormalFlags>L</abnormalFlags>\n"
                + "        <initialRerun>F</initialRerun>\n"
                + "        <rawText></rawText>\n"
                + "        <comment></comment>\n"
                + "        <status></status>\n"
                + "        <addParams>dilution=1,predilution=1</addParams>\n"
                + "    </result>\n"
                + "    <result parameterId=\"15\">\n"
                + "        <instrument>cobas411</instrument>\n"
                + "        <testCode>15</testCode>\n"
                + "        <value>24</value>\n"
                + "        <units>mg/L</units>\n"
                + "        <referenseRanges>12-17</referenseRanges>\n"
                + "        <abnormalFlags>H</abnormalFlags>\n"
                + "        <initialRerun>F</initialRerun>\n"
                + "        <rawText></rawText>\n"
                + "        <comment></comment>\n"
                + "        <status></status>\n"
                + "        <addParams>dilution=2,predilution=2</addParams>\n"
                + "    </result>\n"
                + "</results>";
        return str;

    }
}
