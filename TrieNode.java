import java.util.Iterator;

public class TrieNode implements Iterable<String> {
    private final int R;        // Boggle
    private Node root;      // root of trie
    private int N;          // number of keys in trie

    // R-way trie node
    public class Node {
        private Node[] next = new Node[R];
        private boolean isString;
        
        public Node[] next(){
            return next;
        }
        public boolean isString(){
            return isString;
        }
    }

    /**
     * Initializes an empty set of strings.
     */
    public TrieNode() {
        R = 256;
    }
    public TrieNode(int R){
        this.R = R;
    }
    public Node root(){
        return root;
    }
    /**
     * Does the set contain the given key?
     * @param key the key
     * @return <tt>true</tt> if the set contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return true;
    }
    public Node contains(String key, Node start, int count) {
        Node x = get(start, key, count);
        return x;
    }
    public boolean isWord(String key, Node start, int count) {
        Node x = get(start, key, count);
        if (x == null) return false;
        return x.isString;
    }
    public boolean isWord(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) % R;
        return get(x.next[c], key, d+1);
    }

    /**
     * Adds the key to the set if it is not already present.
     * @param key the key to add
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void add(String key) {
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) N++;
            x.isString = true;
        }
        else {
            int c = key.charAt(d) % R;
            x.next[c] = add(x.next[c], key, d+1);
        }
        return x;
    }

    /**
     * Returns the number of strings in the set.
     * @return the number of strings in the set
     */
    public int size() {
        return N;
    }

    /**
     * Is the set empty?
     * @return <tt>true</tt> if the set is empty, and <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all of the keys in the set, as an iterator.
     * To iterate over all of the keys in a set named <tt>set</tt>, use the
     * foreach notation: <tt>for (Key key : set)</tt>.
     * @return an iterator to all of the keys in the set
     */
    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    /**
     * Returns all of the keys in the set that start with <tt>prefix</tt>.
     * @param prefix the prefix
     * @return all of the keys in the set that start with <tt>prefix</tt>,
     *     as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the set that match <tt>pattern</tt>,
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the set that match <tt>pattern</tt>,
     *     as an iterable, where . is treated as a wildcard character.
     */  
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<String>();
        StringBuilder prefix = new StringBuilder();
        collect(root, prefix, pattern, results);
        return results;
    }
        
    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.isString)
            results.enqueue(prefix.toString());
        if (d == pattern.length())
            return;
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the set that is the longest prefix of <tt>query</tt>,
     * or <tt>null</tt>, if no such string.
     * @param query the query string
     * @throws NullPointerException if <tt>query</tt> is <tt>null</tt>
     * @return the string in the set that is the longest prefix of <tt>query</tt>,
     *     or <tt>null</tt> if no such string
     */
    public String longestPrefixOf(String query) {
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        return query.substring(0, length);
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of length length
    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.isString) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d+1, length);
    }

    /**
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.isString) N--;
            x.isString = false;
        }
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.isString) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }


    /**
     * Unit tests the <tt>TrieSET</tt> data type.
     */
    public static void main(String[] args) {
        char c = 'M';
        c -= 10;
        StdOut.print("Character: " + c + " has the numerical value: " + (int) 'B');
        /*
        TrieSET set = new TrieSET();
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            set.add(key);
        }

        // print results
        if (set.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : set) {
                StdOut.println(key);
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(set.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"xshellsort\"):");
        StdOut.println(set.longestPrefixOf("xshellsort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : set.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shortening\"):");
        for (String s : set.keysWithPrefix("shortening"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : set.keysThatMatch(".he.l."))
            StdOut.println(s);*/
    }
}

