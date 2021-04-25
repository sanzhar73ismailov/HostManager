package kz.biostat.lishostmanager.web.lab.servlet.navig;

//import com.oracle.webservices.api.message.PropertySet;
import kz.biostat.lishostmanager.comport.entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;

public class ParameterInsrumentForPage {

    private List<Parameter> listParameters = new ArrayList<>();
    private List<Instrument> listInstruments = new ArrayList<>();
    private Analysis analysis = null;
    private Instrument instrument = null;

    Model model;
    List<Instrument> allInstruments;
    List<Parameter> allParameters;
    int instrId;
    int analysisId;

    public ParameterInsrumentForPage(String instrStrId, String analysisStrId, Model model) throws ModelException {
        this.allInstruments = model.getInstruments();
        this.allParameters = model.getParameters();
        this.model = model;
        if (instrStrId != null && !instrStrId.isEmpty()) {
            instrId = Integer.parseInt(instrStrId);
            this.instrument = model.getObject(instrId, new Instrument());
        }

        if (analysisStrId != null && !analysisStrId.isEmpty()) {
            this.analysisId = Integer.parseInt(analysisStrId);
        }

        defineListInstrumentsAndListParameters();

    }

    private void defineListInstrumentsAndListParameters() throws ModelException {
        //Properties prop = new Properties();
            /*
         1. Сначала заполняем список нужных параметров
         */
        /* Проверяем задан ли анализ, если нет (analysisId == 0)*, проверяем задан ли прибор */
        if (analysisId == 0) {
            if (instrId != 0) {
                /* Если задан прибор, заполняем список нужных параметров (listParameters) теми, которые делаются на этом приборе*/
                for (Parameter param : allParameters) {
                    Test testDefault = param.getTestDefault();
                    if (testDefault != null && testDefault.getInstrument().getId() == instrId) {
                        this.listParameters.add(param);
                    }
                }
            } else {
                /* Если прибор не задан, заполняем список нужных параметров (listParameters) всеми параметрами*/
                this.listParameters = allParameters;
            }
        } else {
            /* Если анализ задан (analysisId != 0)*, получаем анализ */
            this.analysis = model.getObject(analysisId, new Analysis());
            //prop.setProperty("analysis_id", "=" + analysisId);

            if (instrId != 0) {
                /* Если задан прибор, заполняем список нужных параметров (listParameters) теми, которые делаются на этом приборе
                 для данного типа анализа
                 */
                for (Parameter param : allParameters) {
                    Test testDefault = param.getTestDefault();
                    if (testDefault != null
                            && testDefault.getInstrument().getId() == instrId
                            && param.getAnalysis().getId() == analysis.getId()) {

                        //System.out.println("testDefault = " + testDefault);
                        //System.out.println("testDefault.getParameter().getAnalysis() = " + testDefault.getParameter().getAnalysis());
                        this.listParameters.add(param);
                    }
                }
            } else {
                //this.listParameters = allParameters;
                for (Parameter param : allParameters) {
                    Test testDefault = param.getTestDefault();
                    if(param.getAnalysis() == null && param.getId() != 0) {
                        throw new RuntimeException("The parameter with id " + param.getId() + "has null in analysis_id column. Please check DB");
                    }
                    if (testDefault != null
                            && param.getAnalysis().getId() == analysis.getId()) {

                        //System.out.println("testDefault = " + testDefault);
                        //System.out.println("testDefault.getParameter().getAnalysis() = " + testDefault.getParameter().getAnalysis());
                        this.listParameters.add(param);
                    }
                }
            }

        }

        /*
         2. Теперь заполняем список нужных приборов
         */
        if (analysisId == 0 && instrId == 0) {
            this.listInstruments = this.allInstruments;
            /*
        } else if (analysisId == 0) {
            for (Instrument instr : allInstruments) {
                if (existInstrumentInTestsOfParams(instr, listParameters)) {
                    this.listInstruments.add(instr);
                }
            }
                    */
        } else {
            for (Instrument instr : allInstruments) {
                //if (existInstrumentInParams(instr, listParameters)) {
                if (existInstrumentInTestsOfParams(instr, listParameters)) {
                    this.listInstruments.add(instr);
                }
            }
        }
    }

    public List<Parameter> getListParameters() {
        return listParameters;
    }

    public List<Instrument> getListInstruments() {
        return listInstruments;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    private boolean existInstrumentInParams(Instrument instrument, List<Parameter> listParameter) {
        for (Parameter parameter : listParameter) {
            //System.out.println("parameter = " + parameter);
            //System.out.println("instrument = " + instrument);
            Test testDefault = parameter.getTestDefault();
            if (testDefault != null) {
                if (testDefault.getInstrument().getId() == instrument.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean existInstrumentInTestsOfParams(Instrument instrument, List<Parameter> listParameter) throws ModelException {
        for (Parameter parameter : listParameter) {
            //System.out.println("parameter = " + parameter);
            //System.out.println("instrument = " + instrument);
            Properties prop = new Properties();
            prop.setProperty("parameter_id", "=" + parameter.getId());
            List<Test> listTests = model.getObjects(prop, new Test());
            for (Test test : listTests) {
                if (test != null) {
                    if (test.getInstrument().getId() == instrument.getId()) {
                        return true;
                    }
                }
            }
//            Test testDefault = parameter.getTestDefault();
//            if (testDefault != null) {
//                if (testDefault.getInstrument().getId() == instrument.getId()) {
//                    return true;
//                }
//            }
        }
        return false;
    }
}
