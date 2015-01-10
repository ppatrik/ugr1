import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.input.Keyboard.KEY_ESCAPE;
import static org.lwjgl.input.Keyboard.isKeyDown;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class c5u2 {
    private boolean closeRequested = false;

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 5 úloha 2 - Patrik");
    }

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();

        glViewport(0, 0, sirka, vyska); // Reset The Current Viewport
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        gluPerspective(45.0f, ((float) sirka / (float) vyska), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
    }

    public void spusti() throws Exception {
        vytvorOkno();
        inicializujGL();
        while (!closeRequested) {
            spracujVstup();
            vykresliGL();
            Display.update();
        }
        Display.destroy();
    }

    private void spracujVstup() throws Exception {
        if (isKeyDown(KEY_ESCAPE) || Display.isCloseRequested())
            closeRequested = true;
        Thread.sleep(100);
    }

    private void vykresliGL() {
        // Clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glPointSize(10);
        glLineWidth(3);

        glLoadIdentity();

        glTranslatef(-2f, 0f, -10.0f);
        glColor3f(1f, 1f, 1f);
        glBegin(GL_TRIANGLES);
        glVertex3f(0f, 1f, 0f);
        glVertex3f(1f, -1f, 0f);
        glVertex3f(-1f, -1f, 0f);
        glEnd();

        glTranslatef(4f, 0f, 0f);
        glColor3f(1f, 0f, 0f);
        glBegin(GL_POLYGON);
        glVertex3f(1f, 1f, 0f);
        glVertex3f(1f, -1f, 0f);
        glVertex3f(-1f, -1f, 0f);
        glVertex3f(-1f, 1f, 0f);
        glEnd();
    }

    public static void main(String[] args) {
        try {
            new c5u2().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
