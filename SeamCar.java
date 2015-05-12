import java.awt.Color;
public class SeamCarver2 {
    private Color[] pic;
    private int[] en;
    private int[] edgeto;
    private int[] disto; 
    private int start;
    private int end;
    private int w;
    private int h;
    private final int BORDER = 195075;
    private final int inf = Integer.MAX_VALUE;
    
    /**
     * // create a seam carver object based on the given picture
     * @param picture
     */
    public SeamCarver2(Picture picture){
        Picture pic = new Picture(picture);
        this.w = pic.width();
        this.h = pic.height();
        this.pic = new Color[h*w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++){
                this.pic[map(i, j)] = pic.get(j, i);
            }
        }
        edgeto = new int[h * w + 2];
        disto = new int[h * w + 2];
        end = h * w;
        start = h * w + 1;
        en = new int[h * w + 2];
        initEnergy();
    }
    private int map(int i, int j){
        return i * w + j;
    }
    private int mapx(int p){
        return p % w;
    }
    private int mapy(int p){
        return (p - p %  w) / w;
    }
    
    /**
     * // current picture
     * @return
     */
    public Picture picture(){
       return ToPic();
    }
    private Picture ToPic(){
        Picture P = new Picture(w, h);
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                P.set(j, i, pic[map(i, j)]);
            }
        }
        return P;
    }
   /**
    * width of current picture
    */
    public int width(){
        return w;
    }
    /**
     * // height of current picture
     */
    public int height(){
        return h;
    }
    /**
     * Energy of pixel at column x and row y
     */
    public double energy(int x, int y){
        checkpoint(y, x);
        return (double) en[map(y, x)];
    }
    private void initEnergy(){
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                en[map(i, j)] = calcEnergy(i, j);
            }
        }
    }
    private int calcEnergy(int i, int j){
        if (i == 0 || j == 0 || i >= h - 1 || j >= w - 1) return BORDER;
        return (int) (gradx(i, j) + grady(i, j));
    }
    /**
     * sequence of indices for horizontal seam
     * @return
     */
    public int[] findHorizontalSeam(){
        FHS();
        int p = end;
        int[] HS = new int[w];
        for (int i = w - 1; i >= 0; i--) {
            p = edgeto[p];
            HS[mapx(p)] = mapy(p);
        }
        return HS;
    }
    /**
     * // sequence of indices for vertical seam
     * @return
     */
    public int[] findVerticalSeam(){
        FVS();
        int p = end;
        int[] VS = new int[h];
        while (mapy(p) > 0) {
            p = edgeto[p];
            VS[mapy(p)] = mapx(p);
        }
        return VS;
    }   
    private void FVS(){
        IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(h * w + 3);
        for (int p = 0; p < h * w + 1; p++) disto[p] = inf;
        pq.insert(start, 0);
        while (!pq.isEmpty()){
            int p = pq.delMin();
            for(int q: vadj(p)){ 
                relax(p, q, pq);
            }
        }
    }
    private void FHS(){
        IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(h * w + 3);
        for (int p = 0; p < h * w + 1; p++) disto[p] = inf;
        
        pq.insert(start, 0);
        while (!pq.isEmpty()) {
            int p = pq.delMin();
            for (int q: hadj(p)) {
                relax(p, q, pq);
            }
         }
    }
    
    private void relax(int from, int p, IndexMinPQ<Integer> pq){
        if (disto[p] > disto[from] + en[p]) {
            disto[p] = disto[from] + en[p];
            edgeto[p] = from;
            if (pq.contains(p)) pq.decreaseKey(p, disto[p]);
            else pq.insert(p, disto[p]);
        }
    }
    
    private Bag<Integer> vadj(int p){
        Bag<Integer> VA = new Bag<Integer>();
        if (p == start) {
            for (int v = 0; v < w; v++){
                VA.add(v);
            }
            return VA;
        }
        else if (mapy(p) == h - 1) {
            VA.add(end);
            return VA;
        }
        else if (p == end) return VA;
        int i = mapy(p) + 1, j = mapx(p);
        if (checkpoint(i, j + 1)) VA.add(map(i, j + 1));
        if (checkpoint(i, j - 1)) VA.add(map(i, j - 1));
        if (checkpoint(i, j)) VA.add(map(i, j));
        return VA;
    }
    private Bag<Integer> hadj(int p){
        Bag<Integer> HA = new Bag<Integer>();
        if (p == start) {
            for(int q = 0; q < h-1; q++){
                HA.add(map(q, 0));
            }
            return HA;
        }
        else if (mapx(p) == w - 1) {
            HA.add(end);
            return HA;
        }
        else if (p == end) return HA;
        int i = mapy(p), j = mapx(p) + 1;               
        if (checkpoint(i + 1, j)) HA.add(map(i + 1, j));
        if (checkpoint(i - 1, j)) HA.add(map(i - 1, j));
        if (checkpoint(i, j)) HA.add(map(i, j));    
        return HA;
    }
    /**
     * // remove horizontal seam from current picture
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam){
        checkHSeam(seam);
        int max = 0;
        for (int i : seam) {
            if (i > max) max = i;
        }
        for (int j = 0; j < w; j++){
            for(int i = seam[j]; i < max; i++){
                pic[map(i, j)] = pic[map(i + 1, j)];
                en[map(i,j)] = en[map(i + 1, j)];
            }
        }
        System.arraycopy(pic, (max + 1) * w, pic, (max) * w, (h - max - 1) * w);
        System.arraycopy(en, (max + 1) * w, en, (max) * w, (h - max - 1) * w);
        h--;
        refresh();
        for(int j = 0; j < w; j++){
            if (checkpoint(seam[j] - 1, j)) en[map(seam[j] - 1, j)] = calcEnergy(seam[j] - 1, j);
            if (checkpoint(seam[j] - 1, j)) en[map(seam[j], j)] = calcEnergy(seam[j], j);
            if (checkpoint(seam[j] - 1, j)) en[map(seam[j] + 1, j)] = calcEnergy(seam[j] + 1, j);
        }
    }
    /**
     * // remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam){
        checkVSeam(seam);
        for(int i = 0; i < h; i++){
            System.arraycopy(pic, i * w, pic, i * (w - 1), seam[i]);
            System.arraycopy(en, i * w, en, i * (w - 1), seam[i]);
            System.arraycopy(pic, i * w + seam[i], pic, i * (w - 1) + seam[i] - 1, w - seam[i]);
            System.arraycopy(en, i * w + seam[i], en, i * (w - 1) + seam[i] - 1, w - seam[i]);
        }
        w--;
        refresh();
        for (int i = 0; i < h; i++){
            if (checkpoint(i, seam[i] + 1)) en[map(i, seam[i] + 1)] = calcEnergy(i, seam[i] + 1);
            if (checkpoint(i, seam[i] - 1)) en[map(i, seam[i] - 1)] = calcEnergy(i, seam[i] - 1);
            if (checkpoint(i, seam[i])) en[map(i, seam[i])] = calcEnergy(i, seam[i]);
        }
    }
    private void refresh(){
        end = h * w;
        start = h * w + 1;
    }
    /*************************************************************************************************************************************************
     * 
     * @param l
     * @param r
     * @param u
     * @param d
     ************************************************************************************************************************************************/
    private double gradx(int i, int j) {
        int r1 = pic[map(i, j - 1)].getRed();
        int b1 = pic[map(i, j - 1)].getBlue();
        int g1 = pic[map(i, j - 1)].getGreen();
        int r2 = pic[map(i, j + 1)].getRed();
        int b2 = pic[map(i, j + 1)].getBlue();
        int g2 = pic[map(i, j + 1)].getGreen();
       
        return sq(r1 - r2) + sq(b1 - b2) + sq(g1 - g2);
    }
    private double grady(int i, int j) {
        int r1 = pic[map(i - 1, j)].getRed();
        int b1 = pic[map(i - 1, j)].getBlue();
        int g1 = pic[map(i - 1, j)].getGreen();
        int r2 = pic[map(i + 1, j)].getRed();
        int b2 = pic[map(i + 1, j)].getBlue();
        int g2 = pic[map(i + 1, j)].getGreen();
       
        return sq(r1 - r2) + sq(b1 - b2) + sq(g1 - g2);
    }
    private double sq(double x) {
        return Math.pow(x, 2);
    }
    private boolean checkpoint(int i, int j) {
        boolean flag = false;
        if (j > w - 1) flag = true;
        if (i > h - 1) flag = true;
        if (j < 0) flag = true;
        if (i < 0) flag = true;
        return !flag;
    }
    private void checkHSeam(int[] seam){
        if (seam == null) throw new NullPointerException();
        boolean flag = false;
        if (w <= 1) flag = true;
        if (seam.length != w) flag = true;
        if (!checkpoint(0, seam[0])) flag = true;
        for (int i = 0; i < seam.length - 1; i++) {
            if (!checkpoint(seam[i + 1], i + 1));
            if (Math.abs(seam[i + 1] - seam[i]) > 1) flag = true;
        }
        if (flag) throw new IllegalArgumentException();
    }
    private void checkVSeam(int[] seam){
        if (seam == null) throw new NullPointerException();
        boolean flag = false;
        if (w <= 1) flag = true;
        if (seam.length != h) {
            StdOut.print("Length");
            flag = true;
        }
        if (!checkpoint(0,seam[0])) {
            StdOut.print("checkpoint");
            flag = true;
        }
        for (int i = 0; i < seam.length - 1; i++) {
            //if(!checkpoint(i+1,seam[i+1]));
            if (Math.abs(seam[i + 1] - seam[i]) > 1){
                StdOut.print("Abs > 1\n" + seam[i + 1] + " vs " + seam[i]);
                flag = true;
            }
        }
        if (flag) throw new IllegalArgumentException();
    }
    public static void main(String args[]){
        SeamCarver2 test= new SeamCarver2(new Picture("/Users/tiemo/Desktop/DeskStuff/Tiemo Profile2.jpg"));
        Picture t=new Picture(test.picture());
        t.show();
        Stopwatch timer = new Stopwatch();
        for (int l = 0; l < 300; l++) {
            //if (l % 5 == 1) test.picture().show();
            test.findHorizontalSeam();
            test.findVerticalSeam();
        }
        StdOut.print(timer.elapsedTime());
        test.picture().show();/**/
    }
}
