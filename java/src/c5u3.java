import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.input.Keyboard.KEY_ESCAPE;
import static org.lwjgl.input.Keyboard.isKeyDown;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class c5u3 {
    private boolean closeRequested = false;

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 5 úloha 3 - Patrik");
    }

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();

        glViewport(0, 0, sirka, vyska); // Reset The Current Viewport
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        gluPerspective(45.0f, ((float) sirka / (float) vyska), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
        glTranslatef(0, 0, -5f);
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

        glRotatef(0.1f, 1f, 1f, 1f);

        Thread.sleep(1);
    }

    private void vykresliGL() {
        // Clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        float body[][] = {{0f, 1f, 0f}, {-1f, 0f, 1f}, {-1f, 0f, -1f}, {1f, 0f, -1f}, {1f, 0f, 1f}};
        int steny[][] = {{1,2,0}, {2,3,0}, {3,4,0}, {4,1,0}};

        for (int i = 0; i < steny.length; i++) {
            switch (i) {
                case 0:
                    glColor3f(1f, 0, 0);
                    break;
                case 1:
                    glColor3f(0f, 1f, 0f);
                    break;
                case 2:
                    glColor3f(0f, 0f, 1f);
                    break;
                case 3:
                    glColor3f(0f, 1f, 1f);
                    break;
            }
            glBegin(GL_POLYGON);
            glVertex3f(body[steny[i][0]][0], body[steny[i][0]][1], body[steny[i][0]][2]);
            glVertex3f(body[steny[i][1]][0], body[steny[i][1]][1], body[steny[i][1]][2]);
            glVertex3f(body[steny[i][2]][0], body[steny[i][2]][1], body[steny[i][2]][2]);
            glEnd();
        }
        glColor3f(1f, 0f, 1f);
        glBegin(GL_POLYGON);
        glVertex3f(body[4][0], body[4][1], body[4][2]);
        glVertex3f(body[3][0], body[3][1], body[3][2]);
        glVertex3f(body[2][0], body[2][1], body[2][2]);
        glVertex3f(body[1][0], body[1][1], body[1][2]);
        glEnd();
    }

    public static void main(String[] args) {
        try {
            new c5u3().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
