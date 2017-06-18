package xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.StringReader;
import org.xml.sax.InputSource;

public class ReadXMLFile {
    public static void main(String[] args) {
        readLisMessage();
    }

    public static void readXMLExapmleFromInternet() {
        try {
            String strXml = "<?xml version=\"1.0\"?>\n"
                    + "<company>\n"
                    + "	<staff id=\"1001\">\n"
                    + "		<firstname>yong</firstname>\n"
                    + "		<lastname>mook kim</lastname>\n"
                    + "		<nickname>mkyong</nickname>\n"
                    + "		<salary>100000</salary>\n"
                    + "	</staff>\n"
                    + "	<staff id=\"2001\">\n"
                    + "		<firstname>low</firstname>\n"
                    + "		<lastname>yin fong</lastname>\n"
                    + "		<nickname>fong fong</nickname>\n"
                    + "		<salary>200000</salary>\n"
                    + "	</staff>\n"
                    + "</company>";
            System.out.println("strXml = " + strXml);
            File fXmlFile = new File("c:\\temp\\staf.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(fXmlFile);
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(strXml));
            Document doc = dBuilder.parse(is);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("staff");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("Staff id : " + eElement.getAttribute("id"));
                    System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readLisMessage() {
        try {
            String strXml = getLisMessageXML();
            System.out.println("strXml = " + strXml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(strXml));
            Document doc = dBuilder.parse(is);
            Element rootElement = doc.getDocumentElement();
            rootElement.normalize();
            System.out.println("Root element :" + rootElement.getNodeName());
            String instrument = rootElement.getElementsByTagName("instrumentId").item(0).getTextContent();
            System.out.println("instrument = " + instrument);
            if (1 == 1) {
                return;
            }
            NodeList nList = doc.getElementsByTagName("staff");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("Staff id : " + eElement.getAttribute("id"));
                    System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLisMessageXML() {
        String str = "<?xml version=\"1.0\"?>\n"
                + "<message id=\"123\">\n"
                + "  <instrumentId>cobas411</instrumentId>\n"
                + "  <sid>100</sid>\n"
                + "  <rack>0</rack>\n"
                + "  <position>5</position>\n"
                + "  <sampleType>SC</sampleType>\n"
                + "  <patientName>Ivanov Petr</patientName>\n"
                + "  <patientNumber>45345345345</patientNumber>\n"
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
}
