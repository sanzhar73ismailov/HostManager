package cobas;

import instrument.ASCII;
import java.util.ArrayList;
import java.util.List;

public class DataLine {

    private String lineCode;
    private List<DataField> fields = new ArrayList<DataField>();

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public List<DataField> getFields() {
        return fields;
    }

    public void setFields(List<DataField> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(lineCode);
        for (DataField field : fields) {
            strBuilder.append(" ").append(field.getText());
        }
        strBuilder.append(ASCII.LF_STRING);
        return strBuilder.toString();
    }
}
