import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class program {
    //0：空 1：> 2: < 3:=
    //算符优先关系表
    /*   + * i ( )  #
     * +
     * *
     * i
     * (
     * )
     * #
     */
    private static int[][] table = {
            {1, 2, 2, 2, 1, 1},
            {1, 1, 2, 2, 1, 1},
            {1, 1, 0, 0, 1, 1},
            {2, 2, 2, 2, 3, 0},
            {1, 1, 0, 0, 1, 1},
            {2, 2, 2, 2, 0, 3}};
    private static char[] VN = {'E', 'T', 'F'};
    private static char[] VT = {'+', '*', 'i','(',')','#'};
    private static Set<String> grammar = new LinkedHashSet<>();
    private static Stack stack = new Stack();
    private static List<Character> sentence = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        String name = args[0];
        BufferedReader text = new BufferedReader(new FileReader(name));
//         BufferedReader text = new BufferedReader(new FileReader("D:\\buaa\\2020autumn\\编译原理与技术\\OPG\\test.txt"));
        init();
        int c;
        while((c=text.read())!=-1){
            if(c==10 || c==13) continue;
            sentence.add((char)c);
        }
        sentence.add('#');
//        System.out.println(sentence);
        specification();//对句子进行规约
        text.close();


    }
    public static void init(){
        grammar.add("N+N");
        grammar.add("N*N");
        grammar.add("(N)");
        grammar.add("i");
        stack.push('#');
    }
    ////判断是否是非终结符，不是非终结符返回0
    public static boolean is_VT(char c) {
        for(int i=0; i<VT.length;i++){
            if(c==VT[i]) return true;
        }
        return false;
    }
    //返回栈中首个终结符
    public static char peek_VT() {
        char c = (char)stack.get(stack.size() - 1);
        if(!is_VT(c)) {
            c = (char)stack.get(stack.size() - 2);
        }
        return c;
    }
    public static int find_VT_id(char c){
        for(int i=0;i<VT.length;i++){
            if(c==VT[i]){
                return i;
            }
        }
        return -1;
    }
    //规约
    public static void specification() {
        for(int i=0; i<sentence.size();i++){
            char rc = (char) sentence.get(i);
            if(is_VT(rc)){
                char lc=peek_VT();
                int l = find_VT_id(lc);
                int r = find_VT_id(rc);
                int relation = table[l][r];
                if(relation == 0) {
                    System.out.println("E");
                    return;
                }
                if(relation >= 2 && rc != '#'){
                    stack.push(rc);
                    System.out.println("I"+rc);
                }
                else if(rc == '#' && lc == '#') {
                    return;
                }
                else{
                    if(lc == 'i') {
                        stack.pop();
                        stack.push('N');
                    }
                    else {
                        if(stack.size() >= 4) {
                            StringBuilder sb = new StringBuilder();
                            sb.append((char)stack.pop());
                            sb.append((char)stack.pop());
                            sb.append((char)stack.pop());
                            if(grammar.contains(sb.reverse().toString())) {
                                stack.add('N');
                                //System.out.println(stack);
                            }
                            else {
                                System.out.println("RE");
                                return;
                            }
                        }
                        else {
                            System.out.println("RE");
                            return;
                        }
                    }
                    i--;
                    System.out.println("R");
                }
            }
            else{
                System.out.println("E");
                return;
            }

        }
    }



}
