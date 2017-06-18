package instrument.dimensionXpand;

import java.util.HashMap;
import java.util.Map;

public class DimensionErrorCodes {

    private DimensionErrorCodes() {
    }

    public static Map<Integer, DimensionErrorCode> mapErrorCodes;

    public static Map<Integer, DimensionErrorCode> getMapErrorCodes() {
        if (mapErrorCodes == null) {
            parseErrorCodesString();
        }
        return mapErrorCodes;
    }

    private static String getErrorCodesString() {
        String str = "1;NO;Temperature Out Of Range;The cuvette temperature was out of acceptable range.|"
                + "2;NO;Calibration Expired;The reagent cartridge lot for this test had an EXPIRED calibration status.|"
                + "3;NO;Assay Out Of Range;The result for this test was out of assay range defined for the linear method.|"
                + "4;NO;Absorbance;The photometric reading was out of acceptable range. These errors are also reported out as code.|"
                + "5;NO;Measurement System (noise, cuvette, etc.);During photometric measurement, the system detected some noise or variances in the absorbance.|"
                + "6;YES;Reagent QC;Assay is out of the established range for the specified method. See release notes.|"
                + "7;YES;Arithmetic Error;The result was not able to be calculated using the current coefficients for that method.|"
                + "8;YES;Never Calibrated;The reagent cartridge lot for this method was never calibrated.|"
                + "9;YES;No Reagent;The system lacked sufficient reagent for this test or a hydration of a reagent failed.|"
                + "10;YES;Aborted Test;A system action (by user or system) aborted this test. |"
                + "11;YES;Processing Error;A system processing error occurred that prevented the system from the determined result.|"
                + "12;YES;Software Error;Software error exists on the instrument.|"
                + "13;NO;Hemoglobin;The sample contained enough hemoglobin to interfere with system DBIL results. However, this will not affect the TBIL results.|"
                + "14;NO;Abnormal Reaction;Indicates the abnormal reaction conditions, i. e., foaming, air bubbles or turbidity problems are present in the mixture in the cuvette.|"
                + "15;NO;Diluted;The test has been autodiluted by the instrument.|"
                + "16;YES;Below Assay Range;Below current assay range for non-linear methods.|"
                + "17;YES;Above Assay Range;Above current assay range for non-linear methods.|"
                + "18;NO;HIL Detected;The amount of lipemia, hemolysis, or icterus in the specimen exceeded the threshold set in the software.|"
                + "19;YES;Clot Detected;A clot was detected when the probe attempted to aspirate sample for the test.";
        return str;
    }

    private static void parseErrorCodesString() {
        mapErrorCodes = new HashMap<>();
        String errorsStr = getErrorCodesString();
        String[] strArray = errorsStr.split("\\|");
        DimensionErrorCode errorObj;
        for (String strArray1 : strArray) {
            //System.out.println("strArray1 = " + strArray1);
            String[] parts = strArray1.split(";");
            int number = 0;
            boolean suppressRes = false;
            String error;
            String description = "";
            number = Integer.parseInt(parts[0]);
            suppressRes = parts[1].trim().equalsIgnoreCase("YES") ? true : false;
            error = parts[2].trim();
            description = parts[3].trim();
            errorObj = new DimensionErrorCode();
            errorObj.setNumber(number);
            errorObj.setSuppressResult(suppressRes);
            errorObj.setInterpretation(error);
            errorObj.setDescription(description);
            mapErrorCodes.put(number, errorObj);
//            if (errorObj.isSuppressResult()) {
//                System.out.println(errorObj);
//            }
        }

    }

}
