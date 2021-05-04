import java.io.*;

import java.util.ArrayList;

class MapsOfLevels {
    static int levelIndex = 0;
    static ArrayList<ArrayList<char[][]>> list = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> listBests = new ArrayList<>();
    static int index = 0;
    static File folder = new File("C:\\Users\\admin\\IdeaProjects\\Project0203Bonus\\levels");
    static ArrayList<String> items = new ArrayList<>();

    MapsOfLevels() throws IOException {
        listMapsForFolder(folder);

    }

    public static void writeBest(File folder) throws IOException {
        int i = 0;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().endsWith(".best")) {
                FileWriter writer = new FileWriter(fileEntry);
                StringBuilder buffer = new StringBuilder();
                for (int j = 0; j < listBests.get(i).size(); j++) {
                    buffer.append(listBests.get(i).get(j)).append("\n");
                }
                writer.write(buffer.toString());
                writer.close();
                i++;
            }
        }
    }

    public static String[] getItems() {
        String[] temp = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            temp[i] = items.get(i);
        }
        return temp;
    }

    private void readMaps(File level) throws IOException {
        ArrayList<char[][]> list = new ArrayList<>();
        FileInputStream read = new FileInputStream(level);
        int k;
        StringBuilder s = new StringBuilder();
        while ((k = read.read()) != -1) {
            s.append((char) k);
        }
        String[] strs = s.toString().split("\\n");
        int counter = 0;
        for (int l = 0; l < strs.length; l++) {
            if (strs[l].equals(" "))
                continue;
            for (int j = 0; j < strs[l].length(); j++) {
                if (strs[l].charAt(j) == ';') {
                    char[][] map = new char[counter][];
                    for (int t = 0; t < map.length; t++) {
                        map[t] = new char[strs[l - counter].length()];
                        for (int y = 0; y < map[t].length; y++) {
                            map[t][y] = strs[l - counter].charAt(y);
                        }
                        counter--;
                    }
                    counter = 0;
                    list.add(map);
                    l += 2;
                    break;
                }
            }
            counter++;
        }
        MapsOfLevels.list.add(list);
    }

    private void listMapsForFolder(final File folder) throws IOException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().endsWith(".txt")) {
                readMaps(fileEntry);
                String nameWithoutEx = removeExtension(fileEntry);
                items.add(nameWithoutEx);
                File best = new File("C:\\Users\\admin\\IdeaProjects\\Project0203Bonus\\levels\\" + nameWithoutEx + ".best");
                if (best.createNewFile()) {
                    createBest(best);
                } else readBest(best);
            }
        }
    }

    private String removeExtension(File fileEntry) {
        String name = fileEntry.getName();
        int i = name.length() - 4;
        return name.substring(0, i);
    }

    private void createBest(File best) throws IOException {
        FileWriter writer = new FileWriter(best);
        writer.write("0");
    }

    private void readBest(File file) throws IOException {

        ArrayList<Integer> list = new ArrayList<>();
        FileInputStream read = new FileInputStream(file);
        int k;
        StringBuilder s = new StringBuilder();
        while ((k = read.read()) != -1) {
            s.append((char) k);
        }
        int results = this.list.get(this.list.size() - 1).size();
        String[] strs = s.toString().split("\\n");
        if (strs.length < results) {
            for (int i = 0; i < results; i++) {
                list.add(0);
            }
        } else {
            for (String str : strs) {
                list.add(Integer.parseInt(str));
            }
        }
        MapsOfLevels.listBests.add(list);
    }
}

