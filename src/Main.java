import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int[][] init = {{2, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        Ai ai = new Ai();
        ai.go(init);
    }
}
