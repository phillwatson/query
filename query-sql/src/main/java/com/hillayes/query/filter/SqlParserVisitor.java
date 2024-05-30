package com.hillayes.query.filter;

import com.hillayes.query.filter.expression.*;
import com.hillayes.query.filter.parser.*;

import java.util.stream.Collectors;

public class SqlParserVisitor implements FilterParserVisitor, Argument.Visitor<String> {
    private StringBuilder query;

    public String getQuery() {
        return query.toString();
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTparse node, Object data) {
        query = new StringBuilder();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            data = node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTOr node, Object data) {
        int childCount = node.jjtGetNumChildren();
        for (int i = 0; i < childCount; i++) {
            data = node.jjtGetChild(i).jjtAccept(this, data);
            if (i < childCount - 1)
                query.append(" OR ");
        }
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        int childCount = node.jjtGetNumChildren();
        for (int i = 0; i < childCount; i++) {
            data = node.jjtGetChild(i).jjtAccept(this, data);
            if (i < childCount - 1)
                query.append(" AND ");
        }
        return null;
    }

    @Override
    public Object visit(ASTNot node, Object data) {
        query.append("NOT ");
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            data = node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    @Override
    public Object visit(ASTLParen node, Object data) {
        query.append('(');
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            data = node.jjtGetChild(i).jjtAccept(this, data);
        }
        query.append(')');
        return null;
    }

    @Override
    public Object visit(ASTComparison node, Object data) {
        Expression expr = (Expression) node.jjtGetValue();
        query.append(expr.toString(this));
        return null;
    }

    public String visit(NumericArgument aNumericArgument) {
        return aNumericArgument.getValue().toString();
    }

    public String visit(StringArgument aStringArgument) {
        return "'" + aStringArgument.getValue() + "'";
    }

    public String visit(PropertyArgument aPropertyArgument) {
        return aPropertyArgument.getPropertyPath().toString();
    }

    public String visit(FunctionArgument aFunctionArgument) {
        String args = aFunctionArgument.getArguments().stream()
            .map(a -> a.accept(this))
            .collect(Collectors.joining(", "));
        return aFunctionArgument.getFunction() + "(" + args + ")";
    }
}
