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
    private final JavaSourceWriter writer;

    private Stack<Value> stack = new Stack<Value>();

    public Evaluator(JavaSourceWriter writer) {
        this.writer = writer;
    }

    public void load(String expr, Type type) {
        writer.writeStatement(getTempName() + " = " + expr);
        stack.push(new Value(getTempName(), type));
    }

    public void flush() {
        flush(true);
    }

    public void flush(boolean failOnError) {
        if (stack.isEmpty()) {
            return;
        }
        writer.writeComment("e: stack=" + stack.toString());
        writer.flush();

        if (failOnError) {
            throw new IllegalStateException();
        }
    }

    public void dup() {
        Value v = stack.peek();
        writer.writeStatement(getTempName() + " = " + v.expr);
        stack.push(new Value(getTempName(), v.type));
    }

    public void dupx1() {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        writer.writeStatement(getTempName() + " = " + v1.expr);
        writer.writeStatement(getTempName() + " = " + v2.expr);
        writer.writeStatement(getTempName() + " = " + v1.expr);
        stack.push(new Value(getTempName(), v1.type));
        stack.push(new Value(getTempName(), v2.type));
        stack.push(new Value(getTempName(), v1.type));
    }


    public void pop2() {
        Value value = stack.peek();
        if (value.type == Type.DOUBLE_TYPE || value.type == Type.LONG_TYPE) {
            stack.pop();
        } else {
            stack.pop();
            stack.pop();
        }
    }

    public void dup2() {
        Value value = stack.peek();
        if (value.type == Type.DOUBLE_TYPE || value.type == Type.LONG_TYPE) {
            writer.writeStatement(getTempName() + " = " + value.expr);
            stack.push(value);
        } else {
            Value v1 = stack.pop();
            Value v2 = stack.pop();
            writer.writeStatement(getTempName() + " = " + v2.expr);
            stack.push(v2);
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
            writer.writeStatement(getTempName() + " = " + v2.expr);
            stack.push(v2);
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
        }
    }

    public void dupx2() {
        Value value = stack.peek();
        if (value.type == Type.DOUBLE_TYPE || value.type == Type.LONG_TYPE) {
            Value v1 = stack.pop();
            Value v2 = stack.pop();
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
            writer.writeStatement(getTempName() + " = " + v2.expr);
            stack.push(v2);
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
        } else {
            Value v1 = stack.pop();
            Value v2 = stack.pop();
            Value v3 = stack.pop();

            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
            writer.writeStatement(getTempName() + " = " + v3.expr);
            stack.push(v3);
            writer.writeStatement(getTempName() + " = " + v2.expr);
            stack.push(v2);
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
        }
    }

    public void dup2x1() {
        Value value = stack.peek();
        if (value.type == Type.DOUBLE_TYPE || value.type == Type.LONG_TYPE) {
            Value v1 = stack.pop();
            Value v2 = stack.pop();
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
            writer.writeStatement(getTempName() + " = " + v2.expr);
            stack.push(v2);
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
        } else {
            Value v1 = stack.pop();
            Value v2 = stack.pop();
            Value v3 = stack.pop();

            writer.writeStatement(getTempName() + " = " + v2.expr);
            stack.push(v2);
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
            writer.writeStatement(getTempName() + " = " + v3.expr);
            stack.push(v3);
            writer.writeStatement(getTempName() + " = " + v2.expr);
            stack.push(v2);
            writer.writeStatement(getTempName() + " = " + v1.expr);
            stack.push(v1);
        }
    }


    public void pop() {
        stack.pop();
    }


    public void getField(String owner, String name, Type type) {
        if (stack.empty()) {
            flush();
            writer.writeComment("e: GETFIELD " + owner + " - " + name + " - " + type);
            return;
        }

        Value value = stack.pop();
        writer.writeStatement(getTempName() + " = " + value.expr + "." + name);
        stack.push(new Value(getTempName(), type));
    }

    public void condJump2(String operation, Label label) {
        if (stack.size() < 2) {
            flush();
            writer.writeComment("e: Condjump " + operation + " - " + label);
            return;
        }

        Value value2 = stack.pop();
        Value value1 = stack.pop();
        writer.writeStatement("if (" + value1 + " " + operation + " " + value2 + ") jump " + label);
    }

    public void condJump1(String operation, Label label) {
        if (stack.size() < 1) {
            flush();
            writer.writeComment("e: Condjump " + operation + " - " + label);
            return;
        }

        Value value = stack.pop();
        writer.writeStatement("if (" + value + " " + operation + ") jump " + label);
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

        if (returnType.getSort() != Type.VOID) {
            writer.writeStatement(getTempName() + " = " + method + "(" + argList + ")");
            stack.push(new Value(getTempName(), returnType));
        } else {
            writer.writeStatement(method + "(" + argList + ")");
        }
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

    void stmt(String s, int argsCount) {
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
    }

    public String getTempName() {
        return "__temp_" + stack.size();
    }

    public void expr2(String format, Type type) {
        expr(format, type, 2);
    }

    public void expr1(String format, Type type) {
        expr(format, type, 1);
    }

    public void expr(String format, Type type, int argsCount) {
        if (stack.size() < argsCount) {
            writer.writeComment("e: expr: " + format + " - " + type);
            flush();
            return;
        }

        List<String> args = new ArrayList<String>();
        for (int i = 0; i < argsCount; ++i) {
            args.add(0, stack.pop().expr);
        }

        String e = new MessageFormat(format).format(args.toArray(), new StringBuffer(), new FieldPosition(0)).toString();
        writer.writeStatement(getTempName() + " = " + e);
        stack.push(new Value(getTempName(), type));
    }

    public void stmt1(String format) {
        stmt(format, 1);
    }

    public void stmt0(String format) {
        stmt(format, 0);
    }

    public void stmt2(String format) {
        stmt(format, 2);
    }

    public void expr0(String s, Type type) {
        expr(s, type, 0);
    }

    public void swap() {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        stack.push(v1);
        stack.push(v2);
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
