package kz.biostat.lishostmanager.web.lab.test;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import kz.biostat.lishostmanager.web.lab.webservice.HostManager;
import kz.biostat.lishostmanager.web.lab.webservice.HostManagerHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestHostManager {

    public static void main(String[] args) {
        //testAddWorkorder();
        //createXml();
        //testGetRults();
//        testGetResultsByInstrumentAndSid();
        // printMessageXML();
        //met1();
        //testAddWorkorder();
//        testGetRultsBySid();
//System.out.println(Integer.parseInt("g123"));
        testNewMethod();

    }

    private static void testNewMethod() {
        String strCsv = "";
        try {
            HostManager hm = new HostManager();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //Date date = sdf.parse("2016-04-02");
//            Date date = sdf.parse("2016-05-20");
            //2016-05-20
//            String instrName = "dimensionXpand";
            String instrName = "dimensionXpand";
            //String instrName = "immulite2000";
           // String instrName = "advia2120";
            
            //adviaCentaurCP
//            String dateAsStr = "2016-04-01";
            String dateAsStr = "2016-03-28";
            dateAsStr = "2016-05-20";
            strCsv = hm.getResultsByInstrumentAndDate(instrName, dateAsStr);
            //System.out.println("strCsv = " + strCsv);
        } catch (Exception ex) {
            Logger.getLogger(TestHostManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void met1() {
        String str = "^123";
        str = str.replaceAll("\\^", "");
        System.out.println("str = " + str);
    }

    private static void testAddWorkorder() {
        HostManager hm = new HostManager();
//        String strXml = HostManagerHelper.getLisMessageXML();
//        String strXml = HostManagerHelper.getLisMessageXMLForDimensionXpand();
        String mid = "3 : 2015-05-26";
        String sid = "test03_15-05-26";
        String params = "4027,4028";
        String strXml = HostManagerHelper.getLisMessageXMLForDimensionXpandByParams(mid, sid, params);
        String str = hm.addWorkOrder(strXml);
        System.out.println("str = " + str);
    }

    public static void createXml() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("company");
            doc.appendChild(rootElement);

            // staff elements
            Element staff = doc.createElement("Staff");
            rootElement.appendChild(staff);

            // set attribute to staff element
            Attr attr = doc.createAttribute("id");
            attr.setValue("1");
            staff.setAttributeNode(attr);

            // shorten way
            // staff.setAttribute("id", "1");
            // firstname elements
            Element firstname = doc.createElement("firstname");
            firstname.appendChild(doc.createTextNode("yong"));
            staff.appendChild(firstname);

            // lastname elements
            Element lastname = doc.createElement("lastname");
            lastname.appendChild(doc.createTextNode("mook kim"));
            staff.appendChild(lastname);

            // nickname elements
            Element nickname = doc.createElement("nickname");
            nickname.appendChild(doc.createTextNode("mkyong"));
            staff.appendChild(nickname);

            // salary elements
            Element salary = doc.createElement("salary");
            salary.appendChild(doc.createTextNode("100000"));
            staff.appendChild(salary);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            //StreamResult result = new StreamResult(new File("C:\\file123.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            String str = "";
            StringWriter wr = new StringWriter();
            StreamResult result = new StreamResult(wr);
            transformer.transform(source, result);
            // wr.write(str);
            //wr.flush();
            System.out.println("str = " + wr.getBuffer().toString());
            String output = wr.getBuffer().toString().replaceAll("\n|\r", "");
            System.out.println("File saved!");
        } catch (Exception ex) {
            Logger.getLogger(TestHostManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void testGetRults() {
        HostManager hm = new HostManager();
        String str = hm.getResults("123123", "host_manager");
        System.out.println("str = " + str);
    }

    private static void testGetResultsByInstrumentAndSid() {
        HostManager hm = new HostManager();
        String str = hm.getResultsByInstrumentAndSid("advia2120", "sid_123123");
        System.out.println("str = " + str);
    }

    public static void printMessageXML() {
        String str = HostManagerHelper.getLisMessageXML();
        System.out.println("str = " + str);
    }

    private static void testGetRultsBySid() {
        HostManager hm = new HostManager();
        String str = hm.getResults("75915", "host_manager");
        System.out.println("str = " + str);
    }
}
