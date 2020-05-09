package be.renaud11232.plugins.sqleconomy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryPreparer {

    private final Map<String, Double> doubleParameters;
    private final Map<String, String> stringParameters;
    private final Set<String> allParameters;

    public QueryPreparer() {
        this.doubleParameters = new HashMap<>();
        this.stringParameters = new HashMap<>();
        this.allParameters = new HashSet<>();
    }

    public void setDouble(String name, double value) {
        doubleParameters.put(name, value);
        allParameters.add(name);
    }

    public void setString(String name, String value) {
        stringParameters.put(name, value);
        allParameters.add(name);
    }

    public PreparedStatement prepare(Connection connection, String query) throws SQLException {
        Map<String, Integer> paramPosition = new HashMap<>();
        String regex = "\\{\\{(" + allParameters.stream().map(Pattern::quote).collect(Collectors.joining("|")) + ")\\}\\}";
        Matcher matcher = Pattern.compile(regex).matcher(query);
        int i = 1;
        while(matcher.find()) {
            paramPosition.put(matcher.group(1), i++);
        }
        String newQuery = query.replaceAll(regex, "?");
        PreparedStatement statement = connection.prepareStatement(newQuery);
        for(String paramName : paramPosition.keySet()) {
            int paramPos = paramPosition.get(paramName);
            if(doubleParameters.containsKey(paramName)) {
                statement.setDouble(paramPos, doubleParameters.get(paramName));
            } else {
                statement.setString(paramPos, stringParameters.get(paramName));
            }
        }
        return statement;
    }


}
