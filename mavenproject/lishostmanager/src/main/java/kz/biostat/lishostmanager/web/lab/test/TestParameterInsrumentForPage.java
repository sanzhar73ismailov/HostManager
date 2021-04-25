package kz.biostat.lishostmanager.web.lab.test;

import kz.biostat.lishostmanager.comport.entity.*;
import java.util.List;
import kz.biostat.lishostmanager.web.lab.servlet.navig.ParameterInsrumentForPage;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class TestParameterInsrumentForPage {

    public static void main(String[] args) throws ModelException {
        Model model = new ModelImpl();
        String analysisId = "1";
        String instrStrId = "3";
        ParameterInsrumentForPage parameterInsrumentForPage = new ParameterInsrumentForPage(instrStrId, analysisId, model);
        List<Parameter> listParam = parameterInsrumentForPage.getListParameters();
        List<Instrument> listInstr = parameterInsrumentForPage.getListInstruments();
        Analysis analysis = parameterInsrumentForPage.getAnalysis();
        Instrument instrument = parameterInsrumentForPage.getInstrument();

        System.out.println("analysisId = " + analysisId);
        System.out.println("instrStrId = " + instrStrId);
        System.out.println("List param size: " + listParam.size());
        System.out.println("\n**********List param: ");
        int i = 0;
        for (Parameter param1 : listParam) {
            System.out.println("param1 = " + i++ + ", " + param1.getId() + ", " + param1.getName());
        }
        i = 0;
        System.out.println("\n<<<<<<<<<<List instr: ");
        for (Instrument instr1 : listInstr) {
            // System.out.println("instr1 = " + instr1);
            System.out.println("instr1 = " + i++ + ", " + instr1.getId() + ", " + instr1.getName());
        }
    }
}
