package nil.ed.onlineexam.util;

public class PageUtils {
    public static void checkPageParam(int pageNo, int pageSize){
        if (pageSize == 0 || pageNo * pageSize < 0){
            throw new IllegalArgumentException("分页参数不合法!");
        }
    }

    public static int calPageStart(int pageNo, int pageSize){
        return pageNo*pageSize;
    }
}
