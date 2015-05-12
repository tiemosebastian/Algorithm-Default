import java.awt.Color;
import java.awt.Color.*;

public class SeamCarver2 {
    private DAGPic pic;
    private final double BORDER = 195075;
    
    /**
     * // create a seam carver object based on the given picture
     * @param picture
     */
    public SeamCarver2(Picture picture){
        Picture pic = new Picture(picture);
        int w = pic.width();
        int h = pic.height();
        Pixel tempic[][]= new Pixel[h][w];
        for(int i=0; i<h;i++){
            for(int j=0;j<w;j++){
                tempic[i][j] = new Pixel(pic.get(j,i));
            }
        }
        for(int i=0; i<h;i++){
            for(int j=0;j<w;j++){
                if(i>1&&j>1){
                    tempic[i-1][j-1].energy(tempic[i-2][j-1],tempic[i][j-1],tempic[i-1][j],tempic[i-1][j-2]);
                }
            }
        }
        
        this.pic = new DAGPic(tempic);
            
    }
    /**
     * // current picture
     * @return
     */
    public Picture picture(){
       return ToPic();
    }
   /**
    * width of current picture
    */
    public int width(){
        return pic.width();
    }
    /**
     * // height of current picture
     */
    public int height(){
        return pic.height();
    }
    /**
     * Energy of pixel at column x and row y
     */
    public double energy(int x, int y){
        checkpoint(x,y);
        if(x==0||y==0||x==width()-1||y==height()-1) return BORDER;
        return pic.get(x,y).e();
    }
    /**
     * sequence of indices for horizontal seam
     * @return
     */
    public int[] findHorizontalSeam(){
        return pic.FindHSeam();
    }
    /**
     * // sequence of indices for vertical seam
     * @return
     */
    public int[] findVerticalSeam(){
        return pic.FindVSeam();
    }
    /**
     * // remove horizontal seam from current picture
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam){
        checkHSeam(seam);
        pic.DeleteHorzSeam(seam);
    }
    /**
     * // remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam){
        checkVSeam(seam);
        pic.DeleteVertSeam(seam);
    }
    public static void main(String args[]){
        SeamCarver2 test= new SeamCarver2(new Picture("/Users/tiemo/Desktop/DeskStuff/Tiemo Profile2.jpg"));
        Picture t=new Picture(test.picture());
        t.show();
//      SCUtility.showEnergy(test);
        int i=0;
        Stopwatch timer = new Stopwatch();
    //  test.findVerticalSeam();
        StdOut.print("\n Method 1: "+timer.elapsedTime());
//      for(int f: test.findVerticalSeam()){
            //StdOut.print("Hi,"+f);
//          t.set( f, i ,new Color(255,255,255));
//          i++;
//      }
        i=0;
        int a[];
        t.show();
        timer=new Stopwatch();
        for(int l=0; l<450;l++){
            //System.out.print("\nit#"+l);
            if(l%200==0)test.picture().show();
            a=test.findVerticalSeam();
        //  for(int f:a) System.out.print(" "+f);
            test.removeVerticalSeam(a);
        }
        test.picture().show();
        StdOut.print("\n Method 2: "+timer.elapsedTime());
        for(int f:test.pic.FVSeam()){
            t.set( f, i ,new Color(255,0,255));
            i++;
        }
        t.show();
        i=0;
        /*
        for(int f: test.findVerticalSeam()){
            //StdOut.print("Hi,"+f);
            t.set( f, i ,new Color(255,0,255));
            i++;
        }
        t.show();*/
//      test.removeVerticalSeam(test.findVerticalSeam());
    //  test.picture().show();
    }
    /********************************************************************************************
     *           Helper Classes
     ********************************************************************************************/
    /********************************************************************************************
     *          Helper Functions
     ********************************************************************************************/
    private Picture ToPic(){
        int h = height();
        int w = width();
        Picture temp = new Picture(w,h);
        int i=0;
        for(Pixel p: pic){
            if((i-i%w)/w>=h) break;
            temp.set(i%w,(i-i%w)/w,p.c());
            i++;
        }
        return temp;
    }
    private boolean checkpoint(int x, int y){
        boolean flag=false;
        if(x > width()-1) flag=true;
        if(y > height()-1) flag=true;
        if( x < 0 ) flag=true;
        if(y<0) flag = true;
        if(flag) throw new IndexOutOfBoundsException();
        return !flag;
    }
    private void checkHSeam(int[] seam){
        if(seam==null) throw new NullPointerException();
        boolean flag=false;
        if(width()<=1) flag=true;
        if(seam.length!=width()) flag=true;
        if(!checkpoint(0,seam[0])) flag=true;
        for(int i=0;i<seam.length-2; i++){
            if(!checkpoint(i+1,seam[i+1]));
            if(Math.abs(seam[i+1]-seam[i])>1) flag=true;
        }
        if(flag) throw new IllegalArgumentException();
    }
    private void checkVSeam(int[] seam){
        if(seam==null) throw new NullPointerException();
        boolean flag=false;
        if(height()<=1) {
            flag=true;
        }
        if(seam.length!=height()) {
            System.out.print("WrongLength");
            flag=true;
        }
        if(!checkpoint(0,seam[0]))flag=true;
        for(int i=0;i<seam.length-2; i++){
            checkpoint(seam[i+1],i+1);
            if(Math.abs(seam[i+1]-seam[i])>1) {
                System.out.print("Seam!=+-1.\ni+1:" +seam[i+1]+"\ni: "+seam[i]+"\nindex: "+i);
                flag=true;
            }
        }
        if(flag) throw new IllegalArgumentException();
    }
    
}