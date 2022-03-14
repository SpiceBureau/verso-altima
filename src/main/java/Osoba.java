import java.util.ArrayList;

public class Osoba {

    public String name;
    public final ArrayList<Osoba> next;

    public Osoba(String name) {
        this.name = name;
        this.next = new ArrayList<Osoba>();
    }

    public void setNextNode(Osoba node) {
        this.next.add(node);
    }

    public ArrayList<Osoba> getNextNode() {
        return this.next;
    }

}