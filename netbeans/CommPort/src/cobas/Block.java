package cobas;

import instrument.ASCII;

public class Block {

    private BlockHeader blockHeader;
    private BlockData blockData;
    private BlockCheck blockCheck;
    public static final String START_HEADER = new String(new byte[]{ASCII.SOH, ASCII.LF});
    public static final String START_BLOCK_DATA = new String(new byte[]{ASCII.STX, ASCII.LF});
    public static final String START_BLOCK_CHECK = new String(new byte[]{ASCII.ETX, ASCII.LF});
    public static final String END_BLOCK_CHECK = new String(new byte[]{ASCII.EOT, ASCII.LF});

    public Block(BlockHeader blockHeader, BlockData blockData, BlockCheck blockCheck) {
        this.blockHeader = blockHeader;
        this.blockData = blockData;
        this.blockCheck = blockCheck;
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public BlockCheck getBlockCheck() {
        return blockCheck;
    }

    public int getCounter() {
        return blockCheck.getCounter();
    }

    public synchronized int getCheckSum() {
        return blockCheck.getCheckSum();
    }

    @Override
    public String toString() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<<<<<BLOCK START>>>>>>>>\n");
        strBuilder.append("<header>\n");
        strBuilder.append(replaceByLetters(blockHeader.toString()));
        strBuilder.append("</header>\n");
        strBuilder.append("<data>\n");
        strBuilder.append(replaceByLetters(blockData.toString()));
        strBuilder.append("</data>\n");
        strBuilder.append("<check>\n");
        strBuilder.append(blockCheck.getCounter()+"<LF>\n");
        strBuilder.append(blockCheck.getCheckSum()+"<LF>\n");
        strBuilder.append("</check>\n");
        strBuilder.append("<<<<<BLOCK END>>>>>>>>\n");
        return strBuilder.toString();

    }

    private String replaceByLetters(String str) {
        // String repl = strBuilder.toString();
        str = str.replaceAll(ASCII.SOH_STRING, "<SOH>");
        str = str.replaceAll(ASCII.LF_STRING, "<LF>\n");
        str = str.replaceAll(ASCII.EOT_STRING, "<EOT>");
        str = str.replaceAll(ASCII.ETX_STRING, "<ETX>");
        str = str.replaceAll(ASCII.STX_STRING, "<STX>");
        return str;
    }
}
