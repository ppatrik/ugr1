import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

public class c4u3b {
    boolean closeRequested = false;
    Matica matica = new Matica();

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 4 úloha 3 - Patrik");
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

    public double[] normalovyVektor(double[] p1, double[] p2, double[] p3) {
        int x = 0, y = 1, z = 2;
        double[] N = new double[3];
        double[] U = {p2[x] - p1[x], p2[y] - p1[y], p2[z] - p1[z]};
        double[] V = {p3[x] - p1[x], p3[y] - p1[y], p3[z] - p1[z]};

        N[x] = U[y] * V[z] - U[z] * V[y];
        N[y] = U[z] * V[x] - U[x] * V[z];
        N[z] = U[x] * V[y] - U[y] * V[x];

        return N;
    }

    private void vykresliGL() throws Exception {
        // Clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glLoadIdentity();

        double[][] body = {{-0.5, -0.5, -0.5}, {-0.5, -0.5, 0.5},
                {0.5, -0.5, 0.5}, {0.5, -0.5, -0.5}, {-0.5, 0.5, -0.5},
                {-0.5, 0.5, 0.5}, {0.5, 0.5, 0.5}, {0.5, 0.5, -0.5}};
        int[][] steny = {{0, 3, 2, 1, 1}, {4, 5, 6, 7, 2}, {0, 1, 5, 4, 3}, {2, 3, 7, 6, 4}, {1, 2, 6, 5, 5}, {3, 0, 4, 7, 6}};

        glPointSize(4.0f);
        glBegin(GL_QUADS);

        for (int[] s : steny) {

            Matica priemetA = Matica.nasob(matica, Matica.maticaBodu(body[s[0]]));
            Matica priemetB = Matica.nasob(matica, Matica.maticaBodu(body[s[1]]));
            Matica priemetC = Matica.nasob(matica, Matica.maticaBodu(body[s[2]]));
            Matica priemetD = Matica.nasob(matica, Matica.maticaBodu(body[s[3]]));

            double[] p1 = new double[]{priemetA.get(0, 0), priemetA.get(1, 0), priemetA.get(2, 0)};
            double[] p2 = new double[]{priemetB.get(0, 0), priemetB.get(1, 0), priemetB.get(2, 0)};
            double[] p3 = new double[]{priemetD.get(0, 0), priemetD.get(1, 0), priemetD.get(2, 0)};

            double[] n = normalovyVektor(p1, p2, p3);

            if (n[2] > 0) {

                switch (s[4]) {
                    case 1:
                        brightness(1f, 1f, 1f, (float) n[2]);
                        break;
                    case 2:
                        brightness(1f, 0f, 0f, (float) n[2]);
                        break;
                    case 3:
                        brightness(0f, 1f, 0f, (float) n[2]);
                        break;
                    case 4:
                        brightness(0f, 0f, 1f, (float) n[2]);
                        break;
                    case 5:
                        brightness(1f, 1f, 0f, (float) n[2]);
                        break;
                    case 6:
                        brightness(0f, 1f, 1f, (float) n[2]);
                        break;
                }
                glVertex2d(priemetA.get(0, 0), priemetA.get(1, 0));
                glVertex2d(priemetB.get(0, 0), priemetB.get(1, 0));
                glVertex2d(priemetC.get(0, 0), priemetC.get(1, 0));
                glVertex2d(priemetD.get(0, 0), priemetD.get(1, 0));
            }
        }
        glEnd();
    }

    private void brightness(float r, float g, float b, float scale) {
        r = Math.min(1f, r * scale);
        g = Math.min(1f, g * scale);
        b = Math.min(1f, b * scale);
        glColor3f(r, g, b);
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
            new c4u3b().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
