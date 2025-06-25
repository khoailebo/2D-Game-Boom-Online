public class testMin {
    public static void main(String[] args) {
        int a = 0;
        for(int i = 0;i < 10;i++)
        a = Math.min(++a, 3);
        System.out.println(a);
    }
}
