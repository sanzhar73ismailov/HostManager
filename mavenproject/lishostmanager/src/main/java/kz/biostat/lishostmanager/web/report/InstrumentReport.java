package kz.biostat.lishostmanager.web.report;

import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 */
public class InstrumentReport {
    private Header header;
    private List<Row> rows;
    private Bottom bottom;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Bottom getBottom() {
        return bottom;
    }

    public void setBottom(Bottom bottom) {
        this.bottom = bottom;
    }


    
    
}
