package entity;

public class Instrument implements HostDictionary {

    private int id;
    private String name;
    private InstrumentModel model;
    private String ip;
    private int port;
    private ModeWorking mode;
    private boolean active;
    private boolean runNow;
    private boolean testMode;

    public enum ModeWorking {

        BATCH, QUERY, UNKNOWN
    }

    public Instrument() {
    }

    public Instrument(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InstrumentModel getModel() {
        return model;
    }

    public void setModel(InstrumentModel model) {
        this.model = model;
    }

    public ModeWorking getMode() {
        return mode;
    }

    public void setMode(ModeWorking mode) {
        this.mode = mode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRunNow() {
        return instrument.InstrumentIndicator.getInstance().isRun(this.id);
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    @Override
    public String toString() {
        return "Instrument{" + "id=" + id + ", name=" + name + ", model=" + model + ", ip=" + ip + ", port=" + port + ", mode=" + mode + ", active=" + active + ", runNow=" + runNow + ", testMode=" + testMode + '}';
    }
    
    

    
  
    
    
}
