package instrument;

import java.util.Map;

public class Settings {

    private Settings() {
    }
    
    
   public static  Map<String, String> map;
    static {
        map = InstrumentIndicator.getInstance().getMapSettings();
    }

    public static boolean isBarcodeUsed() throws NoSuchSetting {
        String key = "barcode"; 
        checkISExistSettings(key);
        return map.get(key).equals("1");
    }

    public static boolean checkISExistSettings(String key) throws NoSuchSetting {
        Map<String, String> map = InstrumentIndicator.getInstance().getMapSettings();
        if (!map.containsKey(key)) {
            throw new NoSuchSetting(key);
        }
        return true;
    }

//    public static void main(String[] args) throws NoSuchSetting {
//        boolean res = isBarcodeUsed();
//        System.out.println("res = " + res);
//    }
}
