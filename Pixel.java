import java.awt.Color;

public class Pixel{
		private int BORDER=195075;
		private Color c; //color 
		private int e; //energy of the pixel
		
		/**
		 * Saves energy and RGB value.
		 * @param c
		 * @param e
		 */
		public Pixel(Color c){
			this.c = c;
			e=BORDER;
		}
		public Pixel(Color c, int e){
			this.c = c;
			this.e=e;
		}
		public String toString(){
			return Integer.toString(e);
		}
		public Color c(){
			return c;
		}
		public int e(){
			return e;
		}
		/**
		 * calculates energy based on (hopefully) neighboring pixels.
		 * @param l
		 * @param r
		 * @param u
		 * @param d
		 */
		public void border(){
			e=BORDER;
		}
		public void energy(Pixel l, Pixel r, Pixel u, Pixel d){
			if(l==null||r==null||u==null||d==null) e=BORDER;
			e = (int) (gradx(l,r) + grady(u,d));
		}
		private double gradx(Pixel l, Pixel r){
			int r1 = l.c().getRed();
			int b1 = l.c().getBlue();
			int g1 = l.c().getGreen();
			int r2 = r.c().getRed();
			int b2 = r.c().getBlue();
			int g2 = r.c().getGreen();
		   
			return sq(r1-r2)+sq(b1-b2)+sq(g1-g2);
		}
		private double grady(Pixel u, Pixel d){
			int r1 = u.c().getRed();
			int b1 = u.c().getBlue();
			int g1 = u.c().getGreen();
			int r2 = d.c().getRed();
			int b2 = d.c().getBlue();
			int g2 = d.c().getGreen();
		   
			return sq(r1-r2)+sq(b1-b2)+sq(g1-g2);
		}
		private double sq(double x){
			return Math.pow(x, 2);
		}
	}
