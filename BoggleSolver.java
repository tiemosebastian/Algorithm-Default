
public class BoggleSolver {
    private int M = 0;
    private int N = 0;
    private final TrieNode dict;
    private int[][] adj; 
    private BoggleBoard brd;
    private boolean[] visited;
    private SET<String> hits; 
   // private TrieNode.Node node;
    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
     * @param dictionary
     */
    public BoggleSolver(String[] dictionary) {
        dict = new TrieNode(26);
        for (String w: dictionary) {
            dict.add(w);
        }
    }
    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     * @param board
     * @return
     */
    
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        brd = board;
        if (M != board.rows() || N != board.cols()) {
            M = board.rows();
            N = board.cols();
            setadj();
        }
        hits = new SET<String>();
        visited = new boolean[M * N];
        
        for (int i = 0; i < M * N; i++) {
            if (dict.root().next()[cti(board.getLetter(mapy(i), mapx(i)))] == null) continue;
            DFS(0, i,
                    board.getLetter(mapy(i), mapx(i)), 
                    dict.root());
            visited[i] = false;
        }
        return hits;
    }
    private void setadj() {
        adj = new int[M * N][];
        for (int i = 0; i < M * N; i++) {
            adj[i] = adj(mapx(i), mapy(i));
        }
    }
    /*
     * Runs a Depth First Search on the adjacent fields of the BoggleBoard terminating a branch 
     *  if it is no longer in the trie representation of the dictionary (i.e. Node.next()[cti(nxt)]==null).
     */
    private void DFS(int count, int ps, char nxt, TrieNode.Node nod) {
        TrieNode.Node node = nod.next()[cti(nxt)];
        if (nxt == 'Q') {
            node = node.next()[cti('U')];
            if (node == null) return;
        }
        visited[ps] = true;
        if (node.isString()) {
            if (node.getString().length() > 2) {
                hits.add(node.getString());
            }
        }
        for (int pos : adj[ps]) {
            if (visited[pos]) continue;
            if (node.next()[cti(brd.getLetter(mapy(pos), mapx(pos)))] == null) continue;
            DFS(count+1, 
                    pos,
                    brd.getLetter(mapy(pos), mapx(pos)),
                    node);
        }
        visited[ps] = false;
    }
    /*
     * Calculates adjacent fields and returns them in a Stack for use in DFS
     *
     */
    private int[] adj(int x, int y) {
        Stack<Integer> S = new Stack<Integer>();
        boolean flag = false;
        if (x > 0) {
           S.push(map(x - 1, y));
           if (y > 0) {
               S.push(map(x - 1, y - 1));
               S.push(map(x, y - 1));
               flag = true;
           }
           if (y < M - 1) {
               S.push(map(x - 1, y + 1));
               S.push(map(x, y + 1));
               flag = true;
           }
        }
        if (x < N - 1) {
            S.push(map(x + 1, y));
            if (y > 0) {
                S.push(map(x + 1, y - 1));
                if (!flag) {
                        S.push(map(x, y - 1));
                }
            }
            if (y < M - 1) {
                 S.push(map(x + 1, y + 1));
                 if (!flag) {
                     S.push(map(x, y + 1));
                 }
            }
        }
        if (N == 1) {
            if (y > 0) {
                S.push(map(x, y - 1));
            }   
            if (y < M - 1) {
                S.push(map(x, y + 1));
            }
        }
        int[] ad = new int[S.size()];
        for (int i = 0; i < ad.length; i++) {
            ad[i] = S.pop();
        }
        return ad;
    }
    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * (You can assume the word contains only the uppercase letters A through Z.)
     * @param word
     * @return
     */
    public int scoreOf(String word) {
        if (!dict.isWord(word)) return 0;
        int score = word.length();
        int qus = 0;
        for (int i = 0; i < score; i++) {
            if (word.charAt(i) == 'Q') {
                if (word.charAt(i + 1) != 'U') {
                    return 0;
                }
            }
        }
        score = score + qus;
        if (score < 3) return 0;
        if (score + qus < 5) return 1;
        if (score + qus < 7) return score - 3;
        if (score + qus == 7) return 5;
        else return 11; 
    }
    /****************************************************************************************************
     *  Private Methods
     ***************************************************************************************************/
   /**
    * Turns character representation of capital letter into a reproducible integer via the modulo operator
    * Works because the 26 capital letters are adjacent in the char table. 
    * @param c
    * @return
    */
    private static int cti(char c) {
        return (int) c % 26;
    }
    /**
     * Maps (x, y) position of an array into a value in a one dimensional array
     * N.B.  Not to be confused with ij notation, that is x refers to the column, and y refers to the row.
     * @param x
     * @param y
     * @return
     */
    private int map(int x, int y) {
        return y * N + x;
    }
    private int mapx(int x) {
        return x % N;
    }
    private int mapy(int y) {
        return (y - y % N) / N;
    }
    /**
     * Unit testing.
     * @param args
     */
    public static void main(String[] args) {
        In in = new In("/Users/tiemo/Desktop/Boggle/boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("/Users/tiemo/Desktop/Boggle/boggle/board-points1250.txt");
        BoggleBoard board1 = new BoggleBoard("/Users/tiemo/Desktop/Boggle/boggle/board-points100.txt");
        int N = 10000;
        Stopwatch timer = new Stopwatch();
        for (int i = 0; i < N; i++) {
            solver.getAllValidWords(board);
        }
        double time = timer.elapsedTime();
        timer = new Stopwatch();
        for (int i = 0; i < N; i++) {
            solver.getAllValidWords(board1);
        }
        StdOut.printf("Boggle Solver Time with 100 solutions at %d iterations: %f", N, timer.elapsedTime());
        StdOut.println("");
        StdOut.printf("Boggle Solver Time with 1250 solutions at %d iterations: %f", N, time);
    }
}


