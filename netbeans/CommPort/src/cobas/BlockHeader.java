package cobas;

import instrument.ASCII;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockHeader {

    private String lineCode = "09";
    private DataField field;
    private String blockCode;

   // 09_COBAS INTEGRA _04

    public BlockHeader(String blockCode) {
        this.blockCode = blockCode;
        try {
            field = new DataField(16, "INTEGRA 30-1051");
        } catch (Exception ex) {
            Logger.getLogger(BlockHeader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getLineCode() {
        return lineCode;
    }

    public DataField getField() {
        return field;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    @Override
    public String toString() {
        return lineCode + " " + field.getText() + " " + blockCode + ASCII.LF_STRING;
    }


    
    
    
    
    
    
    
}
