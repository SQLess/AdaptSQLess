package Oracle;
/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/13 8:04
 */
import Connector.Result;

public class OracleChecker {
    /**
     * Check results to see if there is a logical bug according to implication oracle.
     * Return false if there is a logical bug, otherwise return true.
     *
     * Note that implication oracle cannot support error oracle.
     * You cannot have any errors in your results, otherwise, an exception will be thrown.
     *
     * @param originResult the original query result
     * @param mutatedResult the mutated query result
     * @param isUpper the flag indicating the type of comparison
     * @return true if there is no logical bug, false otherwise
     * @throws Exception if there is an error in the results
     */
    public static boolean Pinolo_check(Result originResult, Result mutatedResult, boolean isUpper) throws Exception {
        int cmp = originResult.compare(mutatedResult);
        // -2 indicates there's an error in one of the results
        if (cmp == -2) {
            System.out.println("Error in one of results");
//            return true;
//            throw new Exception("Error in one of the results");
        }

        if (cmp == 0) {
            return true;
        }

        if ((isUpper && cmp == -1) || (!isUpper && cmp == 1)) {
            return true;
        }

        return false;
    }
    public static boolean TLP_check(Result originResult, Result mutatedResult) throws Exception {
        //mutatedResult is union select result
        int cmp = originResult.compare(mutatedResult);
        // -2 indicates there's an error in one of the results
        if (cmp == -2) {
            throw new Exception("Error in one of the results");
        }
        if (cmp == 0) {
            return true;
        }

        return false;
    }
    public static boolean NoRec_check(Result originResult, Result mutatedResult) throws Exception {
        int cmp = originResult.compare(mutatedResult);
        // -2 indicates there's an error in one of the results
        if (cmp == -2) {
            throw new Exception("Error in one of the results");
        }
        if (cmp == 0) {
            return true;
        }

        return false;
    }

}
