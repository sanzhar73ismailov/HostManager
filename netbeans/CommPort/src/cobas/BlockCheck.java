package cobas;

import instrument.ASCII;

public class BlockCheck {

    
    int counter;
    int checkSum;
    BlockHeader header;
    BlockData data;
    private static int MODUL_FOR_CHECK_SUM = 1000;
   

    public BlockCheck(BlockHeader header, BlockData data) {
         counter = (int) (FabricaBlock.getGeneralCounter() % 2);
        this.header = header;
        this.data = data;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public synchronized int getCheckSum() {
        byte[] bytesOfBlock = toString().getBytes();
        for (byte b : bytesOfBlock) {
            checkSum += b;
        }
        checkSum = checkSum % MODUL_FOR_CHECK_SUM;
        return checkSum;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        
        toReturn.append(Block.START_HEADER);
        toReturn.append(header.toString());
        toReturn.append(Block.START_BLOCK_DATA);
        toReturn.append(data.toString());
        toReturn.append(Block.START_BLOCK_CHECK);
        toReturn.append(String.valueOf(counter));
        toReturn.append(ASCII.LF_STRING);
        return toReturn.toString();
    }
}
