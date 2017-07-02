package Services;

import java.util.Map;

/**
 * Created by Ardeshir Shon on 2/24/2017.
 */
public class NameFamily implements Services {
    @Override
    public String GET(Map param) {
        String name=(String) param.get("name");
        String fname=(String)param.get("familyName");
        return "<ul style=\"list-style-type:circle\">\n" +
                "  <li>name is :"+name+"</li>\n" +
                "  <li>family name is :"+fname+"</li>\n" +
                "</ul>" +
                "</br>" +
                "<i>we are in services package and found this class using " +
                "reflection</i>";
    }

    @Override
    public String POST(Map urlParam, Map bodyParam) {
        String name=(String) urlParam.get("name");
        String fname=(String)urlParam.get("familyName");
        return "<ul style=\"list-style-type:circle\">\n" +
                "  <li>name is :"+name+"</li>\n" +
                "  <li>family name is :"+fname+"</li>\n" +
                "</ul>" +
                "</br>" +
                "<i>we are in services package and found this class using " +
                "reflection</i>";
    }
}
