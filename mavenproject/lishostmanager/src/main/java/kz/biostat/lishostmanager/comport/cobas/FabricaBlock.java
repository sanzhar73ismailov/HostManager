package kz.biostat.lishostmanager.comport.cobas;

public class FabricaBlock {
 private static long generalCounter;
    public static  Block createBlock(String code) {
        generalCounter++;
        Block block = null;
        if (code.equals("00")) {
            BlockHeader blockHeader = new BlockHeader("00"); // 00 - Idle Block
            BlockData blockData = new BlockData();
            BlockCheck blockCheck = new BlockCheck(blockHeader, blockData);

            block = new Block(blockHeader, blockData, blockCheck);
        }
        return block;
    }

    public static long getGeneralCounter() {
        return generalCounter;
    }
    
    
}
