import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class c6u5b {
    private boolean closeRequested = false;

    private int textura_stena;
    private int textura_podlaha;
    private int textura_strop;

    private void vytvorOkno() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        Display.setTitle("Cvicenie 6 úloha 4 - Patrik");
    }

    boolean[][] mapa;

    int width;
    int height;

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();
        width = sirka;
        height = vyska;

        glViewport(0, 0, sirka, vyska); // Reset The Current Viewport
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        gluPerspective(45.0f, ((float) sirka / (float) vyska), 0.001f, 100.0f); // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix

        textura_stena = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("stena.jpg")).getTextureID());
        textura_podlaha = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("podstava.jpg")).getTextureID());
        textura_strop = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("strop.jpg")).getTextureID());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        gluPerspective(45f, (float) width / height, .1f, 100f);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();

        loadMap("mapa.txt");
    }

    public void loadMap(String fileName) {
        List<String> lines = new ArrayList<String>();
        Scanner fileScanner = new Scanner(getClass().getResourceAsStream(fileName));
        while (fileScanner.hasNextLine()) {
            lines.add(fileScanner.nextLine());
        }
        mapa = new boolean[lines.size()][lines.get(0).length()];
        for (int r = 0; r < mapa.length; r++) {
            for (int s = 0; s < mapa[r].length; s++) {
                mapa[r][s] = lines.get(r).charAt(s) != '#';
            }
        }
        boolean start = true;
        for (int r = 0; r < mapa.length; r++) {
            for (int s = 0; s < mapa[r].length; s++) {
                if (start && mapa[r][s]) {
                    start = false;
                    x = r + 0.5f;
                    z = s + 0.5f;
                    angle = (float) (Math.PI) / 2;
                    lx = (float) Math.sin(angle);
                    lz = (float) -Math.cos(angle);
                    break;
                }
            }
            if (!start)
                break;
        }
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
    float x = 0.0f, z = 0.0f;

    private void spracujVstup() throws Exception {
        if (isKeyDown(KEY_ESCAPE) || Display.isCloseRequested())
            closeRequested = true;

        float fraction = 0.01f;

        if (isKeyDown(KEY_LEFT)) {
            angle -= fraction;
            lx = (float) Math.sin(angle);
            lz = (float) -Math.cos(angle);
        }
        if (isKeyDown(KEY_RIGHT)) {
            angle += fraction;
            lx = (float) Math.sin(angle);
            lz = (float) -Math.cos(angle);
        }
        float xb = x;
        float zb = z;
        if (isKeyDown(KEY_UP)) {
            x += lx * fraction;
            z += lz * fraction;
        }
        if (isKeyDown(KEY_DOWN)) {
            x -= lx * fraction;
            z -= lz * fraction;
        }
        // kontrola ci nevysiel z mapy
        x = x > mapa.length ? mapa.length : (x < 0 ? 0 : x);
        z = z > mapa[0].length ? mapa[0].length : (z < 0 ? 0 : z);

        if (!mapa[(int) x][(int) z]) {
            if (!mapa[(int) x][(int) zb])
                x = xb;
            if (!mapa[(int) xb][(int) z])
                z = zb;

        }

        Thread.sleep(1);
    }

    protected void make2D() {
        /*glMatrixMode(GL_PROJECTION);
        //glPopMatrix();
        glLoadIdentity();
        glOrtho(0, 1280, 720, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        //glPopMatrix();
        glLoadIdentity();*/


        //Remove the Z axis
        //GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, 0, height, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
    }

    protected void make3D() {
        //Restore the Z axis
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        //GL11.glDisable(GL11.GL_LIGHTING);

        // Reset transformations
        glLoadIdentity();
        // Set the camera
        gluLookAt(x, 0f, z,
                x + lx, 0f, z + lz,
                0.0f, 2f, 0.0f);

    }

    private void vykresliGL() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        make3D();

        podlaha(0, mapa.length, mapa[0].length);
        strop(0, mapa.length, mapa[0].length);

        for (int r = 0; r < mapa.length; r++) {
            for (int s = 0; s < mapa[r].length; s++) {
                if (!mapa[r][s]) {
                    stena(r, s);
                }
            }
        }


        // hud
        make2D();

        glPointSize(5f);
        glBegin(GL_POINTS);
        for (int r = 0; r < mapa.length; r++) {
            for (int s = 0; s < mapa[r].length; s++) {
                if (mapa[r][s]) {
                    glColor3f(0.9f, 0.9f, 0.9f);
                } else {
                    glColor3f(0.5f, 0, 0);
                }
                glVertex2f(10 + s * 5, 10 + r * 5);
            }
        }
        glEnd();
        glColor3f(0, 0, 0);
        glPointSize(2);
        glBegin(GL_POINTS);
        glVertex2f(10 + z * 5 - 2.5f, 10 + x * 5 - 2.5f);
        glEnd();

        glColor3f(1f, 1f, 1f);
    }

    private void stena(int ax, int az) {
        float body[][] = {
                {ax, pozicia_podlahy, az},
                {ax + 1f, pozicia_podlahy, az},
                {ax + 1f, pozicia_podlahy, az + 1f},
                {ax, pozicia_podlahy, az + 1f},
                {ax, pozicia_stropu, az},
                {ax + 1f, pozicia_stropu, az},
                {ax + 1f, pozicia_stropu, az + 1f},
                {ax, pozicia_stropu, az + 1f}
        };
        int steny[][] = {
                {0, 1, 5, 4},
                {1, 2, 6, 5},
                {2, 3, 7, 6},
                {3, 0, 4, 7}
        };

        for (int[] aSteny : steny) {
            glBindTexture(GL_TEXTURE_2D, textura_stena);
            glBegin(GL_POLYGON);

            glTexCoord2f(1, 1);
            glVertex3f(body[aSteny[0]][0], body[aSteny[0]][1], body[aSteny[0]][2]);
            glTexCoord2f(0, 1);
            glVertex3f(body[aSteny[1]][0], body[aSteny[1]][1], body[aSteny[1]][2]);
            glTexCoord2f(0, 0);
            glVertex3f(body[aSteny[2]][0], body[aSteny[2]][1], body[aSteny[2]][2]);
            glTexCoord2f(1, 0);
            glVertex3f(body[aSteny[3]][0], body[aSteny[3]][1], body[aSteny[3]][2]);
            glEnd();
        }
    }


    private float pozicia_podlahy = -0.5f;
    private float pozicia_stropu = 0.5f;

    private void podlaha(int s, int w, int h) {
        surface(s, w, h, pozicia_podlahy, textura_podlaha);
    }

    private void strop(int s, int w, int h) {
        surface(s, w, h, pozicia_stropu, textura_strop);
    }

    private void surface(int s, int w, int h, float pozicia_y, int textura) {

        glBindTexture(GL_TEXTURE_2D, textura);
        glBegin(GL_POLYGON);

        glTexCoord2f(0, 0);
        glVertex3f(s, pozicia_y, s);
        glTexCoord2f(w, 0);
        glVertex3f(s + w, pozicia_y, s);
        glTexCoord2f(w, h);
        glVertex3f(s + w, pozicia_y, s + h);
        glTexCoord2f(0, h);
        glVertex3f(s, pozicia_y, s + h);
        glEnd();
    }

    public static void main(String[] args) {
        try {
            new c6u5b().spusti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
