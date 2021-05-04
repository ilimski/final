import java.io.IOException;
import java.util.ArrayList;

class RobotModel {
    int modelX;
    int modelY;
    char[][] currentMap;
    boolean[][] isUsed;
    boolean isUndo;
    int dir, counter = 0, moves = 0, indexMoves = 0;
    private ArrayList<char[][]> stagesOfRobot = new ArrayList<>();
    private ArrayList<Integer> stageOfControllers = new ArrayList<>();
    private ArrayList<boolean[][]> stageOfRedBoxes = new ArrayList<>();
    private ArrayList<Integer> corRobotX = new ArrayList<>();
    private ArrayList<Integer> corRobotY = new ArrayList<>();
    static MapsOfLevels maps;
    int maxLength;

    {
        try {
            maps = new MapsOfLevels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    RobotModel() {
        char[][] map = MapsOfLevels.list.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index);
        currentMap = copyMap(map);
        for (int i = 0; i < map.length; i++) {
            if (map[i].length > maxLength)
                maxLength = map[i].length;
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '@') {
                    modelX = j;
                    modelY = i;
                } else if (map[i][j] == '+') {
                    modelX = j;
                    modelY = i;
                    isUsed[i][j] = true;
                }
                if (map[i][j] == '.') {
                    counter++;
                }
            }
        }
    }

    void moveUp() {
        boolean isPossibleToMove = true;
        if (modelY > 0) {
            if (currentMap[modelY - 1][modelX] != '#') {
                if (currentMap[modelY - 1][modelX] == '$') {
                    if (modelY - 2 >= 0) {
                        if (currentMap[modelY - 2][modelX] == '#' || currentMap[modelY - 2][modelX] == '$')
                            isPossibleToMove = false;
                        else {
                            if (isCircle(modelY - 2, modelX)) {
                                isUsed[modelY + 2][modelX] = true;
                                counter--;
                            } else if (isUsed[modelY - 1][modelX]) {
                                currentMap[modelY - 1][modelX] = '.';
                                counter++;
                            }
                            currentMap[modelY - 2][modelX] = '$';
                        }
                    }
                }
            } else
                isPossibleToMove = false;
            if (isPossibleToMove) {
                if (!isCircle(modelY - 1, modelX))
                    if (!isUsed[modelY - 1][modelX])
                        currentMap[modelY - 1][modelX] = '@';
                    else {
                        isUsed[modelY - 1][modelX] = false;
                        currentMap[modelY - 1][modelX] = '.';
                    }
                if (!isCircle(modelY, modelX) && !isUsed[modelY][modelX])
                    currentMap[modelY][modelX] = ' ';
                else {
                    currentMap[modelY][modelX] = '.';
                    isUsed[modelY][modelX] = false;
                }
                modelY -= 1;
                moves++;
                dir = 1;
            }
        }
    }

    void moveDown() {
        boolean isPossibleToMove = true;
        if (modelY < currentMap.length - 1) {
            if (currentMap[modelY + 1][modelX] != '#') {
                if (currentMap[modelY + 1][modelX] == '$') {
                    if (modelY + 2 <= currentMap.length - 1) {
                        if (currentMap[modelY + 2][modelX] == '#' || currentMap[modelY + 2][modelX] == '$')
                            isPossibleToMove = false;
                        else {
                            if (isCircle(modelY + 2, modelX)) {
                                isUsed[modelY + 2][modelX] = true;
                                counter--;
                            } else if (isUsed[modelY + 1][modelX]) {
                                currentMap[modelY + 1][modelX] = '.';
                                counter++;
                            }
                            currentMap[modelY + 2][modelX] = '$';
                        }
                    }
                }
            } else
                isPossibleToMove = false;
            if (isPossibleToMove) {
                if (!isCircle(modelY + 1, modelX))
                    if (!isUsed[modelY + 1][modelX])
                        currentMap[modelY + 1][modelX] = '@';
                    else {
                        isUsed[modelY + 1][modelX] = false;
                        currentMap[modelY + 1][modelX] = '.';
                    }
                if (!isCircle(modelY, modelX) && !isUsed[modelY][modelX])
                    currentMap[modelY][modelX] = ' ';
                else {
                    currentMap[modelY][modelX] = '.';
                    isUsed[modelY][modelX] = false;
                }
                modelY += 1;
                moves++;
                dir = 3;
            }
        }
    }

    void moveLeft() {
        boolean isPossibleToMove = true;
        if (modelX > 0) {
            if (currentMap[modelY][modelX - 1] != '#') {
                if (currentMap[modelY][modelX - 1] == '$') {
                    if (modelX - 2 >= 0) {
                        if (currentMap[modelY][modelX - 2] == '#' || currentMap[modelY][modelX - 2] == '$')
                            isPossibleToMove = false;
                        else {
                            if (isCircle(modelY, modelX - 2)) {
                                isUsed[modelY][modelX - 2] = true;
                                counter--;
                            } else if (isUsed[modelY][modelX - 1]) {
                                currentMap[modelY][modelX - 1] = '.';
                                counter++;
                            }
                            currentMap[modelY][modelX - 2] = '$';
                        }
                    }
                }
            } else
                isPossibleToMove = false;
            if (isPossibleToMove) {
                if (!isCircle(modelY, modelX - 1))
                    if (!isUsed[modelY][modelX - 1])
                        currentMap[modelY][modelX - 1] = '@';
                    else {
                        isUsed[modelY][modelX - 1] = false;
                        currentMap[modelY][modelX - 1] = '.';
                    }

                if (!isCircle(modelY, modelX) && !isUsed[modelY][modelX])
                    currentMap[modelY][modelX] = ' ';
                else {
                    currentMap[modelY][modelX] = '.';
                    isUsed[modelY][modelX] = false;
                }
                modelX -= 1;
                moves++;
                dir = 0;
            }
        }
    }

    private boolean isCircle(int modelY, int modelX) {
        return currentMap[modelY][modelX] == '.';
    }

    void moveRight() {
        boolean isPossibleToMove = true;
        if (modelX < currentMap[modelY].length - 1) {
            if (currentMap[modelY][modelX + 1] != '#') {
                if (currentMap[modelY][modelX + 1] == '$') {
                    if (modelX + 2 <= currentMap[modelY].length - 1) {
                        if (currentMap[modelY][modelX + 2] == '#' || currentMap[modelY][modelX + 2] == '$')
                            isPossibleToMove = false;
                        else {
                            if (isCircle(modelY, modelX + 2)) {
                                isUsed[modelY][modelX + 2] = true;
                                counter--;
                            } else if (isUsed[modelY][modelX + 1]) {
                                currentMap[modelY][modelX + 1] = '.';
                                counter++;
                            }
                            currentMap[modelY][modelX + 2] = '$';
                        }
                    }
                }
            } else
                isPossibleToMove = false;
            if (isPossibleToMove) {
                if (!isCircle(modelY, modelX + 1))
                    if (!isUsed[modelY][modelX + 1])
                        currentMap[modelY][modelX + 1] = '@';
                    else {
                        isUsed[modelY][modelX + 1] = false;
                        currentMap[modelY][modelX + 1] = '.';
                    }
                if (!isCircle(modelY, modelX) && !isUsed[modelY][modelX])
                    currentMap[modelY][modelX] = ' ';
                else {
                    currentMap[modelY][modelX] = '.';
                    isUsed[modelY][modelX] = false;
                }
                modelX += 1;
                moves++;
                dir = 2;
            }
        }
    }

    private char[][] copyMap(char[][] array) {
        char[][] temp = new char[array.length][];
        isUsed = new boolean[array.length][];
        for (int i = 0; i < array.length; i++) {
            temp[i] = new char[array[i].length];
            isUsed[i] = new boolean[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                temp[i][j] = array[i][j];
                isUsed[i][j] = false;
            }
        }
        return temp;
    }

    void copyCurrentStageOfLevel() {
        char[][] temp = new char[currentMap.length][];
        boolean[][] isUsed = new boolean[this.isUsed.length][];
        for (int i = 0; i < currentMap.length; i++) {
            temp[i] = new char[currentMap[i].length];
            isUsed[i] = new boolean[this.isUsed[i].length];
            for (int j = 0; j < currentMap[i].length; j++) {
                temp[i][j] = currentMap[i][j];
                isUsed[i][j] = this.isUsed[i][j];
            }
        }
        corRobotX.add(modelX);
        corRobotY.add(modelY);
        stagesOfRobot.add(temp);
        stageOfControllers.add(counter);
        stageOfRedBoxes.add(isUsed);
    }

    void redoMove() {
        indexMoves--;
        if (moves - indexMoves < stageOfRedBoxes.size() && isUndo) {
            currentMap = stagesOfRobot.get(moves - indexMoves);
            isUsed = stageOfRedBoxes.get(moves - indexMoves);
            counter = stageOfControllers.get(moves - indexMoves);
            modelX = corRobotX.get(moves - indexMoves);
            modelY = corRobotY.get(moves - indexMoves);
        } else {
            indexMoves++;
        }

    }

    void undoMove() {
        indexMoves++;
        if (moves - indexMoves >= 0) {
            currentMap = stagesOfRobot.get(moves - indexMoves);
            isUsed = stageOfRedBoxes.get(moves - indexMoves);
            counter = stageOfControllers.get(moves - indexMoves);
            modelX = corRobotX.get(moves - indexMoves);
            modelY = corRobotY.get(moves - indexMoves);
            isUndo = true;
        } else {
            indexMoves--;
        }
    }
}