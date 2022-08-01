import java.util.*;

public class RecentRequireTime {
    List<Integer> list = new LinkedList<>();
    HashSet<Integer> hashSet = new HashSet<>();
    int recentTime = 0;


    public RecentRequireTime() {
        this.recentTime = 0;
    }

    public int ping(int t) {
        this.list.add(t);
        hashSet.add(t);
        int returned = 0;
        if (t - 3000 <= list.get(0)) {
            returned = list.size();
        }
        if (list.size() > 2) {
            if (t - 3000 > list.get(list.size() - 2)) {
                returned = 1;
                if (!hashSet.contains(t - 3000)) {//分别分为包与不包的情况
                    returned = this.list.size() - binarySearch(this.list, 0, this.list.size() - 1, t - 3000) - 1;
                } else {
                    returned = this.list.size() - binarySearch(this.list, 0, this.list.size() - 1, t - 3000);
                }
            }
        }
        return returned;
    }

    public int binarySearch(List<Integer> list, int st, int end, int t) {
        while (st <= end) {
            int mid = (st + end) >> 1;
            if (list.get(mid) < t) st = mid + 1;
            else if (list.get(mid) > t) end = mid - 1;
            else return mid;
        }
        return end;
    }



    public static void main(String[] args) {
        RecentRequireTime recent=new RecentRequireTime();
        Scanner scanner=new Scanner(System.in);
        while (true){
            System.out.println("请输入t");
            int t=scanner.nextInt();
            System.out.println("结果为   "+recent.ping(t));
            System.out.println("list中的内容为   "+recent.list.toString());
        }
    }

}
