package instrument;

public class NoSuchSetting extends InstrumentException {

    String key;

    public NoSuchSetting(String key) {
        super("No such key in system " + key);
        this.key = key;
    }

}
