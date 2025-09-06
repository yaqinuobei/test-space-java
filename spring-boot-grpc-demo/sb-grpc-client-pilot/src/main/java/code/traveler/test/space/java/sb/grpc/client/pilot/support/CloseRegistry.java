package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *@description 关闭注册
 *@author panda
 *@time 2023/7/30 11:42
 */
public class CloseRegistry {

    //关闭监听集合
    private static List<CloseListener> closeListeners = Collections.synchronizedList(new ArrayList<>());

    /**
     * @description 注册关闭监听
     * @author panda
     * @param listener
     * @return void
     * @time 2023/7/30 11:42
     */
   public static void registry(CloseListener listener){
       closeListeners.add(listener);
   };

   /**
    * @description 对关闭的监听进行排序
    * @author panda
    * @param
    * @return java.util.List<code.traveler.test.space.java.sb.grpc.client.pilot.support.CloseListener>
    * @time 2023/7/30 11:42
    */
   public static List<CloseListener> sortedCloseListeners(){
       return closeListeners.stream().sorted(new Comparator<CloseListener>() {
           @Override
           public int compare(CloseListener o1, CloseListener o2) {
               return o1.getCloseOrder().compareTo(o2.getCloseOrder());
           }
       }).collect(Collectors.toList());
   }
}
