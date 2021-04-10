package entity;

public class InstrumentModel  implements HostDictionary {

    private int id;
    private String name;
    private String brand;
    private String type;
    private String comProtocol;
    private int sidSize;
    private boolean testCodeInt;

    public InstrumentModel() {
    }

    public InstrumentModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComProtocol() {
        return comProtocol;
    }

    public void setComProtocol(String comProtocol) {
        this.comProtocol = comProtocol;
    }

    public int getSidSize() {
        return sidSize;
    }

    public void setSidSize(int sidSize) {
        this.sidSize = sidSize;
    }

    public boolean isTestCodeInt() {
        return testCodeInt;
    }

    public void setTestCodeInt(boolean testCodeInt) {
        this.testCodeInt = testCodeInt;
    }

    @Override
    public String toString() {
        return "InstrumentModel{" + "id=" + id + ", name=" + name + ", brand=" + brand + ", type=" + type + ", comProtocol=" + comProtocol + ", sidSize=" + sidSize + ", testCodeInt=" + testCodeInt + '}';
    }

}
