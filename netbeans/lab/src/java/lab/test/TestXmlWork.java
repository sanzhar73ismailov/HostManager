package lab.test;

import entity.WorkOrder;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import lab.webservice.LisParseException;
import lab.webservice.XmlWork;
import static lab.webservice.XmlWork.separateParamsTOWorkOrders;
import modelHost.DaoException;
import modelHost.ModelException;
import org.xml.sax.SAXException;
import static xml.ReadXMLFile.getLisMessageXML;

public class TestXmlWork {
    public static void main(String[] args) throws ParseException, LisParseException, SAXException, IOException, DaoException, SQLException, ModelException {
        XmlWork xmlWorkObj = new XmlWork(getLisMessageXML());
        //WorkOrder wo = xmlWorkObj.parseStringMessage();
        System.out.println("---------------");
        xmlWorkObj.validateTempMethod();
        //Date d =parseDate("20120216");
        //System.out.println("d = " + d);
        if (1 == 0) {
            return;
        }
        String parsStr = "10,1,2,3,25,30";
        List<WorkOrder> list = separateParamsTOWorkOrders(parsStr);
        for (WorkOrder workOrder : list) {
            System.out.println("workOrder = " + workOrder.getInstrument() + ": " + workOrder.getTests());
        }
    }

}
