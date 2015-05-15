
public class BoggleSolver {
    private int M;
    private int N;
    private final TrieNode dict;
   
    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
     * @param dictionary
     */
    public BoggleSolver(String[] dictionary) {
        dict = new TrieNode(26);
        Stopwatch timer = new Stopwatch();
        for (String w: dictionary) {
            dict.add(w);
        }
        StdOut.println("Dictionary initiation time: " + timer.elapsedTime());
        
    }
    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     * @param board
     * @return
     */
    
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        M = board.rows();
        N = board.cols(); 
        TrieSET hits = new TrieSET();
        boolean[] visited = new boolean[M * N];
        for (int i = 0; i < M * N; i++) {
            DFS(0, i,
                    board.getLetter(mapy(i), mapx(i)), 
                    hits, 
                    board, 
                    visited, 
                    dict.root());
            visited[i] = false;
        }
        return hits;
    }
    /*
     * Runs a Depth First Search on the adjacent fields of the BoggleBoard terminating a branch 
     *  if it is no longer in the trie representation of the dictionary (i.e. Node.next()[cti(nxt)]==null).
     */
    private void DFS(int count, int ps, char nxt, TrieSET hits, BoggleBoard board, boolean[] visited, TrieNode.Node nod) {
        if (nod.next()[cti(nxt)] == null) return;
        int pos = ps;
        TrieNode.Node node = nod.next()[cti(nxt)];
        if (nxt == 'Q') {
            node = node.next()[cti('U')];
            if (node == null) return;
        }
        boolean[] vis = visited.clone();
        vis[pos] = true;
        if (node.isString()) {
            if (node.getString().length() > 2) {
                hits.add(node.getString());
            }
        }
        Stack<Integer> DFS = new Stack<Integer>();
        adj(mapx(pos), mapy(pos), DFS);
        while (!DFS.isEmpty()) {
            pos = DFS.pop();
            if (vis[pos]) continue;
            DFS(count+1, 
                    pos,
                    board.getLetter(mapy(pos), mapx(pos)),
                    hits,
                    board,
                    vis,
                    node);
        }
    }
    /*
     * Calculates adjacent fields and returns them in a Stack for use in DFS
     *
     */
    private void adj(int x, int y, Stack<Integer> S) {
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
    }
    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * (You can assume the word contains only the uppercase letters A through Z.)
     * @param word
     * @return
     */
    public int scoreOf(String word) {
        if (!dict.contains(word)) return 0;
        int score = word.length();
        int qus = 0;
        for (int i = 0; i < score; i++) {
            if (word.charAt(i) == 'Q') {
                if (word.charAt(i+1) == 'U') {
                    qus++;
                    i++;
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
     * Testing method for the visited array.
     * @param vis
     * @param M
     */
    private void printvis(boolean[] vis) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < vis.length / M; j++) {
                if (vis[map(j, i)]) StdOut.print(" 1");
                else StdOut.print(" 0");
            }
            StdOut.print("\n");
        }
    }
    /**
     * Unit testing.
     * @param args
     */
    public static void main(String[] args) {
        In in = new In("/Users/tiemo/Desktop/Boggle/boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("/Users/tiemo/Desktop/Boggle/boggle/board-q.txt");
        int score = 0;
        Stopwatch timer = new Stopwatch();
        int N = 1;
        for (int i = 0; i < N; i++) {
            solver.getAllValidWords(board);
            //if (i % 50 == 0) StdOut.println(i);
        }
        double time = timer.elapsedTime();
     //   StdOut.printf("Boggle Solver Time at %d iterations: %f", N, timer.elapsedTime());
        BoggleBoard board1 = new BoggleBoard("/Users/tiemo/Desktop/Boggle/boggle/board-points100.txt");
        timer = new Stopwatch();
        for (int i = 0; i < N; i++) {
            solver.getAllValidWords(board1);
          //  if (i % 50 == 0) StdOut.println(i);
        }
        StdOut.printf("Boggle Solver Time with 100 solutions at %d iterations: %f", N, timer.elapsedTime());
        StdOut.println("");
        StdOut.printf("Boggle Solver Time with 1250 solutions at %d iterations: %f", N, time);
        for (String word : solver.getAllValidWords(board)) {
            StdOut.print("\n" + word);
            score += solver.scoreOf(word);
        }
        StdOut.println("\nScore = " + score);
    }
}


