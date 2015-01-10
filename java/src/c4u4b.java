import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

public class c4u4b {
    boolean closeRequested = false;
    Matica matica = new Matica();

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 4 úloha 4 - Patrik");
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

        Thread.sleep(24);
    }

    private void bezier(double[][] body) {
        glPointSize(3.0f);
        glColor3f(1f, 1f, 1f);
        glBegin(GL_POINTS);

        for (double[] b : body) {
            glVertex2d(b[0], b[1]);
        }

        glEnd();

        glLineWidth(2f);
        glPointSize(5.0f);

        glBegin(GL_LINES);
        boolean first = true;
        for (double[] b : body) {
            if (first) {
                glVertex2d(b[0], b[1]);
                first = false;
            } else {
                glVertex2d(b[0], b[1]);
                glVertex2d(b[0], b[1]);
            }
        }
        glEnd();

        glBegin(GL_POINTS);
        glColor3f(1f, 0f, 0f);
        for (double t = 0; t <= 1; t += 0.001) {
            double[] tmp = getCasteljauPoint(body, body.length - 1, 0, t);
            glVertex2d(tmp[0], tmp[1]);
        }
        glEnd();
    }

    private double[] getCasteljauPoint(double[][] body, int r, int i, double t) {
        if (r == 0)
            return body[i];

        double[] p1 = getCasteljauPoint(body, r - 1, i, t);
        double[] p2 = getCasteljauPoint(body, r - 1, i + 1, t);

        double[] ret = new double[2];
        ret[0] = (1 - t) * p1[0] + t * p2[0];
        ret[1] = (1 - t) * p1[1] + t * p2[1];
        return ret;
    }

    private void vykresliGL() throws Exception {
        // Clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glLoadIdentity();

        double[][] body = {{-0.5, -0.5}, {0, 0.5}, {0.5, 0}, {0.5, -0.5}};
        bezier(body);
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
            new c4u4b().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
