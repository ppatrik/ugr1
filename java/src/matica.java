public class Matica {
    public double[][] matica;

    public Matica() {
        this(4, 4);
    }

    public Matica(int w, int h) {
        matica = new double[w][h];
    }

    public void identita() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < matica.length; j++)
                matica[i][j] = 0;
        for (int i = 0; i < 4; i++)
            matica[i][i] = 1;
    }

    public Matica posunutie(double x, double y, double z) {
        Matica m = new Matica();
        m.identita();
        m.set(0, 3, x);
        m.set(1, 3, y);
        m.set(2, 3, z);
        return m;
    }

    public Matica rotaciaX(double uhol) {
        Matica m = new Matica();
        m.identita();
        m.set(1, 1, Math.cos(uhol));
        m.set(1, 2, -Math.sin(uhol));
        m.set(2, 1, Math.sin(uhol));
        m.set(2, 2, Math.cos(uhol));
        return m;
    }

    public Matica rotaciaY(double uhol) {
        Matica m = new Matica();
        m.identita();
        m.set(0, 0, Math.cos(uhol));
        m.set(0, 2, Math.sin(uhol));
        m.set(2, 0, -Math.sin(uhol));
        m.set(2, 2, Math.cos(uhol));
        return m;
    }

    public Matica rotaciaZ(double uhol) {
        Matica m = new Matica();
        m.identita();
        m.set(0, 0, Math.cos(uhol));
        m.set(0, 1, -Math.sin(uhol));
        m.set(1, 0, Math.sin(uhol));
        m.set(1, 1, Math.cos(uhol));
        return m;
    }

    public double get(int i, int j) {
        return matica[i][j];
    }

    public void set(int i, int j, double set) {
        matica[i][j] = set;
    }

    public int width() {
        return matica[0].length;
    }

    public int height() {
        return matica.length;
    }

    public static Matica nasob(Matica a, Matica b) throws Exception {
        if (a.width() != b.height())
            throw new Exception("Zle rozmery!!!");

        Matica matica = new Matica(a.height(), b.width());

        for (int i = 0; i < matica.height(); i++)
            for (int j = 0; j < matica.width(); j++) {
                double novahodnota = 0;
                for (int k = 0; k < a.width(); k++) {
                    novahodnota += a.get(i, k) * b.get(k, j);
                }
                matica.set(i, j, novahodnota);
            }

        return matica;
    }

    public static Matica maticaBodu(double[] bod) {
        Matica m = new Matica();
        m.matica = new double[bod.length + 1][1];
        for (int i = 0; i < 3; i++) {
            m.matica[i][0] = bod[i];
        }
        m.matica[3][0] = 1;
        return m;
    }

}
