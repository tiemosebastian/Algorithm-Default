import java.awt.*;
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
		this.pic=new Color[h*w];
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				this.pic[map(i,j)] = pic.get(j, i);
			}
		}
		edgeto=new int[h*w+2];
		disto=new int[h*w+2];
		end=h*w;
		start=h*w+1;
		en=new int[h*w+2];
		initEnergy();
	}
	private int map(int i, int j){
		return i*w+j;
	}
	private int mapx(int p){
		return p%w;
	}
	private int mapy(int p){
		return (p-p%w)/w;
	}
	
	/**
	 * // current picture
	 * @return
	 */
	public Picture picture(){
	   return ToPic();
	}
	private Picture ToPic(){
		Picture P = new Picture(w,h);
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				P.set(j, i, pic[map(i,j)]);
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
		checkpoint(x,y);
		return (double) en[map(y,x)];
	}
	private void initEnergy(){
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				en[map(i,j)]=calcEnergy(i,j);
			}
		}
	}
	private int calcEnergy(int i, int j){
		if(i==0||j==0||i==h-1||j==w-1) return BORDER;
		return (int) (gradx(i,j)+grady(i,j));
	}
	/**
	 * sequence of indices for horizontal seam
	 * @return
	 */
	public int[] findHorizontalSeam(){
		FHS();
		int p=end;
		int HS[]=new int[w];
		while(mapx(p)>=0){
			HS[mapx(p)]=disto[p];
			p=edgeto[p];
		}
		return HS;
	}
	/**
	 * // sequence of indices for vertical seam
	 * @return
	 */
	public int[] findVerticalSeam(){
		FVS();
		int p=end;
		int VS[] = new int[h];
		while(mapy(p)>0){
			p=edgeto[p];
			VS[mapy(p)]=mapx(p);
		}
		return VS;
	}	
	private void FVS(){
		IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(h*w+3);
		for(int p=0; p<h*w+1;p++) disto[p]=inf;
		
		pq.insert(start, 0);
	 	while (!pq.isEmpty()){
	 		int p = pq.delMin();
		 	for(int q: vadj(p)){
		 		relax(p,q,pq);
		 	}
	 	}
	}
	private void FHS(){
		IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(h*w+3);
		for(int p=0; p<h*w;p++) disto[p]=inf;
		
		pq.insert(start, 0);
	 	while (!pq.isEmpty()){
	 		int p = pq.delMin();
		 	for(int q: hadj(p)){
			 	relax(p,q,pq);
		 	}
		 }
	}
	private void relax(int from, int p, IndexMinPQ<Integer> pq){
		if (disto[p] > disto[from] + en[p])	{
			disto[p] = disto[from] + en[p];
			edgeto[p] = from;
			if (pq.contains(p)) pq.decreaseKey(p, disto[p]);
			else pq.insert (p, disto[p]);
		}
	}
	
	private Bag<Integer> vadj(int p){
		Bag<Integer> VA = new Bag<Integer>();
		if(p==start) {
			for(int v=0;v<w;v++){
				VA.add(v);
			}
		}
		else if(mapy(p)==h-1) VA.add(end);
		else if(p==end) return VA;
		int i=mapy(p)+1,j=mapx(p);				
		if(checkpoint(i,j+1)) VA.add(map(i,j+1));
		if(checkpoint(i,j-1)) VA.add(map(i,j-1));
		if(checkpoint(i,j)) VA.add(map(i,j));	
		return VA;
	}
	private Bag<Integer> hadj(int p){
		Bag<Integer> HA = new Bag<Integer>();
		if(p==start) {
			for(int q=0;q<h;q++){
				HA.add(map(q,0));
			}
		}
		else if(mapx(p)==w-1) HA.add(end);
		else if(p==end) return HA;
		int i=mapy(p),j=mapx(p)+1;				
		if(checkpoint(i+1,j)) HA.add(map(i+1,j));
		if(checkpoint(i-1,j)) HA.add(map(i-1,j));
		if(checkpoint(i,j)) HA.add(map(i,j));	
		return HA;
	}
	/**
	 * // remove horizontal seam from current picture
	 * @param seam
	 */
	public void removeHorizontalSeam(int[] seam){
		checkHSeam(seam);
		int min=inf;
		int max=0;
		for(int i:seam) {
			if(i<min) min=i;
			if(i>max) max=i;
		}
	//	System.out.printf("\nMin: %d\nMax: %d",min,max);
		System.arraycopy(pic, 0, pic, 0, min*w);
		for(int j=0;j<w;j++){
			for(int i=seam[j];i<max;i++){
				pic[map(i,j)]=pic[map(i+1,j)];
		//		System.out.printf("\ni: %d j: %d val: %d",i,j,pic[map(i+1,j)].getBlue());
			}
		}
		System.arraycopy(pic, (max+1)*w, pic, (max)*w, (h-max-1)*w);
		h--;
		for(int j=0;j<w;j++){
			for(int i=seam[j];i<h;i++){
				en[map(i,j)]=calcEnergy(i,j);
				en[map(i,j)]=calcEnergy(i,j);
			}
		}
	}
	/**
	 * // remove vertical seam from current picture
	 */
	public void removeVerticalSeam(int[] seam){
		checkVSeam(seam);
		//Color P[]=new Color[w*h-1];
		Color temp[]=new Color[(w-1)*h];
		for(int i=0;i<h;i++){
			System.arraycopy(pic,i*w, temp,i*(w-1), seam[i]);
			System.out.print("\ni:"+i+" w: "+w+"seam[i]"+seam[i]+"piclength: "+pic.length);
			System.arraycopy(pic,i*w+seam[i],pic,i*(w-1)+seam[i]-1,w-seam[i]);
			System.arraycopy(temp,i*(w-1),pic,i*(w-1),seam[i]);
		}
		/*for(int j=0;j<w;j++){
			for(int i=0;i<h-1;i++){
				System.arraycopy(pic,i*w, pic,i*(w-1), seam[i]);
				if(i>=seam[j]) P[i*(w)+j]=pic[map(i,j+1)];
				else P[i*(w)+j]=pic[map(i,j)];
			}
		}
		pic=P;*/
		w--;
		refresh();
		for(int j=0;j<w;j++){
			for(int i=seam[j];i<h;i++){
				en[map(i,j)]=calcEnergy(i,j);
				en[map(i,j+1)]=calcEnergy(i,j+1);
			}
		}
	}
	private void refresh(){
		end=h*w;
		start=h*w+1;
	}
	/*************************************************************************************************************************************************
	 * 
	 * @param l
	 * @param r
	 * @param u
	 * @param d
	 ************************************************************************************************************************************************/
	private double gradx(int i, int j){
		int r1 = pic[map(i,j-1)].getRed();
		int b1 = pic[map(i,j-1)].getBlue();
		int g1 = pic[map(i,j-1)].getGreen();
		int r2 = pic[map(i,j+1)].getRed();
		int b2 = pic[map(i,j+1)].getBlue();
		int g2 = pic[map(i,j+1)].getGreen();
	   
		return sq(r1-r2)+sq(b1-b2)+sq(g1-g2);
	}
	private double grady(int i, int j){
		int r1 = pic[map(i-1,j)].getRed();
		int b1 = pic[map(i-1,j)].getBlue();
		int g1 = pic[map(i-1,j)].getGreen();
		int r2 = pic[map(i+1,j)].getRed();
		int b2 = pic[map(i+1,j)].getBlue();
		int g2 = pic[map(i+1,j)].getGreen();
	   
		return sq(r1-r2)+sq(b1-b2)+sq(g1-g2);
	}
	private double sq(double x){
		return Math.pow(x, 2);
	}
	private boolean checkpoint(int x, int y){
		boolean flag=false;
		if(x > w-1) flag=true;
		if(y > h-1) flag=true;
		if( x < 0 ) flag=true;
		if(y<0) flag = true;
		return !flag;
	}
	private void checkHSeam(int[] seam){
		if(seam==null) throw new NullPointerException();
		boolean flag=false;
		if(w<=1) flag=true;
		if(seam.length!=w) flag=true;
		if(!checkpoint(0,seam[0])) flag=true;
		for(int i=0;i<seam.length-1; i++){
			if(!checkpoint(seam[i+1],i+1));
			if(Math.abs(seam[i+1]-seam[i])>1) flag=true;
		}
		if(flag) throw new IllegalArgumentException();
	}
	private void checkVSeam(int[] seam){
		if(seam==null) throw new NullPointerException();
		boolean flag=false;
		if(w<=1) flag=true;
		if(seam.length!=h) flag=true;
		if(!checkpoint(0,seam[0])) flag=true;
		for(int i=0;i<seam.length-1; i++){
			if(!checkpoint(i+1,seam[i+1]));
			if(Math.abs(seam[i+1]-seam[i])>1) flag=true;
		}
		if(flag) throw new IllegalArgumentException();		
		if(flag) throw new IllegalArgumentException();
	}
	public static void main(String args[]){
		/*Picture pic=new Picture(10,10);
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				pic.set(j,i,new Color(i*10+j,i+j,i*10+j));
			}
		}
		SeamCarver2 testhremove=new SeamCarver2(pic);
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				System.out.printf("%02d ",pic.get(j, i).getBlue());
			}
			System.out.println("");
		}
		System.out.println("\n\n");
		testhremove.removeVerticalSeam(new int[]{2,3,4,5,6,7,8,8,8,7});
		testhremove.removeVerticalSeam(new int[]{2,3,4,5,6,7,8,8,8,7});
		System.out.println("\n\n");
		for(int i=0;i<10;i++){
			for(int j=0;j<8;j++){
				System.out.printf("%02d ",testhremove.picture().get(j, i).getBlue());
			}
			System.out.println("");
		}
		/**/SeamCarver2 test= new SeamCarver2(new Picture("/Users/tiemo/Desktop/Tiemo Profile2.jpg"));
		Picture t=new Picture(test.picture());
		int a[]=test.findVerticalSeam();
		int i=0;
		for(int f:a){
			t.set(f, i ,new Color(255,0,255));
			i++;
		}
		t.show();
		for(int l=0; l<50;l++){
			if(l%200==1)test.picture().show();
			test.removeVerticalSeam(test.findVerticalSeam());
		}
		test.picture().show();/**/
	}
}
