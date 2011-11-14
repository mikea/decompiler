package com.mikea.decompiler;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 */
public class Evaluator {
    private final MethodInfo methodInfo;
    private final JavaSourceWriter writer;

    private Stack<Value> stack = new Stack<Value>();

    public Evaluator(MethodInfo methodInfo, JavaSourceWriter writer) {
        this.methodInfo = methodInfo;
        this.writer = writer;
    }

    public void push(String expr, Type type) {
        stack.push(new Value(expr, type));
    }

    public void flush() {
        if (stack.isEmpty()) {
            return;
        }
        writer.writeComment("e: stack=" + stack.toString());
        stack.clear();
    }

    public void dup() {
        if (stack.empty()) {
            flush();
            writer.writeComment("e: DUP");
            return;
        } else {
            Value v = stack.peek();
            int sort = v.type.getSort();
            String expr = v.expr;
            if (sort == Type.ARRAY || sort == Type.OBJECT) {
                expr = "&" + expr;
            }
            stack.push(new Value(expr, v.type));
        }
    }

    public void pop2() {
        if (stack.empty()) {
            writer.writeComment("e: POP2");
            return;
        }

        Value value = stack.peek();
        if (value.type == Type.DOUBLE_TYPE || value.type == Type.LONG_TYPE) {
            stack.pop();
        } else {
            stack.pop();
            stack.pop();
        }
    }

    public void getField(String owner, String name, Type type) {
        if (stack.empty()) {
            flush();
            writer.writeComment("e: GETFIELD " + owner + " - " + name + " - " + type);
            return;
        }

        Value value = stack.pop();
        stack.push(new Value(value.expr + "." + name, type));
    }

    public void condJump(String operation, Label label) {
        if (stack.size() < 2) {
            flush();
            writer.writeComment("e: Condjump " + operation + " - " + label);
            return;
        }

        Value value2 = stack.pop();
        Value value1 = stack.pop();
        writer.writeStatement("if (" + value1 + " " + operation + " " + value2 + ") jump " + label);
    }

    public void staticCall(String method, int argsCount, Type returnType) {
        if (stack.size() < argsCount) {
            writer.writeComment("e: staticCall " + method + " - " + argsCount + " - " + returnType);
            return;
        }

        List<Value> args = new ArrayList<Value>();
        for (int i = 0; i < argsCount; ++i) {
            args.add(0, stack.pop());
        }
        String argList = "";
        for (int i = 0; i < argsCount; ++i) {
            if (i > 0) {
                argList += ", ";
            }
            argList += args.get(i).expr;
        }

        stack.push(new Value(method + "(" + argList + ")", returnType));
    }

    public void expr1(String format, Type type) {
        if (stack.size() < 1) {
            writer.writeComment("e: expr1: " + format + " - " + type);
            flush();
            return;
        }

        Value val = stack.pop();
        stack.push(new Value(MessageFormat.format(format, val.expr), type));
    }

    public void virtualCall(String method, int argsCount, Type returnType) {
        if (stack.size() < 1) {
            writer.writeComment("e: virtualCall: " + method + " - " + argsCount + " - " + returnType);
            flush();
            return;
        }

        Value ref = stack.pop();
        staticCall(ref.expr + "." + method, argsCount, returnType);
    }

    public void stmt3(String s) {
        stmt(s, 3);
    }

    private void stmt(String s, int argsCount) {
        if (stack.size() < argsCount) {
            writer.writeComment("e: stmt: " + s);
            flush();
            return;
        }

        List<String> args = new ArrayList<String>();
        for (int i = 0; i < argsCount; ++i) {
            args.add(0, stack.pop().expr);
        }

        String stmt = new MessageFormat(s).format(args.toArray(), new StringBuffer(), new FieldPosition(0)).toString();
        writer.writeStatement(stmt);
        writer.writeComment("stack after: " + stack.toString());
    }

    private static class Value {
        private final String expr;
        private final Type type;

        public Value(String expr, Type type) {
            this.expr = expr;
            this.type = type;
        }

        @Override
        public String toString() {
            return expr;
        }
    }
}
