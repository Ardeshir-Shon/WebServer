package Services;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ardeshir Shon on 2/24/2017.
 */
public class GetHelloWorld implements Services  {
    @Override
    public String GET(Map param) {
        return "<b>Hello from getHelloWorldService</b>" +
                "</br>" +
                "</br>" +
                "<i>we are in services package and found this class using " +
                "reflection";
    }

    @Override
    public String POST(Map urlParam, Map bodyParam) {
        return "<b>Hello from getHelloWorldService</b>" +
                "</br>" +
                "<i>we are in services package and found this class using " +
                "reflection";
    }
}
