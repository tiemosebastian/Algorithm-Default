public class SeamCarverTesting {
	/**
	 * 
	 */
	private void constructorImmutabilityTest(){
		SeamCarver the = new SeamCarver(Picture Picturetest=new Picture());
		Picture comp= new Picture(Picturetest);
		the.removeHorizontalSeam(the.findHorizontalSeam());
		the.removeVerticalSeam(the.findVerticalSeam());
		assert Picturetest.equals(comp);
		assert the.picture().equals(comp);
	}
	/** Test whether the initial picture matches the input.
	 * 
	 */
	private void initPictureTest(){
		SeamCarver the = new SeamCarver(new Picture);
		assert(the.picture().isequal(new Picture))
	}
	private void initWidthTest(){
		SeamCarver the = new SeamCarver(new Picture);
		assert the.width()==Picture.width();
	}
	private void initHeightTest(){
		SeamCarver the = new SeamCarver(new Picture);
		assert the.height()==Picture.height();
	}
	private void findHorizontalSeamReturnsValidSeam(){
		SeamCarver the = new SeamCarver(new Picture);
		int width = the.width();
		int seam[]=the.findHorizontalSeam();
		assert seam.length==width;
		assert checkpoint(0,seam[0]);
		for(int i=0;i<seam.length-2; i++){
			assert checkpoint(i+1,seam[i+1]);
			assert Math.abs(seam[i+1]-seam[i])<=1;
		}
	}
	private void findVerticalSeamReturnsValidSeam(){
		SeamCarver the = new SeamCarver(new Picture);
		int height = the.height();
		int seam[]=the.findVerticalSeam();
		assert seam.length==height;
		assert checkpoint(0,seam[0]);
		for(int i=0;i<seam.length-2; i++){
			assert checkpoint(i+1,seam[i+1]);
			assert Math.abs(seam[i+1]-seam[i])<=1;
		}
	}
	private boolean checkpoint(int x, int y){
		boolean flag;
		if(x > width()-1) flag=true;
		if(y > height()-1) flag=true;
		if( x < 0 ) flag=true;
		if(y<0) flag = true;
		if(flag) throw new IndexOutOfBoundsException();
		return !flag;
	}
	private void removeHorizontalSeamDecrementsWidth(){
		SeamCarver the = new SeamCarver(new Picture);
		int width = the.width();
		the.removeVerticalSeam(new int[the.height()]);
		assert width-the.width()==1;
	}
	private void removeVerticalSeamDecrementsHeight(){
		SeamCarver the = new SeamCarver(new Picture);
		int height = the.height();
		the.removeHorizontalSeam(new int[the.width()]);
		assert height-the.height()==1;
	}
}
