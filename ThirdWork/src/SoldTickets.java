/**
 * 关于
 */
public class SoldTickets {
    public static void main(String[] args) {
        int[] pre ={2,2,4,5,7,8};
        System.out.println(buyTicketsTime(pre,1));
    }

    public static int buyTicketsTime(int[] need, int k){
        int time = 0;
        while(need[k]!=0){
            int n = 0;//记录一轮买票有多少人
            for(int loop = 0; loop<need.length&&need[k]!=0; loop++){
                if(need[loop]!=0) {
                    need[loop] -=1;
                    n++;

                }
            }



            time+=n;
        }

        return time;
    }


}
