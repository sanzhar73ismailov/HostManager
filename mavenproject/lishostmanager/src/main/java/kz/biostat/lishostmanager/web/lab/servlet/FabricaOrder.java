package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.web.lab.servlet.navig.NavigatorException;

import static kz.biostat.lishostmanager.comport.instrument.Configurator.*;

public class FabricaOrder {

    public static CreateWorkOrder createWorkOrder(String instrumentAsStr) throws NavigatorException {
        CreateWorkOrder create = null;
        switch (instrumentAsStr) {
            case ADVIA2120:
                create = new CreateWorkOrderForAdvia2120();
                break;
            case COBASE411:
                create = new CreateWorkOrderForCobas411();
                break;
            case IMMULITE2000:
                create = new CreateWorkOrderForImmulite2000();
                break;
            case ADVIA_CENTAUR_CP:
                create = new CreateWorkOrderForAdviaCentaurCp();
                break;
            case DIMENSION_XPAND:
                create = new CreateWorkOrderForDimensionXPand();
                break;
            case SYSMEX_CA_660:
                create = new CreateWorkOrderForSysmexCa660();
                break;
            case VIVA_E:
                create = new CreateWorkOrderForVivaE();
                break;
            case SYSMEX_CA_1500:
                create = new CreateWorkOrderForVivaE();
                break;
            default:
                throw new NavigatorException("unknown instrument  (see FabricaOrder createWorkOrder() method)");
        }
        return create;
    }
}
