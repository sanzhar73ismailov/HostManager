package kz.biostat.lishostmanager.web.lab.servlet.navig;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.instrument.*;

import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.biostat.lishostmanager.web.lab.servlet.TimerSessionBean;
import kz.biostat.lishostmanager.comport.modelHost.Model;

public class PageStartStopApparatus extends AbstractPageNavigator {

    public PageStartStopApparatus(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        this.pageTo = "listInstruments.jsp";
        String action = request.getParameter("action");
        InstrumentIndicator instrIndicator = InstrumentIndicator.getInstance();
        List<Instrument> listInstruments = null;
        if (action != null) {
            try {
                if (action.equals("startAllActive")) {
                    TimerSessionBean timerSessionBean = new TimerSessionBean();
                    timerSessionBean.startAllInstruments();
                } else {
                    int instrId = Integer.parseInt(request.getParameter("instrument"));
                    final Instrument instrObj = model.getObject(instrId, new Instrument());
                    DriverInstrument driver = null;
                    if (!instrObj.isActive()) {
                        request.setAttribute("message", instrObj.getName() + " is not active");
                    } else {
                        switch (action) {
                            case "start":
                            switch (instrObj.getModel().getName()) {
                                case Configurator.COBASE411:
                                    driver = new DriverCobas411(instrObj, model);
                                    break;
                                case Configurator.ADVIA2120:
                                    driver = new DriverAdvia2120(instrObj, model);
                                    break;
                                case Configurator.DIMENSION_XPAND:
                                    driver = new DriverDimensionXpand(instrObj, model);
                                    break;
                                case Configurator.IMMULITE2000:
                                    driver = new DriverImmulite2000(instrObj, model);
                                    break;
                                case Configurator.SYSMEX_CA_660:
                                    driver = new DriverSysmexCa660(instrObj, model);
                                    break;
                                case Configurator.ADVIA_CENTAUR_CP:
                                    driver = new DriverAdviaCentaurCP(instrObj, model);
                                    break;
                                case Configurator.VIVA_E:
                                    driver = new DriverVivaE(instrObj, model);
                                    break;
                                case Configurator.SYSMEX_CA_1500:
                                    driver = new DriverSysmexCa1500(instrObj, model);
                                    break;
                                case Configurator.MINDRAY_BC_3000:
                                    driver = new DriverMindrayBC3000(instrObj, model);
                                    break;
                                case Configurator.SYSMEX_XS_500_i:
                                    driver = new DriverSysmexXS500i(instrObj, model);
                                    break;
                                default:
                                    throw new UnsupportedOperationException("see " + this.getClass().getName());
                            }

                                if (instrIndicator.isStopped(instrObj.getId())) {
                                    InstrumentIndicator.getInstance().startInstrument(instrObj.getId());
                                    driver.mainRunInNewThread();
                                } else {
                                    driver.logIsAlredyRun();
                                }
                                break;


                            case "stop":
                                instrIndicator.stopInstrument(instrObj.getId());
                                break;

                            default:
                                throw new NavigatorException("parameter action must be start or stop, but it is: " + action);
                        }
                    }
                }

                listInstruments = model.getObjects(new Properties(), new Instrument());
                request.setAttribute("listInstruments", listInstruments);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new NavigatorException(ex);
            }
        } else {
            throw new NavigatorException("parameter action is null");
        }

    }

}
