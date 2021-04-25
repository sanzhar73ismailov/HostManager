package kz.biostat.lishostmanager.web.report;

import kz.biostat.lishostmanager.comport.entity.Result;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

/**
 *
 * @author sanzhar.ismailov
 */
public class DoReport {

    private String instrument;
    private String date;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<Result> listResults;
    private InstrumentReport instrumentReport;
    private static final boolean TEST_MODE = false;
    private static final String separator = ";";

    public DoReport(String instrument, String date) {
        this.instrument = instrument;
        this.date = date;
    }

    public void makeReport() throws ParseException, ModelException {
        ModelImpl model = new ModelImpl();
        Date date = sdf.parse(this.date);
        listResults = model.getResultsByInstrumentAndDate(this.instrument, date);
        final List<Integer> uniqListParameters = getUniqListParameters(listResults);
        if (TEST_MODE) {
            System.out.println("uniqListParameters = " + uniqListParameters);
        }

        final List<String> uniqListBarcodes = getUniqBarcodes(listResults);
        if (TEST_MODE) {
            System.out.println("uniqListBarcodes = " + uniqListBarcodes);
        }
        instrumentReport = new InstrumentReport();
        Header header = new Header(uniqListParameters);
        instrumentReport.setHeader(header);
        List<Row> rows = new ArrayList<>();
        instrumentReport.setRows(rows);
        long cycles = 0L;
        Bottom bottom = new Bottom();
        instrumentReport.setBottom(bottom);
        List<Integer> listOfStringForBottom = new ArrayList<>();
        bottom.setValues(listOfStringForBottom);
        //заполняем listOfStringForBottom пустыми значениями
        for (Integer val : uniqListParameters) {
            listOfStringForBottom.add(0);
        }
        for (int i = 0; i < uniqListBarcodes.size(); i++) {
            String barcodeVersion = uniqListBarcodes.get(i);
            Row row = new Row();
            row.setBarcode(barcodeVersion);
            List<String> valuesForRow = new ArrayList<>();
            row.setValues(valuesForRow);

            for (int j = 0; j < uniqListParameters.size(); j++) {
                for (int k = 0; k < listResults.size(); k++) {
                    Result result = listResults.get(k);
                    cycles++;
                    String barcode = barcodeVersion.split("\\^")[0];
                    int version = 0;
                    try {
                        version = Integer.parseInt(barcodeVersion.split("\\^")[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("e = " + e);
                    }
                    if ((result.getSid().equals(barcode))
                            && (result.getParameterId() == uniqListParameters.get(j))
                            && result.getVersion() == version) {
                        valuesForRow.add(result.getValue());
                        int valOfBottomCell = listOfStringForBottom.get(j);
                        listOfStringForBottom.set(j, valOfBottomCell + 1);
                        break;
                    }
                    if (k == (listResults.size() - 1)) {
                        valuesForRow.add("");
                    }
                }
            }
            rows.add(row);
        }
        if (TEST_MODE) {
            System.out.println("cycles = " + cycles);
        }
        if (TEST_MODE) {
            System.out.println("--------------Getted rows START--------------------");
        }
        for (int i = 0; i < rows.size(); i++) {
            Row get = rows.get(i);
            if (TEST_MODE) {
                System.out.println("row = " + get);
            }
        }
        if (TEST_MODE) {
            System.out.println("--------------Getted rows END--------------------");
        }
        if (TEST_MODE) {
            System.out.println("--------------START to show bottom values--------------------");
        }
        final List<Integer> values = bottom.getValues();
        if (TEST_MODE) {
            System.out.println("values = " + values);
        }
        if (TEST_MODE) {
            System.out.println("--------------END to show bottom values--------------------");
        }
    }

    /**
     *
     * @return Возвращает отчет в виде CSV текста первая колонка - баркод строка
     * 1 - название колонок (коды параметров) строка последняя - подытоживающая
     * строка (сколько было сделано тестов по каждому параметру) промежуточные
     * строки - сами результаты
     */
    public String getReportAsCSVString() {
        StringBuilder stb = new StringBuilder();

        final Header header = this.instrumentReport.getHeader();
        final List<Row> rows = this.instrumentReport.getRows();
        final Bottom botom = this.instrumentReport.getBottom();

        //формирумем 1-ую строку (заголовок), 1-ячейка пустая
        final List<String> headerValues = header.getValues();
        stb.append(header.getName() + separator);
        for (int i = 0; i < headerValues.size(); i++) {
            String val = headerValues.get(i);
            stb.append(val);
            if (i == (headerValues.size() - 1)) {
                stb.append("\r\n");
                break;
            }
            stb.append(separator);
        }

        //формируем содержимое отчета значениями, первая колонка - баркод
        for (int i = 0; i < rows.size(); i++) {
            final Row row = rows.get(i);
            stb.append(row.getBarcode() + separator);
            final List<String> values = row.getValues();
            for (int j = 0; j < values.size(); j++) {
                String val = values.get(j);
                stb.append(val);
                if (j == (values.size() - 1)) {
                    stb.append("\r\n");
                    continue;
                }
                stb.append(separator);
            }
            //stb.append("\r\n");
        }

        //формируем подвал
        final List<Integer> values = botom.getValues();
        stb.append(botom.getName() + separator);
        for (int i = 0; i < values.size(); i++) {
            Integer val = values.get(i);
            stb.append(val);
            if (i == (values.size() - 1)) {
                //stb.append("\r\n");
                break;
            }
            stb.append(separator);
        }
        return stb.toString();
    }

    public static List<Integer> getUniqListParameters(List<Result> listResults) {
        List<Integer> uniqListParameters = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < listResults.size(); i++) {
            Result res = listResults.get(i);
            // пропускаем неизвестные (у который id параметра 0
            if (res.getParameterId() == 0) {
                continue;
            }
            set.add(res.getParameterId());
        }
        uniqListParameters.addAll(set);
        Collections.sort(uniqListParameters);
        return uniqListParameters;
    }

    public static List<String> getUniqBarcodes(List<Result> listResults) {
        List<String> uniqListBarcodes = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < listResults.size(); i++) {
            Result res = listResults.get(i);
            set.add(res.getSidVersion());
        }
        uniqListBarcodes.addAll(set);
        Collections.sort(uniqListBarcodes);
        return uniqListBarcodes;
    }

    /*
    public static void main(String[] args) {

        try {
            DoReport doReport = new DoReport("advia2120", "2016-04-01");
            doReport.makeReport();
            String csv = doReport.getReportAsCSVString();
            System.out.println("REPORT AS CSV START");
            System.out.println(csv);
            System.out.println("REPORT AS CSV FINISH");

        } catch (ParseException ex) {
            Logger.getLogger(DoReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ModelException ex) {
            Logger.getLogger(DoReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
}
