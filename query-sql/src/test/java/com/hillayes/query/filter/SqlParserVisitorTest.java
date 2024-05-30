package com.hillayes.query.filter;

import com.hillayes.query.filter.parser.FilterParser;
import org.junit.jupiter.api.Test;

public class SqlParserVisitorTest {
    @Test
    public void test() throws Exception {
        SqlParserVisitor visitor = new SqlParserVisitor();
        String filter = "a.property3 eq 3.9 and not isnull(property1) and (property2 eq 2 or (not property4 eq 4 or contains(property5, '5')))";
        //String filter = "property3 eq 3 and not isnull(property1) and (property2 eq 2 or (not property4 eq 4 or property5 eq '5'))";
        FilterParser.parse(filter).jjtAccept(visitor, null);

        System.out.println(filter);
        System.out.println(visitor.getQuery());
    }
}
