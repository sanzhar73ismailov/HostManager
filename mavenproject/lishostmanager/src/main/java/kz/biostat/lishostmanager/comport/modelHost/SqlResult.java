package kz.biostat.lishostmanager.comport.modelHost;

public class SqlResult {

    private String tableName;
    private String resultMessage;
    private String[] columns = new String[0];
    private String[][] dataTable = new String[0][];

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[][] getDataTable() {
        return dataTable;
    }

    public void setDataTable(String[][] dataTable) {
        this.dataTable = dataTable;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
