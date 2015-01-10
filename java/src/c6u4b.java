import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class c6u4b {
    private boolean closeRequested = false;

    private int textura_stena;
    private int textura_podstava;

    private int textura_svet_top;
    private int textura_svet_bottom;
    private int textura_svet_left;
    private int textura_svet_right;
    private int textura_svet_front;
    private int textura_svet_back;

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 6 úloha 4 - Patrik");
    }

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();

        glViewport(0, 0, sirka, vyska); // Reset The Current Viewport
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        gluPerspective(45.0f, ((float) sirka / (float) vyska), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix

        textura_podstava = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("podstava.jpg")).getTextureID());
        textura_stena = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("stena.jpg")).getTextureID());

        textura_svet_top = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("top.jpg")).getTextureID());
        textura_svet_bottom = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("bottom.jpg")).getTextureID());
        textura_svet_left = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("left.jpg")).getTextureID());
        textura_svet_right = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("right.jpg")).getTextureID());
        textura_svet_front = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("front.jpg")).getTextureID());
        textura_svet_back = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("back.jpg")).getTextureID());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
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
    float x = 0.0f, z = 10.0f;

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

        svet(-50 - 1, 100 + 2);

        podlaha(-50 - 1, 100 + 2);
        for (int ax = -50; ax <= 50; ax += 2) {
            for (int az = -50; az <= 50; az += 2) {
                if (Math.abs(ax + az) % 7 == Math.abs(ax - az) % 5) {
                    ihlan(ax, az);
                }
            }
        }
    }

    private void svet(int s, int w) {
        // back
        glBindTexture(GL_TEXTURE_2D, textura_svet_back);
        glBegin(GL_POLYGON);
        glTexCoord2f(0, 0);
        glVertex3f(s, s + w, s);
        glTexCoord2f(1f, 0);
        glVertex3f(s + w, s + w, s);
        glTexCoord2f(1f, 1f);
        glVertex3f(s + w, s, s);
        glTexCoord2f(0, 1f);
        glVertex3f(s, s, s);
        glEnd();

        // right
        glBindTexture(GL_TEXTURE_2D, textura_svet_right);
        glBegin(GL_POLYGON);
        glTexCoord2f(0, 0);
        glVertex3f(s + w, s + w, s);
        glTexCoord2f(1f, 0);
        glVertex3f(s + w, s + w, s + w);
        glTexCoord2f(1f, 1f);
        glVertex3f(s + w, s, s + w);
        glTexCoord2f(0, 1f);
        glVertex3f(s + w, s, s);
        glEnd();

        // front
        glBindTexture(GL_TEXTURE_2D, textura_svet_front);
        glBegin(GL_POLYGON);
        glTexCoord2f(0, 0);
        glVertex3f(s + w, s + w, s + w);
        glTexCoord2f(1f, 0);
        glVertex3f(s, s + w, s + w);
        glTexCoord2f(1f, 1f);
        glVertex3f(s, s, s + w);
        glTexCoord2f(0, 1f);
        glVertex3f(s + w, s, s + w);
        glEnd();

        // left
        glBindTexture(GL_TEXTURE_2D, textura_svet_left);
        glBegin(GL_POLYGON);
        glTexCoord2f(0, 0);
        glVertex3f(s, s + w, s + w);
        glTexCoord2f(1f, 0);
        glVertex3f(s, s + w, s);
        glTexCoord2f(1f, 1f);
        glVertex3f(s, s, s);
        glTexCoord2f(0, 1f);
        glVertex3f(s, s, s + w);
        glEnd();

        // top
        glBindTexture(GL_TEXTURE_2D, textura_svet_top);
        glBegin(GL_POLYGON);
        glTexCoord2f(0, 1f);
        glVertex3f(s + w, s + w, s + w);
        glTexCoord2f(1f, 1f);
        glVertex3f(s, s + w, s + w);
        glTexCoord2f(1f, 0);
        glVertex3f(s, s + w, s);
        glTexCoord2f(0, 0);
        glVertex3f(s + w, s + w, s);
        glEnd();

        // bottom
        glBindTexture(GL_TEXTURE_2D, textura_svet_bottom);
        glBegin(GL_POLYGON);
        glTexCoord2f(0, 0);
        glVertex3f(s + w, s, s + w);
        glTexCoord2f(0, 1f);
        glVertex3f(s + w, s, s);
        glTexCoord2f(1f, 1f);
        glVertex3f(s, s, s);
        glTexCoord2f(1f, 0);
        glVertex3f(s, s, s + w);
        glEnd();

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

    private void podlaha(int s, int w) {

        float n = ((w + 1) * 2 - 1);

        glBindTexture(GL_TEXTURE_2D, textura_podstava);
        glBegin(GL_POLYGON);

        glTexCoord2f(0, 0);
        glVertex3f(s, pozicia_podlahy, s);

        glTexCoord2f(w / 2, 0);
        glVertex3f(s + w, pozicia_podlahy, s);

        glTexCoord2f(w / 2, w / 2);
        glVertex3f(s + w, pozicia_podlahy, s + w);

        glTexCoord2f(0, w / 2);
        glVertex3f(s, pozicia_podlahy, s + w);
        glEnd();

    }

    public static void main(String[] args) {
        try {
            new c6u4b().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
