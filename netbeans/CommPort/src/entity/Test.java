package entity;

public class Test implements Comparable<Test>, HostDictionary {

    private int id;
    private Instrument instrument;
    private String code;
    private String name;
    private String description;
    private String units;
    private int order = 1000;
    private Parameter parameter;
    //private boolean useDefault;

    public Test() {
    }

    public Test(String code, String name) {
        this(0, null, code, name, null, null, 1000);
    }

    public Test(int id, Instrument instrument, String code, String name, String description, String units, int order) {
        this.id = id;
        this.instrument = instrument;
        this.code = code;
        this.name = name;
        this.description = description;
        this.units = units;
        this.order = order;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }


    @Override
    public String toString() {
        return "Test{" + "id=" + id + ", instrument=" + instrument + ", code=" + code + ", name=" + name + ", description=" + description + ", units=" + units + ", order=" + order + ", parameter=" + parameter + '}';
    }

    public String getHtml() {
        StringBuilder stb = new StringBuilder();
        stb.append("<table style='border-collapse: collapse;width: 500px'><tr><td>");
        appendInTable(stb, "id", id + "");
        appendInTable(stb, ", code", code);
        appendInTable(stb, ", name", name);
        appendInTable(stb, ", description", description);
        appendInTable(stb, ", instrument", instrument.getId() + "");
        stb.append("</td></tr></table>");
        return stb.toString();
    }

    private void appendInTable(StringBuilder stb, String label, String value) {
        stb.append("").append(label).append(": ").append(value).append("");
    }

    //@Override
    public int compareTo(Test tOther) {
        String thisInstName = null;
        if (this.instrument != null) {
            thisInstName = this.instrument.getName();
        }
        String otherInstrName = null;
        if (tOther.getInstrument() != null) {
            otherInstrName = tOther.getInstrument().getName();
        }

        if (thisInstName == null && otherInstrName == null) {
            return this.id - tOther.id;
        } else if (thisInstName != null && otherInstrName == null) {
            return -1;
        } else if (thisInstName == null && otherInstrName != null) {
            return 1;
        } else {
            if (thisInstName.equals(otherInstrName)) {
                return this.id - tOther.id;
            } else {
                return thisInstName.compareTo(otherInstrName);
            }
        }
    }
}
