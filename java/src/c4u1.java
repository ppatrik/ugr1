import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.input.Keyboard.isKeyDown;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

public class c4u1 {
    boolean closeRequested = false;
    Matica matica = new Matica();

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 4 úloha 1 - Patrik");
    }

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();

        // Initialize Projection Matrix
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, sirka, vyska, 0.0, 1.0, -1.0);

        glLoadIdentity();
        glClearColor(0.f, 0.f, 0.f, 1.f);
    }

    private void spracujVstup() throws Exception {
        if (isKeyDown(KEY_ESCAPE) || Display.isCloseRequested())
            closeRequested = true;

        double zmena = 0.01;

        if (isKeyDown(KEY_LEFT)) {
            matica = Matica.nasob(matica, matica.posunutie(-zmena, 0, 0));
        }
        if (isKeyDown(KEY_RIGHT)) {
            matica = Matica.nasob(matica, matica.posunutie(zmena, 0, 0));
        }
        if (isKeyDown(KEY_UP)) {
            matica = Matica.nasob(matica, matica.posunutie(0, zmena, 0));
        }
        if (isKeyDown(KEY_DOWN)) {
            matica = Matica.nasob(matica, matica.posunutie(0, -zmena, 0));
        }

        if (isKeyDown(KEY_Q)) {
            matica = Matica.nasob(matica, matica.rotaciaX(zmena));
        }
        if (isKeyDown(KEY_W)) {
            matica = Matica.nasob(matica, matica.rotaciaX(-zmena));
        }
        if (isKeyDown(KEY_A)) {
            matica = Matica.nasob(matica, matica.rotaciaY(zmena));
        }
        if (isKeyDown(KEY_S)) {
            matica = Matica.nasob(matica, matica.rotaciaY(-zmena));
        }
        if (isKeyDown(KEY_Y)) {
            matica = Matica.nasob(matica, matica.rotaciaZ(zmena));
        }
        if (isKeyDown(KEY_X)) {
            matica = Matica.nasob(matica, matica.rotaciaZ(-zmena));
        }

        Thread.sleep(24);
    }

    private void vykresliGL() throws Exception {
        // Clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glLoadIdentity();

        double[][] body = {{-0.5, -0.5, -0.5}, {-0.5, -0.5, 0.5},
                {0.5, -0.5, 0.5}, {0.5, -0.5, -0.5}, {-0.5, 0.5, -0.5},
                {-0.5, 0.5, 0.5}, {0.5, 0.5, 0.5}, {0.5, 0.5, -0.5}};
        int[][] hrany = {{0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}};

        glLineWidth(1f);
        glColor3f(1f, 1f, 1f);

        glBegin(GL_LINES);
        for (int[] h : hrany) {
            Matica priemetA = Matica.maticaBodu(body[h[0]]);
            priemetA = Matica.nasob(matica, priemetA);
            Matica priemetB = Matica.maticaBodu(body[h[1]]);
            priemetB = Matica.nasob(matica, priemetB);
            glVertex2d(priemetA.get(0, 0), priemetA.get(1, 0));
            glVertex2d(priemetB.get(0, 0), priemetB.get(1, 0));
        }
        glEnd();


        glPointSize(4.0f);
        glBegin(GL_POINTS);
        for (double[] b : body) {
            Matica bod = Matica.maticaBodu(b);
            bod = Matica.nasob(matica, bod);
            glVertex2d(bod.get(0, 0), bod.get(1, 0));
        }

        glEnd();
    }

    public void spusti() throws Exception {
        vytvorOkno();
        inicializujGL();
        matica.identita();

        while (!closeRequested) {
            spracujVstup();
            vykresliGL();
            Display.update();
        }

        Display.destroy();
    }

    public static void main(String[] args) {
        try {
            new c4u1().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
