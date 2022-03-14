import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.*;

public class Zadatak {
    private static ArrayList<Osoba> peopleNodes = new ArrayList<Osoba>();
    private static ArrayList<String> peoplePrinted = new ArrayList<String>();
    private static Map<String, Integer> treeLengthByNode = new HashMap<>();
    private static boolean invalidRelationship = false;

    public static void main(String[] args) {
        writeToFile("", false);
        openFile("C:\\Users\\Dominik\\Desktop\\FER\\inputFile.txt"); //TODO add path to input file

        countTreeLevels(peopleNodes, 1);

        String minLevelNode = "";
        int indexCounter = -1;
        while (treeLengthByNode.keySet().size() > 0){ // 22-37 Sortanje liste peopleNodes po dubini stabla kako bi se prvo isprintala
            for (String key: treeLengthByNode.keySet()){ // najdublja stabla.
                if (minLevelNode.equals("")) minLevelNode = key;
                else {
                    if (treeLengthByNode.get(minLevelNode) > treeLengthByNode.get(key)) minLevelNode = key;
                }
            }
            treeLengthByNode.remove(minLevelNode);
            indexCounter += 1;
            for (Osoba person : peopleNodes){
                if (person.name.equals(minLevelNode)) {
                    Collections.swap(peopleNodes, indexCounter, peopleNodes.indexOf(person));
                }
            }
            minLevelNode = "";
        }
        if (!invalidRelationship) printHierarchy(peopleNodes, 1, false);
    }

    static void openFile(String pathName) {
        try {
            File inputFile = new File(pathName);

            Scanner myReader = new Scanner(inputFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                createHierarchy(peopleNodes, data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An input file error occurred.");
            e.printStackTrace();
        }
    }

    static void writeToFile(String data, boolean append){
        try {
            FileWriter myWriter = new FileWriter("outputFile.txt", append);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static void createHierarchy(ArrayList<Osoba> peopleNodes, String newRelationship){
        String[] parts = newRelationship.split(" ");

        String newChild = parts[0];
        String newParent = parts[1];

        Osoba newChildNode = null;
        Osoba newParentNode = null;
        
        for (Osoba o : peopleNodes){
            if (newParent.equals(o.name)) newParentNode = o; // provjere postoje li već imena newParent i newChild u peopleNodes
            if (newChild.equals(o.name)) newChildNode = o;
        }

        if (newParentNode == null){
            newParentNode = new Osoba(newParent);
            peopleNodes.add(newParentNode);

        }
        if (newChildNode == null){
            newChildNode = new Osoba(newChild);
            peopleNodes.add(newChildNode);
        }

        if (hasCycle(newChildNode, newParentNode)){ //provjera postoji li ciklični odnos A->B->C->A
            System.out.format("Invalid relationship: %s can't be a child of %s", newChildNode.name, newParentNode.name);
            invalidRelationship = true;
        }

        newParentNode.setNextNode(newChildNode);
    }

    static boolean hasCycle(Osoba child, Osoba parent){
        boolean flag = false;
        ArrayList<Osoba> childrenOfChild = child.getNextNode();
        for (Osoba person: childrenOfChild){
            if (person.name.equals(parent.name)) return true;
            if (!(person.getNextNode().size() == 0)){
                flag = hasCycle(person, parent);
            }
        }
        return flag;
    }

    static void countTreeLevels(ArrayList<Osoba> people, int level){
        for (Osoba person : people){
            if (treeLengthByNode.containsKey(person.name)) {
                if (level > treeLengthByNode.get(person.name)) treeLengthByNode.put(person.name, level);
            }
            else treeLengthByNode.put(person.name, level);
            if (person.getNextNode().size() > 0) countTreeLevels(person.getNextNode(), level + 1);
        }
    }

    static void printHierarchy(ArrayList<Osoba> people, int indents, boolean printAsChild){
        String spaces = String.format("%" + indents*4 + "s", "");
        for (Osoba person: people){
            if (printAsChild){
                writeToFile(spaces + person.name+"\n", true);
                peoplePrinted.add(person.name);
                ArrayList<Osoba> childrenOfPerson = person.getNextNode();
                printHierarchy(childrenOfPerson, indents+1, true);
            }
            else if (!peoplePrinted.contains(person.name)){
                writeToFile(spaces + person.name+"\n", true);
                peoplePrinted.add(person.name);
                ArrayList<Osoba> childrenOfPerson = person.getNextNode();
                printHierarchy(childrenOfPerson, indents+1, true);
            }
        }
    }
}
