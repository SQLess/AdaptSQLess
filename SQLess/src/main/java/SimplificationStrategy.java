/**
 * 可以逐个删除的策略有
 * remove order by
 * remove group by
 * remove having
 * remove limit
 * remove where
 * remove column  （删除第x处，比较特殊，下表从0开始，开发原因）
 * remove bitop left
 * remove bitop right
 * remove logicalop left
 * remove logicalop right
 *
 * 全部删除的有：
 * remove with
 * remove use index
 * remove union left  （有危险）
 * remove union right
 * remove unuse def  （一次只删除一个）
 * remove sql hint
 */


public final class SimplificationStrategy{
    /**
     *Clause SImplification
     */
    public static final String REMOVEORDERBY = "REMOVEORDERBY";  //remove order by
    public static final String REMOVEWITH = "REMOVEWITH";  //remove with
    public static final String REMOVEUSEINDEX = "REMOVEUSEINDEX"; // remove use index
    public static final String REMOVEUNIONLEFT = "REMOVEUNIONLEFT";  //remove union left
    public static final String REMOVEUNIONRIGHT = "REMOVEUNIONRIGHT";  //remove union right
    public static final String REMOVEGROUPBY = "REMOVEGROUPBY";   // remove group by
    public static final String REMOVEHAVING = "REMOVEHAVING";   //remove having
    public static final String REMOVELIMIT = "REMOVELIMIT";   //remove limit
    public static final String REMOVEWHERE = "REMOVEWHERE";  //remove where

    /**
     * Column Simplification
     */
    public static final String REMOVECOLUMN = "REMOVECOLUMN";  //remove column

    /**
     * Function Simplification
     */

    /**
     * Subquery Simplification
     */
    public static final String REMOVEUNUSEDEF = "REMOVEUNUSEDEF";  //remove unuse def

    /**
     * Expression Simplification
     */
    public static final String REMOVEBITOPLEFT = "REMOVEBITOPLEFT";  //remove bitop left
    public static final String REMOVEBITOPRIGHT = "REMOVEBITOPRIGHT" ;  //remove bitop right
    public static final String REMOVELOGICALOPLEFT = "REMOVELOGICALOPLEFT" ;  //remove logicalop left
    public static final String REMOVELOGICALOPRIGHT = "REMOVELOGICALOPRIGHT" ;  //remove logicalop right

    /**
     * Optimizer HInts and other MOdifiers
     */
    public static final String REMOVESQLHINT = "REMOVESQLHINT";  //remove sql hint

}