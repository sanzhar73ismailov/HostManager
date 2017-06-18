package lab.servlet;

import lab.servlet.navig.NavigatorException;

public class FabricaOrder {

    public static CreateWorkOrder createWorkOrder(String instrumentAsStr) throws NavigatorException {
        CreateWorkOrder create = null;
        switch (instrumentAsStr) {
            case instrument.Configurator.ADVIA2120:
                create = new CreateWorkOrderForAdvia2120();
                break;
            case instrument.Configurator.COBASE411:
                create = new CreateWorkOrderForCobas411();
                break;
            case instrument.Configurator.IMMULITE2000:
                create = new CreateWorkOrderForImmulite2000();
                break;
            case instrument.Configurator.ADVIA_CENTAUR_CP:
                create = new CreateWorkOrderForAdviaCentaurCp();
                break;
            case instrument.Configurator.DIMENSION_XPAND:
                create = new CreateWorkOrderForDimensionXPand();
                break;
            case instrument.Configurator.SYSMEX_CA_660:
                create = new CreateWorkOrderForSysmexCa660();
                break;
            case instrument.Configurator.VIVA_E:
                create = new CreateWorkOrderForVivaE();
                break;
            case instrument.Configurator.SYSMEX_CA_1500:
                create = new CreateWorkOrderForVivaE();
                break;
            default:
                throw new NavigatorException("unknown instrument  (see FabricaOrder createWorkOrder() method)");
        }
        return create;
    }
}
