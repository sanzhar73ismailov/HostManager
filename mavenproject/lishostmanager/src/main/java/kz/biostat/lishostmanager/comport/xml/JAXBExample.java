package kz.biostat.lishostmanager.comport.xml;

import java.io.File;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBExample {

    public static void main(String[] args) {
        unmarshalFromString();
    }

    public static void unmarshalFromString() {
        try {

            // File file = new File("C:\\111\\file.xml");
            String xmlString = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n"
                    + "<customer id='105'>\n"
                    + "    <age>25</age>\n"
                    + "    <name>mkyong m</name>\n"
                    + "</customer>";
            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Customer customer = (Customer) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));

            System.out.println(customer);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void unmarshal() {
        try {

            File file = new File("C:\\111\\file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Customer customer = (Customer) jaxbUnmarshaller.unmarshal(file);
            System.out.println(customer);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void marshal() {

        Customer customer = new Customer();
        customer.setId(100);
        customer.setName("mkyong");
        customer.setAge(29);

        try {

            File file = new File("C:\\111\\file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(customer, file);
            jaxbMarshaller.marshal(customer, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
