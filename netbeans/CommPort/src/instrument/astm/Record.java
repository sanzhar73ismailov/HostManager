package instrument.astm;

import instrument.ASCII;
import instrument.InstrumentException;

public class Record {

    protected TypeRecord typeRecord;
    protected String textRecord;

    public Record() {
    }

    public Record(String textRecord) throws InstrumentException {
        if (!textRecord.endsWith("\r")) {
            throw new InstrumentException("textRecord is not ends with <CR>: " + textRecord);
        }
        this.textRecord = textRecord;
        defineTypeRecord();
    }

    private void defineTypeRecord() throws instrument.InstrumentException {
        char typeRecChar = textRecord.toCharArray()[0];
        switch (typeRecChar) {
            case 'H':
                typeRecord = TypeRecord.HEAD;
                break;
            case 'Q':
                typeRecord = TypeRecord.QUERY;
                break;
            case 'P':
                typeRecord = TypeRecord.PATIENT_INFO;
                break;
            case 'O':
                typeRecord = TypeRecord.ORDER;
                break;
            case 'R':
                typeRecord = TypeRecord.RESULT;
                break;
            case 'L':
                typeRecord = TypeRecord.TERMINATION;
                break;
            case 'C':
                typeRecord = TypeRecord.COMMENT;
                break;
            default:
                throw new InstrumentException("Unknown type Record, the symbol: " + typeRecChar);
        }

    }

    public void setTextRecord(String textRecord) {
        this.textRecord = textRecord;
    }

    public TypeRecord getTypeRecord() {
        return typeRecord;
    }

    public String getTextRecord() {
        return textRecord;
    }

    @Override
    public String toString() {
        return "Record{" + "textRecord=" + ASCII.getStringWithAsciiCodes(textRecord) + '}';
    }

    protected String getFromArraIndexIfExist(String[] array, int index) {
        String field = "";
        if (index < array.length) {
            field = array[index];
        }
        return field;
    }

}
