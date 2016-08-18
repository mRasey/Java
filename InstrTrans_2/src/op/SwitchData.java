package op;

public class SwitchData {
    public String tabName;
    public int hashValue;

    public SwitchData(String tabName, String hexValue) {
        this.tabName = tabName;
        this.hashValue = Integer.parseInt(hexValue.substring(2), 16);
    }

    public SwitchData(String tabName, int hashValue) {
        this.tabName = tabName;
        this.hashValue = hashValue;
    }
}
