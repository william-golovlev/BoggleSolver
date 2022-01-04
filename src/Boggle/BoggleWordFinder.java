package Boggle;

/* This is where you'll write your code to use a canonical graph-traversal algorithm to
 * solve a problem that at first may not seem like it's a graph problem at all.
 *
 * A note: This really is a fun one. If it gets to feel frustrating instead of fun, or if you feel
 * like you're  completely stuck, step back a bit and ask some questions.
 *
 * Spend a lot of time sketching out a plan, and figuring out which data structures might be
 * best for the various tasks, before you write any code at all.
 *
 * If you're not sure about how to approach this as a graph problem, feel free to ask questions.
 * (I won't give you all the answer, but...)
 * -Ben
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import edu.princeton.cs.algs4.TrieSET;
public class BoggleWordFinder {

    // some useful constants
    public static final String WORD_LIST = "src/words";
    public static final int ROWS = 5;
    public static final int COLUMNS = 5;
    public static final int SEED = 137;

    public BoggleWordFinder(String filename) throws Exception {
        TrieSET trie = new TrieSET();
        ArrayList<String> answers = new ArrayList<String>();

        initDictionary(filename, trie);
        BoggleBoard board = new BoggleBoard();
        System.out.println(board);

        for (int i = 0; i < board.getColumns()-1; i++) {
            for (int j = 0; j < board.getRows()-1; j++) {
                dfs(i, j, new String(), answers, board, trie);
                board = clearVisited(board);
            }
        }

        //output answers
        int count = 0;
        for (String str : answers) {
            System.out.println(str);
            count++;
        }
        System.out.println("Found " + count + " words found");

//        Set<String> updatedAnswer = new HashSet<String>(answers);
//        for (String str : updatedAnswer) {
//            System.out.println(str);
//        }
    }

    private static BoggleBoard clearVisited(BoggleBoard board) {
        for (int i = 0; i < board.getColumns()-1; i++) {
            for (int j = 0; j < board.getRows()-1; j++) {
                board.setUnvisited(i,j);
            }
        }
        return board;
    }

    public static void initDictionary(String filename, TrieSET trie) throws FileNotFoundException {
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        while(sc.hasNextLine()) {
            if(Character.isLowerCase(sc.nextLine().charAt(0))) {
                trie.add(sc.nextLine());
            }
        }
    }

    private static ArrayList<String> checkDictionary(String str, BoggleBoard board, TrieSET trie, ArrayList<String> answers) {
        for(String word : trie.keysThatMatch(str)) {
            if(word.equals(str)) {
                //System.out.println(str);
                if(!answers.contains(str)) answers.add(str);
            }
        }
        return answers;
    }

    private static boolean isNull(TrieSET trie, String word) {
        int i = 0;
        for (String str: trie.keysWithPrefix(word)) {
            i++;
        }
        return i == 0 ? true : false;
    }

    public static void dfs(int i, int j, String str, ArrayList<String> answers, BoggleBoard board, TrieSET trie) {
        if (i < 0 || j < 0 || i > board.getColumns()-1 || j > board.getRows()-1 || board.isVisited(i, j)) return;
        str += board.getCharAt(i,j);
        if(isNull(trie, str)) return;
        board.setVisited(i,j);

        answers = checkDictionary(str, board, trie, answers);
        //north
        dfs(i, j+1, str, answers, board, trie);
        //NW
        dfs(i-1, j+1, str, answers, board, trie);
        //west
        dfs(i-1, j, str, answers, board, trie);
        //SW
        dfs(i-1, j-1, str, answers, board, trie);
        //south
        dfs(i, j-1, str, answers, board, trie);
        //SE
        dfs(i+1, j-1, str, answers, board, trie);
        //east
        dfs(i+1, j, str, answers, board, trie);
        //NE
        dfs(i+1, j+1, str, answers, board, trie);

        board.setUnvisited(i,j);
        //return; reaches end and automatically returns
    }


    public static void main(String[] args) throws Exception {
        System.out.println(new File("words").getAbsolutePath());
        BoggleWordFinder bwf = new BoggleWordFinder("D:\\Desktop\\Fall 2021\\CIS27 D&S\\Boggle Solver\\src\\words");
    }
}
