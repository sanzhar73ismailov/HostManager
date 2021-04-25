package kz.biostat.lishostmanager.comport.cobas;

import java.util.ArrayList;
import java.util.List;

public class BlockData {

    private List<DataLine> lines = new ArrayList<DataLine>();



    public List<DataLine> getLines() {
        return lines;
    }

    public void setLines(List<DataLine> lines) {
        this.lines = lines;
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (DataLine dataLine : lines) {
            toReturn.append(dataLine.toString());
        }
        return toReturn.toString();
    }
}
