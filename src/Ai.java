import java.util.ArrayList;
import java.util.Random;

public class Ai {
    private int[] zeros = {0, 0, 0, 0};

    /**
     * 分数
     */
    int score;

    private Random r = new Random();

    // 可变参数begin
    double EI = 0.22526476022378505;
    double CI = 1.0206;

    private static final int TRY_TIMES_PER_MOVE = 1600;// 尝试次数

    int STEPS = 18;// 尝试深度

    /**
     * 计算可以移动的方向候选
     *
     * @param d
     * @return
     */
    private boolean[] candidates(int[][] d) {
        boolean candidates[] = new boolean[4];
        candidates[0] = checkUp(d);
        candidates[1] = checkDown(d);
        candidates[2] = checkLeft(d);
        candidates[3] = checkRight(d);
        return candidates;
    }

    /**
     * 判断是否可以上移
     *
     * @param d
     * @return
     */
    private boolean checkUp(int[][] d) {
        for (int j = 0; j < 4; j++) {
            // 可合并
            for (int i = 0; i < 3; i++) {
                if (d[i][j] == d[i + 1][j] && d[i][j] != 0) {
                    return true;
                }
            }

            // 有空位
            if (d[0][j] == 0) {
                if (d[1][j] + d[2][j] + d[3][j] != 0) {
                    return true;
                }
                continue;
            }
            if (d[1][j] == 0) {
                if (d[2][j] + d[3][j] != 0) {
                    return true;
                }
                continue;
            }
            if (d[2][j] == 0) {
                if (d[3][j] != 0) {
                    return true;
                }
                continue;
            }

        }
        return false;
    }

    /**
     * 判断是否可以下移
     *
     * @param d
     * @return
     */
    private boolean checkDown(int[][] d) {
        for (int j = 0; j < 4; j++) {
            // 可合并
            for (int i = 0; i < 3; i++) {
                if (d[i][j] == d[i + 1][j] && d[i][j] != 0) {
                    return true;
                }
            }

            // 有空位
            if (d[3][j] == 0) {
                if (d[0][j] + d[1][j] + d[2][j] != 0) {
                    return true;
                }
                continue;
            }
            if (d[2][j] == 0) {
                if (d[0][j] + d[1][j] != 0) {
                    return true;
                }
                continue;
            }
            if (d[1][j] == 0) {
                if (d[0][j] != 0) {
                    return true;
                }
                continue;
            }

        }
        return false;

    }

    /**
     * 判断是否可以左移
     *
     * @param d
     * @return
     */
    private boolean checkLeft(int[][] d) {
        for (int i = 0; i < 4; i++) {
            // 可合并
            for (int j = 0; j < 3; j++) {
                if (d[i][j] == d[i][j + 1] && d[i][j] != 0) {
                    return true;
                }
            }

            // 有空位
            if (d[i][0] == 0) {
                if (d[i][1] + d[i][2] + d[i][3] != 0) {
                    return true;
                }
                continue;
            }
            if (d[i][1] == 0) {
                if (d[i][2] + d[i][3] != 0) {
                    return true;
                }
                continue;
            }
            if (d[i][2] == 0) {
                if (d[i][3] != 0) {
                    return true;
                }
                continue;
            }

        }
        return false;

    }

    /**
     * 判断是否可以右移
     *
     * @param d
     * @return
     */
    private boolean checkRight(int[][] d) {
        for (int i = 0; i < 4; i++) {
            // 可合并
            for (int j = 0; j < 3; j++) {
                if (d[i][j] == d[i][j + 1] && d[i][j] != 0) {
                    return true;
                }
            }

            // 有空位
            if (d[i][3] == 0) {
                if (d[i][0] + d[i][1] + d[i][2] != 0) {
                    return true;
                }
                continue;
            }
            if (d[i][2] == 0) {
                if (d[i][0] + d[i][1] != 0) {
                    return true;
                }
                continue;
            }
            if (d[i][1] == 0) {
                if (d[i][0] != 0) {
                    return true;
                }
                continue;
            }
        }
        return false;

    }

    /**
     * 往方向移动（local）
     *
     * @param i
     * @param d
     * @return
     */
    private int move(int i, int[][] d) {
        if (i == 0) {
            return moveUp(d);
        }
        if (i == 1) {
            return moveDown(d);
        }
        if (i == 2) {
            return moveLeft(d);
        }
        if (i == 3) {
            return moveRight(d);
        }
        return 0;
    }

    /**
     * 上移（local）
     *
     * @param d
     * @return
     */
    private int moveUp(int[][] d) {
        int virtualScore = 0;
        for (int j = 0; j < 4; j++) {
            zeros[0] = zeros[1] = zeros[2] = zeros[3] = 0;
            for (int i = 0; i < 3; i++) {
                if (d[i][j] == 0) {
                    for (int k = i + 1; k < 4; k++) {
                        zeros[k]++;
                    }
                }
            }
            for (int i = 1; i < 4; i++) {
                if (d[i][j] != 0 && zeros[i] != 0) {
                    d[i - zeros[i]][j] = d[i][j];
                    d[i][j] = 0;
                }
            }
            for (int i = 0; i < 3; i++) {
                if (d[i][j] == 0) {
                    break;
                }
                if (d[i][j] == d[i + 1][j]) {
                    d[i][j] = 2 * d[i][j];
                    d[i + 1][j] = 0;
                    virtualScore += (d[i][j]);
                    for (int k = i + 1; k < 3; k++) {
                        d[k][j] = d[k + 1][j];
                        d[k + 1][j] = 0;
                    }
                }
            }
        }
        randomPut(d);
        return virtualScore;
    }

    /**
     * 下移（local）
     *
     * @param d
     * @return
     */
    private int moveDown(int[][] d) {
        int virtualScore = 0;
        for (int j = 0; j < 4; j++) {
            zeros[0] = zeros[1] = zeros[2] = zeros[3] = 0;
            for (int i = 3; i > 0; i--) {
                if (d[i][j] == 0) {
                    for (int k = 0; k < i; k++) {
                        zeros[k]++;
                    }
                }
            }
            for (int i = 2; i > -1; i--) {
                if (d[i][j] != 0 && zeros[i] != 0) {
                    d[i + zeros[i]][j] = d[i][j];
                    d[i][j] = 0;
                }
            }
            for (int i = 3; i > 0; i--) {
                if (d[i][j] == 0) {
                    break;
                }
                if (d[i][j] == d[i - 1][j]) {
                    d[i][j] = 2 * d[i][j];
                    d[i - 1][j] = 0;
                    virtualScore += (d[i][j]);
                    for (int k = i - 1; k > 0; k--) {
                        d[k][j] = d[k - 1][j];
                        d[k - 1][j] = 0;
                    }
                }
            }
        }
        randomPut(d);
        return virtualScore;
    }

    /**
     * 左移（local）
     *
     * @param d
     * @return
     */
    private int moveLeft(int[][] d) {
        int virtualScore = 0;
        for (int i = 0; i < 4; i++) {
            zeros[0] = zeros[1] = zeros[2] = zeros[3] = 0;
            for (int j = 0; j < 3; j++) {
                if (d[i][j] == 0) {
                    for (int k = j + 1; k < 4; k++) {
                        zeros[k]++;
                    }
                }
            }
            for (int j = 1; j < 4; j++) {
                if (d[i][j] != 0 && zeros[j] != 0) {
                    d[i][j - zeros[j]] = d[i][j];
                    d[i][j] = 0;
                }
            }
            for (int j = 0; j < 3; j++) {
                if (d[i][j] == 0) {
                    break;
                }
                if (d[i][j] == d[i][j + 1]) {
                    d[i][j] = 2 * d[i][j];
                    d[i][j + 1] = 0;
                    virtualScore += (d[i][j]);
                    for (int k = j + 1; k < 3; k++) {
                        d[i][k] = d[i][k + 1];
                        d[i][k + 1] = 0;
                    }
                }
            }
        }
        randomPut(d);
        return virtualScore;
    }

    /**
     * 右移（local）
     *
     * @param d
     * @return
     */
    private int moveRight(int[][] d) {
        int virtualScore = 0;
        for (int i = 0; i < 4; i++) {
            zeros[0] = zeros[1] = zeros[2] = zeros[3] = 0;
            for (int j = 3; j > 0; j--) {
                if (d[i][j] == 0) {
                    for (int k = 0; k < j; k++) {
                        zeros[k]++;
                    }
                }
            }
            for (int j = 2; j > -1; j--) {
                if (d[i][j] != 0 && zeros[j] != 0) {
                    d[i][j + zeros[j]] = d[i][j];
                    d[i][j] = 0;
                }
            }
            for (int j = 3; j > 0; j--) {
                if (d[i][j] == 0) {
                    break;
                }
                if (d[i][j] == d[i][j - 1]) {
                    d[i][j] = 2 * d[i][j];
                    d[i][j - 1] = 0;
                    virtualScore += (d[i][j]);
                    for (int k = j - 1; k > 0; k--) {
                        d[i][k] = d[i][k - 1];
                        d[i][k - 1] = 0;
                    }
                }
            }
        }
        randomPut(d);
        return virtualScore;
    }

    /**
     * 以90%的概率放2，10%放4
     *
     * @param d
     */
    private void randomPut(int[][] d) {
        ArrayList<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (d[i][j] == 0) {
                    l.add(i * 4 + j);
                }
            }
        }
        int p = l.get(r.nextInt(l.size()));
        d[p / 4][p % 4] = r.nextInt(10) < 9 ? 2 : 4;
    }

    private void copy(int[][] source, int[][] dest) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                dest[i][j] = source[i][j];
            }
        }
    }

    private int getMax(double[] x) {
        double max = -1;
        int maxi = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] > max) {
                max = x[i];
                maxi = i;
            }
        }
        return maxi;
    }

    /**
     * 在d的局面上随机往下进行steps步，返回得分
     *
     * @param d
     * @return
     */
    private int randomPlaySteps(int[][] d) {
        int virtualScore = 0;
        int steps = 0;
        while (steps < STEPS) {
            boolean[] candidates = candidates(d);
            if (!candidates[0] && !candidates[1] && !candidates[2] && !candidates[3]) {
                break;
            }
            int ac = r.nextInt(4);
            while (candidates[ac] == false) {
                ac = r.nextInt(4);
            }
            virtualScore += move(ac, d);
            steps++;
        }
        return virtualScore;
    }

    /**
     * 本地测试用的
     */
    public void go(int[][] init) {
        score = 0;
        while (true) {
            boolean candidates[] = candidates(init);
            if (!candidates[0] && !candidates[1] && !candidates[2] && !candidates[3]) {// game over
                System.out.println(score);
                return;
            }

            int totalCount = 4;
            int totalScore = 4;
            double ucb[] = new double[4];
            int scorei[] = {1, 1, 1, 1};
            int counti[] = {1, 1, 1, 1};
            for (int i = 0; i < TRY_TIMES_PER_MOVE; i++) {// 对当前局面，尝试TRY_TIMES_PER_MOVE次
                int d[][] = new int[4][4];
                copy(init, d);
                for (int j = 0; j < 4; j++) {// 每次尝试前，先计算各个方向的ucb
                    if (!candidates[j]) {
                        ucb[j] = -1;
                        continue;
                    }
                    ucb[j] = calUCB(scorei[j], totalScore, counti[j], totalCount, true);
                }
                int choice = getMax(ucb);// 选择ucb最大的方向
                int virtualScore = move(choice, d);// 往该方向拓展一步

                virtualScore += randomPlaySteps(d);// 在上一步的基础上再走STEPS步

                totalCount++;// 更新次数
                counti[choice]++;// 更新该方向被选择的次数
                totalScore += virtualScore;// 更新总分
                scorei[choice] += virtualScore;// 更新该方向的总分
            }
            int choice = getMax(ucb);
            score += move(choice, init);
            printData(init);
        }
    }

    /**
     * UCB计算公式(Upper Confidence Bound)
     *
     * @param score
     * @param totalScore
     * @param count
     * @param totalCount
     * @param quiet
     * @return
     */
    private double calUCB(int score, int totalScore, int count, int totalCount, boolean quiet) {
        double experience = EI * (double) score / (double) totalScore;
        double curiosity = CI * Math.sqrt(Math.log((double) totalCount) / (double) count);
        if (!quiet) {
            System.out.println("experience:" + experience);
            System.out.println("curiosity:" + curiosity);
            System.out.println("ucb:" + (experience + curiosity));
        }
        return experience + curiosity;
    }

    int count = 0;

    public void printData(int d[][]) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(d[i][j] + "\t\t\t\t");
            }
            System.out.println();
        }
        count++;
        System.out.println();
        System.out.println(count + " steps ==============================================");
        System.out.println();
    }
}
