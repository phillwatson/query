/**
 * [Phillip Watson] ("COMPANY") CONFIDENTIAL Unpublished Copyright © 2019-2020 Phillip Watson,
 * All Rights Reserved.
 * <p>
 * NOTICE: All information contained herein is, and remains the property of COMPANY. The
 * intellectual and technical concepts contained herein are proprietary to COMPANY and may be
 * covered by U.K. and Foreign Patents, patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or reproduction of this material is strictly
 * forbidden unless prior written permission is obtained from COMPANY. Access to the source code
 * contained herein is hereby forbidden to anyone except current COMPANY employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements explicitly covering
 * such access.
 * <p>
 * The copyright notice above does not evidence any actual or intended publication or disclosure of
 * this source code, which includes information that is confidential and/or proprietary, and is a
 * trade secret, of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR
 * PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF
 * COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES.
 * THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY
 * ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL
 * ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.hillayes.query.filter;

import com.hillayes.query.filter.parser.*;
import com.hillayes.query.predicate.PropertyPredicate;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class FilterParserVisitor2Test {
    @Test
    public void test() throws Exception {
        MockQueryContext context = new MockQueryContext();

        Node filter = FilterParser.parse(context,
            "property3 eq 3 and not property1 le 1 and property2 eq 2 or property4 eq 4");
        QueryPredicate queryPredicate = filter.jjtAccept(new Visitor(), null);
        System.out.println(queryPredicate);
    }

    private static class Visitor implements FilterParserVisitor {
        @Override
        public com.hillayes.query.filter.QueryPredicate visit(SimpleNode aNode, com.hillayes.query.filter.QueryPredicate aData) {
            // never used
            throw new RuntimeException("Didn't expect this visit.");
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTparse aNode, com.hillayes.query.filter.QueryPredicate aData) {
            QueryPredicate result = null;
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                result = aNode.jjtGetChild(i).jjtAccept(this, result);
            }
            return result;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTOr aNode, com.hillayes.query.filter.QueryPredicate aData) {
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                QueryPredicate predicate = aNode.jjtGetChild(i).jjtAccept(this, aData);
                aData = (aData == null) ? predicate : aData.or(predicate);
            }
            return aData;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTAnd aNode, com.hillayes.query.filter.QueryPredicate aData) {
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                QueryPredicate predicate = aNode.jjtGetChild(i).jjtAccept(this, aData);
                aData = (aData == null) ? predicate : aData.and(predicate);
            }
            return aData;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTNot aNode, com.hillayes.query.filter.QueryPredicate aData) {
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                aData = aNode.jjtGetChild(i).jjtAccept(this, aData).not();
            }
            return aData;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTLParen aNode, com.hillayes.query.filter.QueryPredicate aData) {
            QueryPredicate result = null;
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                result = aNode.jjtGetChild(i).jjtAccept(this, result);
            }
            return result;
        }

        @Override
        public com.hillayes.query.filter.QueryPredicate visit(ASTComparison aNode, com.hillayes.query.filter.QueryPredicate aData) {
            aNode.childrenAccept(this, aData);
            PredicateExpr expr = (PredicateExpr)aNode.jjtGetValue();

            return new PropertyPredicate(expr.getProperty().getName(), expr.getOperator(), expr.getValue());
        }
    }

    /**
     * A mock implementation of QueryContext to keep a record of the PropertyComparisons generated
     * during parsing.
     */
    private static class MockQueryContext implements QueryContext {
        private final StringBuilder query = new StringBuilder();

        @Override
        public String getClassName() {
            return "mock";
        }

        @Override
        public PredicateExpr newPredicate() {
            return new PredicateExpr(this);
        }

        @Override
        public StringBuilder queryBuilder() {
            return query;
        }

        @Override
        public QueryProperty getPropertyFor(String aName) {
            return new QueryProperty(aName, aName, String.class);
        }

        @Override
        public Iterable<? extends PredicateExpr> getPredicates() {
            return null;
        }
    }
}
