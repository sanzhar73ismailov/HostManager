package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.util.List;

public class FLFunctions {

    public static String getWorkOrderProperty(WorkOrder order, String property) {
        return order.getAddParams().getProperty(property);
    }

    public static String getSelected(String propertyValueOfBean, String constant) {
        if (propertyValueOfBean != null && !propertyValueOfBean.trim().isEmpty() && propertyValueOfBean.equals(constant)) {
            return "selected";
        }
        return "";
    }

    public static String getValueFromDic(List dic, String key) {
        for (Object obj : dic) {
            Test elem = (Test) obj;
            if (elem.getCode().equals(key)) {
                return elem.getName();
            }
        }
        return "";
    }
}
