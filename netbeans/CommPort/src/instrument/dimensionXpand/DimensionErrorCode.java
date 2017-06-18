package instrument.dimensionXpand;

public class DimensionErrorCode {

    int number;
    boolean suppressResult;
    String interpretation;
    String description;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isSuppressResult() {
        return suppressResult;
    }

    public void setSuppressResult(boolean suppressResult) {
        this.suppressResult = suppressResult;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DimensionErrorCode{" + "number=" + number + ", suppressResult=" + suppressResult + ", interpretation=" + interpretation + ", description=" + description + '}';
    }

}
