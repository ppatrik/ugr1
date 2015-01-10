import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.input.Keyboard.KEY_ESCAPE;
import static org.lwjgl.input.Keyboard.isKeyDown;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class c6u1 {
    private boolean closeRequested = false;

    private int textura_pf;

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 6 úloha 1 - Patrik");
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

        textura_pf = (TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("pfupjs_50vyrocie.png")).getTextureID());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
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

        glRotatef(0.1f, 1f, 0f, 0f);

        Thread.sleep(1);
    }

    private double[][] body = {{-1, -1, -1}, {-1, -1, 1},
            {1, -1, 1}, {1, -1, -1}, {-1, 1, -1},
            {-1, 1, 1}, {1, 1, 1}, {1, 1, -1}};

    private float[][][] textury = {
            {{0f, 0f}, {1f, 0f}, {1f, 1f}, {0f, 1f}},
            {{0f, 0f}, {2f, 0f}, {2f, 2f}, {0f, 2f}},
            {{0f, 0f}, {3f, 0f}, {3f, 3f}, {0f, 3f}},
            {{0f, 0f}, {4f, 0f}, {4f, 4f}, {0f, 4f}},
            {{0f, 0f}, {5f, 0f}, {5f, 5f}, {0f, 5f}},
            {{0f, 0f}, {6f, 0f}, {6f, 6f}, {0f, 6f}}
    };

    private int[][] steny = {
            {0, 3, 2, 1, 1},
            {4, 5, 6, 7, 2},
            {0, 1, 5, 4, 3},
            {2, 3, 7, 6, 4},
            {1, 2, 6, 5, 5},
            {3, 0, 4, 7, 6}
    };

    private void vykresliGL() {// Clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glRotatef(0.05f, 1f, 1f, 1f);

        glBindTexture(GL_TEXTURE_2D, textura_pf);
        glBegin(GL_QUADS);

        for (int stena = 0; stena < steny.length; stena++) {
            for (int i = 0; i < 4; i++) {
                glTexCoord2f(textury[stena][i][0], textury[stena][i][1]);
                glVertex3d(body[steny[stena][i]][0], body[steny[stena][i]][1], body[steny[stena][i]][2]);
            }
        }

        glEnd();
    }

    public static void main(String[] args) {
        try {
            new c6u1().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
