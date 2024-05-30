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
package com.hillayes.query.predicate;

import com.hillayes.query.filter.expression.Expression;
import com.hillayes.query.filter.parser.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:watson.phill@gmail.com">Phill Watson</a>
 * @since 1.0.0
 */
public class PropertyPredicateTest {
    @Test
    public void test() throws Exception {
        Node filter = FilterParser.parse("property3 eq 3 and not property1 le 1 and property2 eq 2 or property4 eq 4");
        QueryPredicate queryPredicate = (QueryPredicate)filter.jjtAccept(new Visitor(), null);
        System.out.println(queryPredicate);
    }

    private static class Visitor implements FilterParserVisitor {
        @Override
        public Object visit(SimpleNode aNode, Object aData) {
            // never used
            throw new RuntimeException("Didn't expect this visit.");
        }

        @Override
        public Object visit(ASTparse aNode, Object aData) {
            Object result = null;
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                result = aNode.jjtGetChild(i).jjtAccept(this, result);
            }
            return result;
        }

        @Override
        public Object visit(ASTOr aNode, Object aData) {
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                QueryPredicate predicate = (QueryPredicate)aNode.jjtGetChild(i).jjtAccept(this, aData);
                aData = (aData == null) ? predicate : ((QueryPredicate)aData).or(predicate);
            }
            return aData;
        }

        @Override
        public Object visit(ASTAnd aNode, Object aData) {
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                QueryPredicate predicate = (QueryPredicate)aNode.jjtGetChild(i).jjtAccept(this, aData);
                aData = (aData == null) ? predicate : ((QueryPredicate)aData).and(predicate);
            }
            return aData;
        }

        @Override
        public Object visit(ASTNot aNode, Object aData) {
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                aData = ((QueryPredicate)aNode.jjtGetChild(i).jjtAccept(this, aData)).not();
            }
            return aData;
        }

        @Override
        public Object visit(ASTLParen aNode, Object aData) {
            QueryPredicate result = null;
            for (int i = 0; i < aNode.jjtGetNumChildren(); i++) {
                result = (QueryPredicate)aNode.jjtGetChild(i).jjtAccept(this, result);
            }
            return result;
        }

        @Override
        public Object visit(ASTComparison aNode, Object aData) {
            aNode.childrenAccept(this, aData);
            Expression expression = (Expression) aNode.jjtGetValue();
            return new ExpressionPredicate(expression);
        }
    }
}
