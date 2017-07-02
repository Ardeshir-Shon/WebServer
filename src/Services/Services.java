package Services;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ardeshir Shon on 2/24/2017.
 */
public interface Services {
    public String GET(Map param);
    public String POST(Map urlParam,Map bodyParam);
}
