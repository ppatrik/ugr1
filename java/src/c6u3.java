import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.nio.FloatBuffer;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.input.Keyboard.KEY_DOWN;
import static org.lwjgl.input.Keyboard.KEY_UP;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class c6u3 {
    private boolean closeRequested = false;

    private int textura_stena;
    private int textura_podstava;

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 6 úloha 3 - Patrik");
    }

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();

        glViewport(0, 0, sirka, vyska); // Reset The Current Viewport
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        gluPerspective(45.0f, ((float) sirka / (float) vyska), 0.1f, 200.0f); // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix

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

    // uhol natocenia kamery
    float angle = 0.0f;
    // aktualny smer pohladu
    float lx = 0f, lz = -1f;
    // suradnice kamery
    float x = 0.0f, z = -10.0f;

    private void spracujVstup() throws Exception {
        if (isKeyDown(KEY_ESCAPE) || Display.isCloseRequested())
            closeRequested = true;

        float fraction = 0.1f;

        if (isKeyDown(KEY_LEFT)) {
            angle -= 0.01f;
            lx = (float) Math.sin(angle);
            lz = (float) -Math.cos(angle);
        }
        if (isKeyDown(KEY_RIGHT)) {
            angle += 0.01f;
            lx = (float) Math.sin(angle);
            lz = (float) -Math.cos(angle);
        }
        if (isKeyDown(KEY_UP)) {
            x += lx * fraction;
            z += lz * fraction;
        }
        if (isKeyDown(KEY_DOWN)) {
            x -= lx * fraction;
            z -= lz * fraction;
        }
        x = x > 50 ? 50 : (x < -50 ? -50 : x);
        z = z > 50 ? 50 : (z < -50 ? -50 : z);

        // Reset transformations
        glLoadIdentity();
        // Set the camera
        gluLookAt(x, 0.5f, z,
                x + lx, 0.5f, z + lz,
                0.0f, 0.5f, 0.0f);

        Thread.sleep(1);
    }

    private void vykresliGL() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        podlaha(-50 - 1, -50 - 1, 100 + 2);
        for (int ax = -50; ax <= 50; ax += 2) {
            for (int az = -50; az <= 50; az += 2) {
                if (Math.abs(ax + az) % 7 == Math.abs(ax - az) % 5) {
                    ihlan(ax, az);
                }
            }
        }
    }

    private int pozicia_podlahy = -1;

    private void ihlan(int ax, int az) {
        float body[][] = {{ax, pozicia_podlahy + 2f, az}, {ax - 1f, pozicia_podlahy, az - 1f}, {ax + 1f, pozicia_podlahy, az - 1f}, {ax + 1f, pozicia_podlahy, az + 1f}, {ax - 1f, pozicia_podlahy, az + 1f}};
        int steny[][] = {{1, 2, 0}, {2, 3, 0}, {3, 4, 0}, {4, 1, 0}};

        for (int[] aSteny : steny) {
            glBindTexture(GL_TEXTURE_2D, textura_stena);
            glBegin(GL_POLYGON);

            glTexCoord2f(0, 0);
            glVertex3f(body[aSteny[0]][0], body[aSteny[0]][1], body[aSteny[0]][2]);


            glTexCoord2f(1, 0);
            glVertex3f(body[aSteny[1]][0], body[aSteny[1]][1], body[aSteny[1]][2]);


            glTexCoord2f(0.5f, 1);
            glVertex3f(body[aSteny[2]][0], body[aSteny[2]][1], body[aSteny[2]][2]);
            glEnd();
        }
    }

    private void podlaha(int sx, int sz, int w) {

        float n = ((w + 1) * 2 - 1);

        glBindTexture(GL_TEXTURE_2D, textura_podstava);
        glBegin(GL_POLYGON);

        glTexCoord2f(0, 0);
        glVertex3f(sx, pozicia_podlahy, sz);

        glTexCoord2f(w / 2, 0);
        glVertex3f(sx + w, pozicia_podlahy, sz);

        glTexCoord2f(w / 2, w / 2);
        glVertex3f(sx + w, pozicia_podlahy, sz + w);

        glTexCoord2f(0, w / 2);
        glVertex3f(sx, pozicia_podlahy, sz + w);
        glEnd();

    }

    public static void main(String[] args) {
        try {
            new c6u3().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
