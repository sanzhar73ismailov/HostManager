package test;

import entity.Instrument;
import entity.Result;
import static instrument.ASCII.*;
import instrument.DriverDimensionXpand;
import instrument.DriverInstrument;
import instrument.InstrumentException;
import instrument.InstrumentIndicator;
import instrument.dimensionXpand.MessageDimensionXpand;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestMessageDimensionXpand {

    public static void main(String[] args) throws Exception {
        //testCreateMessage();
        //testGetResults();
        testSaveResults();
        //testDate();
        // testSplitByFs();
    }

    private static void testCreateMessage() {
        String str = STX_STRING + "R<FS>*<FS>4<FS>4<FS>1<FS>p<FS>0<FS>222811290415<FS>1"
                + "<FS>1<FS>3<FS>TP<FS>77<FS>g/L<FS><FS>ALT<FS>57.7<FS>U/L<FS>"
                + "<FS>AST<FS>24.7<FS>U/L<FS><FS>2C" + ETX_STRING;
        str = str.replaceAll("<FS>", FS_STRING);
        MessageDimensionXpand message = new MessageDimensionXpand(str.getBytes());
        //String[] fields = message.getFields();
        String[] fields = str.split(FS_STRING);
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            System.out.println("field " + i + ": <" + field + ">");
        }

    }

    private static DriverDimensionXpand gerDriver() throws ModelException, InstrumentException {
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        //instrInd.startInstrument(3);

        Instrument instrument = model.getObject(3, new Instrument());
        //   instrument.setIp("localhost");
//        instrument.setIp("192.168.1.206");
        instrument.setIp("192.168.127.254");
        DriverDimensionXpand driver = new DriverDimensionXpand(instrument, model);
        return driver;
    }

    private static void testSplitByFs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void testGetResults() throws InstrumentException, ModelException {
        String str = STX_STRING + "R<FS>*<FS>4<FS>4<FS>1<FS>p<FS>0<FS>222811290415<FS>1"
                + "<FS>1<FS>3<FS>TP<FS>77<FS>g/L<FS><FS>ALT<FS>57.7<FS>U/L<FS>"
                + "<FS>AST<FS>24.7<FS>U/L<FS><FS>2C" + ETX_STRING;
        str = str.replaceAll("<FS>", FS_STRING);
        MessageDimensionXpand message = new MessageDimensionXpand(str.getBytes());
        DriverInstrument drv = gerDriver();
        List<entity.Result> listResults = drv.getResultsFromMessage(message);
        for (Result res : listResults) {
            System.out.println("res = " + res );
        }
    }
    private static void testSaveResults() throws InstrumentException, ModelException {
        String str = STX_STRING + "R<FS>*<FS>4<FS>4<FS>1<FS>p<FS>0<FS>222811290415<FS>1"
                + "<FS>1<FS>3<FS>TP<FS>77<FS>g/L<FS><FS>ALT<FS>57.7<FS>U/L<FS>"
                + "<FS>AST<FS>24.7<FS>U/L<FS><FS>2C" + ETX_STRING;
        str = str.replaceAll("<FS>", FS_STRING);
        MessageDimensionXpand message = new MessageDimensionXpand(str.getBytes());
        DriverInstrument drv = gerDriver();
        drv.saveResults(message);
//        List<entity.Result> listResults = drv.getResultsFromMessage(message);
//        for (Result res : listResults) {
//            System.out.println("res = " + res );
//        }

    }
    
    private static void testDate(){
        
        String str = "502211290415";
        SimpleDateFormat sdf = new SimpleDateFormat("ssmmHHddMMyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        System.out.println(sdf.format(new Date()));
        
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException ex) {
            Logger.getLogger(TestMessageDimensionXpand.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("date = " + date);
    }
}
