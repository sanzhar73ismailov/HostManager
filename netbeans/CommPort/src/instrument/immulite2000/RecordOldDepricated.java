package instrument.immulite2000;

import entity.WorkOrder;
import static instrument.ASCII.*;
import instrument.DriverImmulite2000;
import instrument.InstrumentException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;

/**
 *
 * @author sanzhar.ismailov
 */
@Deprecated
class RecordOldDepricated extends instrument.astm.Record {

    private RecordOldDepricated(String textRecord) throws InstrumentException {
        super(textRecord);
    }

    private static RecordOldDepricated createHeaderRecord1() throws Immulite2000Exception {
        try {
            String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            if (DriverImmulite2000.PASSWORD_INSTR.isEmpty()) {
                throw new Immulite2000Exception("password not defined in DriverImmulite2000.PASSWORD_INSTR");
            }

            if (DriverImmulite2000.SENDER_ID.isEmpty()) {
                throw new Immulite2000Exception("senderID not defined in DriverImmulite2000.SENDER_ID");
            }
            if (DriverImmulite2000.RECEIVER_ID.isEmpty()) {
                throw new Immulite2000Exception("receiverID not defined in DriverImmulite2000.RECEIVER_ID");
            }
            String text = "H|\\^&||"
                    + DriverImmulite2000.PASSWORD_INSTR + "|"
                    + DriverImmulite2000.RECEIVER_ID + "|Markova|||8N1|"
                    + DriverImmulite2000.SENDER_ID + "||P|1|" + dateTime + CR_STRING + ETX_STRING;
            RecordOldDepricated record = new RecordOldDepricated(text);
            return record;
        } catch (InstrumentException ex) {
            throw new Immulite2000Exception(ex);
        }
    }

    private static RecordOldDepricated createPatientRecord1(WorkOrder wo) throws Immulite2000Exception {
        try {
            String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String patId = "";
            String patName = "";
            if (wo.getPatientNumber() != null) {
                patId = wo.getPatientNumber().length() > 20 ? wo.getPatientNumber().substring(0, 20) : wo.getPatientNumber();
                patId = replaceAllSpecialCharacters(patId);
            }
            if (wo.getPatientName() != null) {
                patName = replaceAllSpecialCharacters(wo.getPatientName());
                patName = wo.getPatientName().replaceAll(" ", " ");
                patName = patName.length() > 30 ? patName.substring(0, 30) : patName;
                patName = patName.replaceAll(" ", "^");
            }
            String patDateBirth = wo.getDateBirth() != null ? new SimpleDateFormat("yyyyMMdd").format(wo.getDateBirth()) : "";
            String patSex = "";
            switch (wo.getSex()) {
                case 1:
                    patSex = "M";
                    break;
                case 2:
                    patSex = "F";
                    break;
            }
            String text = "P|1|"
                    + patId + "|"
                    + "|"
                    + "|"
                    + patName + "|"
                    + "|"
                    + patDateBirth + "|"
                    + patSex + "|"
                    + "|" + "|" + "|" + "|"
                    + CR_STRING + ETX_STRING;
            RecordOldDepricated record = new RecordOldDepricated(text);
            return record;
        } catch (InstrumentException ex) {
            throw new Immulite2000Exception(ex);
        }
    }

    private static List<RecordOldDepricated> createOrderRecords1(WorkOrder wo) throws Immulite2000Exception {
        List<RecordOldDepricated> orderRecords = new ArrayList<>();
        String sid = wo.getSid();
        if (sid.length() > 20) {
            throw new Immulite2000Exception("length of sid more than 20 symbols: " + sid);
        }
        String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String[] tests = wo.getTests().split(",");
        for (int i = 0; i < tests.length; i++) {
            try {
                String text = "O|1|"
                        + sid + "|"
                        + "|"
                        + "^^^" + tests[i] + "|"
                        + "R" + "|"
                        + dateTime + "|"
                        + dateTime + "|"
                        + "|||||" + CR_STRING + ETX_STRING;

                RecordOldDepricated orderRecord = new RecordOldDepricated(text);
                orderRecords.add(orderRecord);
            } catch (InstrumentException ex) {
                throw new Immulite2000Exception(ex);
            }
        }

        return orderRecords;
    }

    private static RecordOldDepricated createTerminatorRecord1() throws Immulite2000Exception {
        try {
            String text = "L|1|N" + CR_STRING + ETX_STRING;
            RecordOldDepricated record = new RecordOldDepricated(text);
            return record;
        } catch (InstrumentException ex) {
            throw new Immulite2000Exception(ex);
        }
    }

    private static String replaceAllSpecialCharacters(String str) {
        str = str.replaceAll("\\|", "");
        str = str.replaceAll("\\\\", "");
        str = str.replaceAll("\\^", "");
        str = str.replaceAll("&", "");
        return str;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        byte[] rawBytes = textRecord.getBytes();
        for (int i = 0; i < rawBytes.length; i++) {
            byte c = rawBytes[i];
            strBuilder.append(getASCIICodeAsString(c));
        }
        return strBuilder.toString();
    }

    private RecordResultImmulite2000 getResultRecord1() {
        try {
            return new RecordResultImmulite2000(textRecord);
        } catch (InstrumentException ex) {
            Logger.getLogger(RecordOldDepricated.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
