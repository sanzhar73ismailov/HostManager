package kz.biostat.lishostmanager.web.report;

import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 * содержит количество проведенных тестов
 */
public class Bottom {
    private String name = "Итого";
    private List<Integer> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
    
    
}
