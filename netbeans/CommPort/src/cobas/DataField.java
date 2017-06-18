package cobas;

public class DataField {

    private final int length;
    private String text;

    public DataField(int length, String text) throws Exception {
        this.length = length;
        StringBuilder strBuilder = new StringBuilder(text);
        int difference = length - text.length();
        if (difference < 0) {
            throw new Exception("length of " + text + " must be not more " + length);
        }
        for (int i = 0; i < difference; i++) {
            strBuilder.append(" ");
        }
        this.text = strBuilder.toString();
    }

    public int getLength() {
        return length;
    }

    public String getText() {
        return text;
    }
    
    
    
//    public static void main(String[] args) throws Exception {
//        DataField df = new DataField(5, "qweaaa");
//        System.out.println("df = " + df.getText()+ "---");
//        
//    }

}
