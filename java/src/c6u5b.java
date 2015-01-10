import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
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

    private void inicializujGL() throws Exception {
        int sirka = Display.getDisplayMode().getWidth();
        int vyska = Display.getDisplayMode().getHeight();

        glViewport(0, 0, sirka, vyska); // Reset The Current Viewport
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        gluPerspective(45.0f, ((float) sirka / (float) vyska), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix

        textura_stena = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("stena.jpg")).getTextureID());
        textura_podlaha = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("podstava.jpg")).getTextureID());
        textura_strop = (TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("strop.jpg")).getTextureID());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        List<String> lines = new ArrayList<String>();
        Scanner fileScanner = new Scanner(getClass().getResourceAsStream("mapa.txt"));
        while (fileScanner.hasNextLine()) {
            lines.add(fileScanner.nextLine());
            System.out.println(lines.get(lines.size() - 1));
        }
        mapa = new boolean[lines.size()][lines.get(0).length()];
        boolean start = true;
        for (int r = 0; r < mapa.length; r++) {
            for (int s = 0; s < mapa[r].length; s++) {
                mapa[r][s] = lines.get(r).charAt(s) != '#';
                if(start && mapa[r][s]) {
                    start = false;
                    x = r * 2;
                    z = s * 2;
                }
            }
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
        if(!mapa[(int)x][(int)z]) {
            x = xb;
            z = zb;
        }
        // TODO: urobit kontrolu ci je este v priestore alebo by siel mimo
        // kontrola ci nevysiel z mapy
        x = x > mapa.length ? mapa.length : (x < 0 ? 0 : x);
        z = z > mapa[0].length ? mapa[0].length : (z < 0 ? 0 : z);

        // Reset transformations
        glLoadIdentity();
        // Set the camera
        gluLookAt(x, 0f, z,
                x + lx, 0f, z + lz,
                0.0f, 2f, 0.0f);

        Thread.sleep(1);
    }

    private void vykresliGL() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        podlaha(0, mapa.length, mapa[0].length);
        strop(0, mapa.length, mapa[0].length);

        for (int r = 0; r < mapa.length; r++) {
            for (int s = 0; s < mapa[r].length; s++) {
                if (!mapa[r][s]) {
                    stena(r, s);
                }
            }
        }

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
