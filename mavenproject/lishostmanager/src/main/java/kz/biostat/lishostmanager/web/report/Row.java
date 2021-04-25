package kz.biostat.lishostmanager.web.report;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 */
public class Row {
    private String barcode;
    private List<String> values;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return   barcode + ": " + values;
    }
    
    
}
