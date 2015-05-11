
public class Test {
	/** Checks to ensure the relation in question is an equivalence relation
	 *  N.B. assumes a return of -1 implies not related.
	 */
	public static void main(String[] args){
		System.out.println(args[0]);
		int len = args[0].length();
		char rev[] = new char[len]; 
		String rev1="";
		String rev2="";
		for(int i=0;i<len;i++){
			rev[i]=args[0].charAt(len-i-1);
			rev1=rev1.concat(Character.toString(args[0].charAt(len-i-1)));
			rev2=rev2.concat(args[0].substring(len-i-1,len-i));
			}
		String reversed = new String(rev);
		System.out.println(rev);
		System.out.println(rev1);
		System.out.println(rev2);
		System.out.println(reversed);
	}
}
