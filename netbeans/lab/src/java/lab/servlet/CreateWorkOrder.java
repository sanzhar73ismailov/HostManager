package lab.servlet;

import entity.WorkOrder;
import java.util.List;
import javax.servlet.ServletRequest;

public interface CreateWorkOrder {

    WorkOrder parseRequestToWorkOrder(ServletRequest request);

    String validate(WorkOrder order, List<WorkOrder> listWorkOrders);

    String getPageForm();
}
