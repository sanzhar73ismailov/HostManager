package kz.biostat.lishostmanager.comport.instrument.dimensionXpand;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProduceMessage {

    public MessageDimensionXpand resultAcceptanceMessage() {
        String str = "M" + FS_STRING + "A" + FS_STRING + FS_STRING;
        String chSumAsStr = MessageDimensionXpand.getCheckSumAsString(str.getBytes());
        String strForMessage = STX_STRING + str + chSumAsStr + ETX_STRING;
        MessageDimensionXpand msg = new MessageDimensionXpand(strForMessage.getBytes());
        return msg;
    }

    public MessageDimensionXpand resultRejectedMessage() {
        String str = "M" + FS_STRING + "R" + FS_STRING + 1 + FS_STRING;
        String chSumAsStr = MessageDimensionXpand.getCheckSumAsString(str.getBytes());
        String strForMessage = STX_STRING + str + chSumAsStr + ETX_STRING;
        MessageDimensionXpand msg = new MessageDimensionXpand(strForMessage.getBytes());
        return msg;
    }

    public MessageDimensionXpand noRequestMessage() {
        String str = STX_STRING + "N" + FS_STRING + "6A" + ETX_STRING;
        return new MessageDimensionXpand(str.getBytes());
    }

    public MessageDimensionXpand sampleRequestMessage(WorkOrder wo) throws DimensionXpandException {
        String strForMessage = getStrForOrderMessage(wo);
        String chSumAsStr = MessageDimensionXpand.getCheckSumAsString(strForMessage.getBytes());
        strForMessage = STX_STRING + strForMessage + chSumAsStr + ETX_STRING;
        MessageDimensionXpand msg = new MessageDimensionXpand(strForMessage.getBytes());
        return msg;
    }

    private ParamesForWorkOrder paramsFromWorkOrder(WorkOrder wo) throws DimensionXpandException {
        ParamesForWorkOrder params = new ParamesForWorkOrder();
        params.patientId = getMaxNumberSymbols(wo.getPatientName(), 27);
        params.sid = wo.getSid();
        if (params.sid.length() > 12) {
            throw new DimensionXpandException("sid is more than 12 symbols: " + params.sid);
        }

        params.sampleType = "1"; // 1- serum (в основном сыворотку используют)
        if (wo.getSampleType() != null && !wo.getSampleType().trim().isEmpty()) {
            params.sampleType = getSampleTypeFromWorkOrder(wo);
        }
        params.location = "";
        String[] testNames = wo.getTests().split(",");
        params.numberTests = testNames.length + "";
        params.testNames = testNames;
        return params;
    }

    String getMaxNumberSymbols(String str, int maxNumber) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        int length = str.length();
        if (length > maxNumber) {
            return str.substring(0, maxNumber);
        }
        return str;

    }

    private String getSampleTypeFromWorkOrder(WorkOrder wo) {
        String str = "1"; //serum
        //тип пробы: wb: whole blood, ser/pl: Blood Serum/Plasma, urine: urine, csf: Cerebral 
        switch (wo.getSampleType()) {
            case "wb":
                str = "W";
                break;
            case "ser":
                str = "1";
                break;
            case "pl":
                str = "2";
                break;
            case "urine":
                str = "3";
                break;
            case "csf":
                str = "4";
                break;
            default: {
                try {
                    throw new DimensionXpandException("unknown type of sample: " + wo.getSampleType());
                } catch (DimensionXpandException ex) {
                    Logger.getLogger(ProduceMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return str;
    }

    private String getStrForOrderMessage(WorkOrder wo) throws DimensionXpandException {
        ParamesForWorkOrder paramesForWorkOrder = paramsFromWorkOrder(wo);
        StringBuilder stb = new StringBuilder();
        stb.append(paramesForWorkOrder.type).append(FS_STRING);
        stb.append(paramesForWorkOrder.sampleCarrierID).append(FS_STRING);
        stb.append(paramesForWorkOrder.loadListID).append(FS_STRING);
        stb.append(paramesForWorkOrder.transaction).append(FS_STRING);
        stb.append(paramesForWorkOrder.patientId).append(FS_STRING);
        stb.append(paramesForWorkOrder.sid).append(FS_STRING);
        stb.append(paramesForWorkOrder.sampleType).append(FS_STRING);
        stb.append(paramesForWorkOrder.location).append(FS_STRING);
        stb.append(paramesForWorkOrder.priority).append(FS_STRING);
        stb.append(paramesForWorkOrder.numberOfCupsForSample).append(FS_STRING);
        stb.append(paramesForWorkOrder.cupPosition).append(FS_STRING);
        stb.append(paramesForWorkOrder.dilution).append(FS_STRING);
        stb.append(paramesForWorkOrder.numberTests).append(FS_STRING);
        for (String testName : paramesForWorkOrder.testNames) {
            stb.append(testName).append(FS_STRING);
        }
        return stb.toString();
    }
}

class ParamesForWorkOrder {

    public String type = "D";
    public String sampleCarrierID = "0";
    public String loadListID = "0";
    public String transaction = "A";
    public String patientId;
    public String sid;
    public String sampleType;
    public String location;
    public String priority = "0";
    public String numberOfCupsForSample = "1";
    public String cupPosition = "**"; // по документации номер рэка и позиции указывать нельзя (сам аппарат присваивает)
    public String dilution = "1";
    public String numberTests;
    public String[] testNames;
}
