package instrument.sysmexCA1500;

import instrument.ASCII;

public class BlockSysmexCA1500 {

    int number;
    int totalNumbers;
    String text;
    private Parameters parameters;

    public BlockSysmexCA1500(String text) {
        this.text = text;
        parameters = new Parameters(text);
        number = Integer.parseInt(parameters.blockNumber);
        totalNumbers = Integer.parseInt(parameters.totalNumberBlocks);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalNumbers() {
        return totalNumbers;
    }

    public void setTotalNumbers(int totalNumbers) {
        this.totalNumbers = totalNumbers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFinalBlock() {
        return  (totalNumbers != 0) && (number == totalNumbers);
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
    
    public String getBlockAsString(){
        String str = ASCII.getStringWithAsciiCodes(text);
        return str;
    }

}
