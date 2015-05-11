
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
        for (String w: dictionary){
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
        StringBuilder word = new StringBuilder(M*N);
        boolean visited[] = new boolean[M*N];
       // Stopwatch timer = new Stopwatch();
        for (int i = 0; i < M*N; i++){
            word.delete(0, M*N);
            DFS(0, i,
                    board.getLetter(mapy(i), mapx(i)), 
                    word, 
                    hits, 
                    board, 
                    visited, 
                    dict.root());
            visited[i] = false;
        }
     //   StdOut.print("\n\n Time elapsed: " + timer.elapsedTime());
        return hits;
    }
    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * (You can assume the word contains only the uppercase letters A through Z.)
     * @param word
     * @return
     */
    public int scoreOf(String word){
        int score = word.length();
        int qus=0;
        for (int i = 0; i < score; i++) {
            if (word.charAt(i) == 'Q') {
                if ( word.charAt(i+1) == 'U') {
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
   /* private int DFS(int startpos, TrieSET hits, BoggleBoard board){
        
        Stack<Integer> DFS = new Stack<Integer>();
        StringBuilder word = new StringBuilder(M*N);
        int pos[] = new int[M*N];
        boolean visited[] = new boolean[M*N];
        int count = 0;
        
        pos[count] = startpos;
        DFS.push(pos[count]);
        
        StdOut.print("\n\nStarting at: (" + mapx(pos[0]) + ", " + mapy(pos[0]) + ")");
        
        while (!DFS.isEmpty()) {
            
            StdOut.println("\n\nStack:" + DFS.toString());
            
            pos[count] = DFS.pop();
            
            StdOut.print("(" + mapx(pos[count]) + ", " + mapy(pos[count]) + ") number = " +count+ "\n");
            
            if (visited[pos[count]]) {
                StdOut.println("already visited" + DFS.isEmpty());
                continue;
            }
            
            if(count < word.toString().length()) word.setCharAt(count, board.getLetter(mapy(pos[count]), mapx(pos[count])));
            else word.append(board.getLetter(mapy(pos[count]), mapx(pos[count])));
      
            if (dict.contains(word.toString())) {
                
                if(dict.isWord(word.toString())) {
                    hits.add(new String(word));
                    StdOut.println("added");
                }
                
                visited[pos[count]] = true;
                StdOut.println(new String(word));
                StdOut.println("hi + " + pos[count]);
                printvis(visited, 4);
        //        DFS(count+1, pos[count+1], word, hits, board, visited);
                count++;
            }
        }
        return adj(mapx(pos[count]), mapy(pos[count]), DFS);
    }
    private void DFS(int count, int pos, StringBuilder wrd, TrieSET hits, BoggleBoard board, boolean[] visited, TrieNode.Node Nod){
        //boolean vis[] = visited.clone();
        //StringBuilder word = new StringBuilder(wrd);
        StdOut.println(wrd.toString());
        Stack<Integer> DFS = new Stack<Integer>();
        adj(mapx(pos), mapy(pos), DFS);
        char next;
        while(!DFS.isEmpty()){
            StdOut.println("Stack: " + DFS.toString());
            StdOut.println("Word: " + new String(wrd));
            StdOut.println("Count: " + count);
            pos = DFS.pop();
            if(visited[pos]) continue;
            next = board.getLetter(mapy(pos), mapx(pos));
            StdOut.println("Next: " + next);
            if (Nod.next[next] != null) {
                TrieNode.Node Node = Nod.next[next];
                StringBuilder word = new StringBuilder(wrd);
                word.append(next);
                boolean vis[]=visited.clone();
                vis[pos]=true;
                if (Node.isString) {
                    hits.add(new String(word));
                    StdOut.println("added");
                } 
                StdOut.print("\n");
                DFS(count+1, pos, word, hits, board, vis, Node);
            }
        }
    }*/
    private void DFS(int count, int pos, char nxt, StringBuilder wrd, TrieSET hits, BoggleBoard board, boolean[] visited, TrieNode.Node Nod){
        if (Nod.next()[cti(nxt)] == null) return;
        //if (visited[pos]) return;
        TrieNode.Node Node = Nod.next()[cti(nxt)];
     //   StringBuilder word = new StringBuilder(wrd);
      //  word.append(nxt);
        if (nxt == 'Q') {
            Node = Node.next()[cti('U')];
     //       word.append('U');
        }
        boolean vis[] = visited.clone();
        vis[pos]=true;
        if (Node.isString()) {
    //        StdOut.println("added\n");
            hits.add(Node.getString());
        }
        Stack<Integer> DFS = new Stack<Integer>();
        adj(mapx(pos), mapy(pos), DFS);
        while (!DFS.isEmpty()){
            pos = DFS.pop();
            if (vis[pos]) continue;
            /*StdOut.println("Stack: " + DFS.toString());
            StdOut.println("Word: " + new String(word));
            StdOut.println("Count: " + count);
            StdOut.println("Next: " + board.getLetter(mapy(pos), mapx(pos)));
            StdOut.println("Position: (" + mapx(pos) + ", " + mapy(pos) + ")\n");*/
            DFS(count+1, 
                    pos,
                    board.getLetter(mapy(pos),mapx(pos)),
                    wrd,
                    hits,
                    board,
                    vis,
                    Node);
        }
    }
    private static int cti(char c){
        return (int) c % 26;
    }
    private int adj(int x, int y, Stack<Integer> S) {
        int count = 0;
        boolean flag=false;
        if (x > 0) {
           S.push(map(x-1, y));
           count++;
           if (y > 0) {
               S.push(map(x-1, y-1));
               S.push(map(x, y-1));
               count += 2;
               flag = true;
           }
           if (y < N-1) {
               S.push(map(x-1, y+1));
               S.push(map(x, y+1));
               count += 2;
               flag = true;
           }
        }
        if (x < M-1) {
            count++;
            S.push(map(x+1, y));
            if (y > 0) {
                S.push(map(x+1, y-1));
                count++;
                if (!flag) {
                        S.push(map(x,y-1));
                        count++;
                }
            }
            if (y < N-1) {
                 S.push(map(x+1, y+1));
                 count++;
                 if (!flag) {
                     S.push(map(x, y+1));
                     count+=2;
                 }
            }
         }
        return count;
    }
    private int map(int x, int y){
        return y * M + x;
    }
    private int mapx(int x){
        return x % M;
    }
    private int mapy(int y){
        return (y - y % M) / M;
    }
    private void printvis(boolean vis[], int M){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < vis.length / M; j++){
                if(vis[map(j,i)]) StdOut.print(" 1");
                else StdOut.print(" 0");
            }
            StdOut.print("\n");
        }
    }
    public static void main(String args[]){
        In in = new In("/Users/tiemo/Desktop/Boggle/boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("/Users/tiemo/Desktop/Boggle/boggle/board-points1250.txt");
        int score = 0;
        Stopwatch timer = new Stopwatch();
        int N = 10000;
        for (int i = 0; i < N; i++){
            solver.getAllValidWords(board);
            //if (i % 50 == 0) StdOut.println(i);
        }
        double Time = timer.elapsedTime();
     //   StdOut.printf("Boggle Solver Time at %d iterations: %f", N, timer.elapsedTime());
        BoggleBoard board1 = new BoggleBoard("/Users/tiemo/Desktop/Boggle/boggle/board-points100.txt");
        timer = new Stopwatch();
        for (int i = 0; i < N; i++){
            solver.getAllValidWords(board1);
          //  if (i % 50 == 0) StdOut.println(i);
        }
        StdOut.printf("Boggle Solver Time with 100 solutions at %d iterations: %f", N, timer.elapsedTime());
        StdOut.println("");
        StdOut.printf("Boggle Solver Time with 1250 solutions at %d iterations: %f", N, Time);
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.print("\n" + word);
            score += solver.scoreOf(word);
        }
     //   StdOut.println("\nScore = " + score);
        StdOut.println("\n Q-char" +board.getLetter(2, 1));
    }
}


