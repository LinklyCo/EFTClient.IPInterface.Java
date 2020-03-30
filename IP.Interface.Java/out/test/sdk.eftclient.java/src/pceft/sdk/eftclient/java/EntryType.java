package pceft.sdk.eftclient.java;

public enum EntryType {
    Unknown(-1),

    Text(0),

    Numeric(1),

    Amount(2);

    public int entryType;
    EntryType(int i) {entryType = i;}
}
