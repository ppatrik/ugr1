import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.input.Keyboard.KEY_ESCAPE;
import static org.lwjgl.input.Keyboard.isKeyDown;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class c6u2 {
    private boolean closeRequested = false;

    private int textura_stena;
    private int textura_podstava;

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 6 úloha 2 - Patrik");
    }

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();

        glViewport(0, 0, sirka, vyska); // Reset The Current Viewport
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        gluPerspective(45.0f, ((float) sirka / (float) vyska), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
        glTranslatef(0, 0, -10f);

        textura_podstava = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("podstava.jpg")).getTextureID());
        textura_stena = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("stena.jpg")).getTextureID());

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

        //glRotatef(0.1f, 1f, 0f, 0f);

        Thread.sleep(1);
    }

    private void vykresliGL() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glRotatef(0.05f, 1f, 1f, 1f);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        float body[][] = {{0f, 2f, 0f}, {-1f, 0f, 1f}, {-1f, 0f, -1f}, {1f, 0f, -1f}, {1f, 0f, 1f}};
        int steny[][] = {{1, 2, 0}, {2, 3, 0}, {3, 4, 0}, {4, 1, 0}};

        int i = 0;
        for (int[] aSteny : steny) {
            glBindTexture(GL_TEXTURE_2D, textura_stena);
            glBegin(GL_POLYGON);

            if(i%2==1)
                glTexCoord2f(1, 0);
            else
                glTexCoord2f(0, 0);
            glVertex3f(body[aSteny[0]][0], body[aSteny[0]][1], body[aSteny[0]][2]);

            if(i%2==0)
                glTexCoord2f(1, 0);
            else
                glTexCoord2f(0, 0);
            glVertex3f(body[aSteny[1]][0], body[aSteny[1]][1], body[aSteny[1]][2]);


            glTexCoord2f(0.5f, 1);
            glVertex3f(body[aSteny[2]][0], body[aSteny[2]][1], body[aSteny[2]][2]);
            glEnd();
            i++;
        }

        glBindTexture(GL_TEXTURE_2D, textura_podstava);
        glBegin(GL_POLYGON);

        glTexCoord2f(0f, 0f);
        glVertex3f(body[4][0], body[4][1], body[4][2]);

        glTexCoord2f(0f, 1f);
        glVertex3f(body[3][0], body[3][1], body[3][2]);

        glTexCoord2f(1f, 1f);
        glVertex3f(body[2][0], body[2][1], body[2][2]);

        glTexCoord2f(1f, 0f);
        glVertex3f(body[1][0], body[1][1], body[1][2]);
        glEnd();
        glDisable(GL_CULL_FACE);
    }


    public static void main(String[] args) {
        try {
            new c6u2().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
