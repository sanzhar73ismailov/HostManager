package kz.biostat.lishostmanager.web.report;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 */
public class Header {

    String name = "ФИО, лет";
    List<String> values = new ArrayList<>();

    public Header(List<Integer> listInt) {
        for (int i = 0; i < listInt.size(); i++) {
            this.values.add(listInt.get(i).toString());
        }
    }

    public List<String> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
