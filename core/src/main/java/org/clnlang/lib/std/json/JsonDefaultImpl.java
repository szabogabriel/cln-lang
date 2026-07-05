package org.clnlang.lib.std.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.lib.ClnFunction;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;

public class JsonDefaultImpl implements ClnFunction {

    private final String packageName = "std.json";

    // ========== toJson ==========

    private void executeToJson(ExecutionContext context) {
        Object data = context.getLocalContext().getValue("data");
        String result = toJson(data);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private String toJson(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String s) {
            return toJsonString(s);
        } else if (value instanceof Long l) {
            return Long.toString(l);
        } else if (value instanceof Boolean b) {
            return Boolean.toString(b);
        } else if (value instanceof BigDecimal bd) {
            return bd.toPlainString();
        } else if (value instanceof List<?> list) {
            return toJsonArray(list);
        } else if (value instanceof Map<?, ?> map) {
            return toJsonObject(map);
        } else {
            return "null";
        }
    }

    private String toJsonString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                default   -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    private String toJsonArray(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private String toJsonObject(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            if ("__type__".equals(key)) continue;
            if (!first) sb.append(",");
            first = false;
            sb.append(toJsonString(key));
            sb.append(":");
            sb.append(toJson(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    // ========== parseJson ==========

    private void executeParseJson(ExecutionContext context) {
        String json = (String) context.getLocalContext().getValue("json");
        Object target = context.getLocalContext().getValue("target");

        if (!(target instanceof Map)) {
            throw new RuntimeException("parseJson: target must be a struct instance");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> targetMap = (Map<String, Object>) target;

        int[] pos = {0};
        skipWhitespace(json, pos);
        Object parsed = parseValue(json, pos);

        if (!(parsed instanceof Map)) {
            throw new RuntimeException("parseJson: JSON root must be an object to populate a struct target");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> parsedMap = (Map<String, Object>) parsed;

        for (Map.Entry<String, Object> entry : parsedMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null && targetMap.containsKey(key)) {
                throw new RuntimeException(
                    "parseJson: null value received for field '" + key + "' but target already has an element for that field");
            }
            if (!"__type__".equals(key)) {
                targetMap.put(key, value);
            }
        }
    }

    // ========== JSON Parser ==========

    private Object parseValue(String json, int[] pos) {
        skipWhitespace(json, pos);
        if (pos[0] >= json.length()) {
            throw new RuntimeException("parseJson: unexpected end of input");
        }
        char c = json.charAt(pos[0]);
        return switch (c) {
            case '"' -> parseString(json, pos);
            case '{' -> parseObject(json, pos);
            case '[' -> parseArray(json, pos);
            case 't' -> parseLiteral(json, pos, "true", Boolean.TRUE);
            case 'f' -> parseLiteral(json, pos, "false", Boolean.FALSE);
            case 'n' -> parseLiteral(json, pos, "null", null);
            default  -> {
                if (c == '-' || Character.isDigit(c)) {
                    yield parseNumber(json, pos);
                }
                throw new RuntimeException("parseJson: unexpected character '" + c + "' at position " + pos[0]);
            }
        };
    }

    private void skipWhitespace(String json, int[] pos) {
        while (pos[0] < json.length() && Character.isWhitespace(json.charAt(pos[0]))) {
            pos[0]++;
        }
    }

    private String parseString(String json, int[] pos) {
        pos[0]++; // skip opening '"'
        StringBuilder sb = new StringBuilder();
        while (pos[0] < json.length()) {
            char c = json.charAt(pos[0]);
            if (c == '"') {
                pos[0]++; // skip closing '"'
                return sb.toString();
            }
            if (c == '\\') {
                pos[0]++;
                if (pos[0] >= json.length()) break;
                char esc = json.charAt(pos[0]);
                switch (esc) {
                    case '"'  -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/'  -> sb.append('/');
                    case 'n'  -> sb.append('\n');
                    case 'r'  -> sb.append('\r');
                    case 't'  -> sb.append('\t');
                    case 'b'  -> sb.append('\b');
                    case 'f'  -> sb.append('\f');
                    case 'u'  -> {
                        if (pos[0] + 4 >= json.length()) {
                            throw new RuntimeException("parseJson: incomplete unicode escape");
                        }
                        String hex = json.substring(pos[0] + 1, pos[0] + 5);
                        sb.append((char) Integer.parseInt(hex, 16));
                        pos[0] += 4;
                    }
                    default -> sb.append(esc);
                }
            } else {
                sb.append(c);
            }
            pos[0]++;
        }
        throw new RuntimeException("parseJson: unterminated string");
    }

    private Map<String, Object> parseObject(String json, int[] pos) {
        pos[0]++; // skip '{'
        Map<String, Object> map = new LinkedHashMap<>();
        skipWhitespace(json, pos);
        if (pos[0] < json.length() && json.charAt(pos[0]) == '}') {
            pos[0]++;
            return map;
        }
        while (pos[0] < json.length()) {
            skipWhitespace(json, pos);
            if (json.charAt(pos[0]) != '"') {
                throw new RuntimeException("parseJson: expected string key in object at position " + pos[0]);
            }
            String key = parseString(json, pos);
            skipWhitespace(json, pos);
            if (pos[0] >= json.length() || json.charAt(pos[0]) != ':') {
                throw new RuntimeException("parseJson: expected ':' after key '" + key + "'");
            }
            pos[0]++; // skip ':'
            Object value = parseValue(json, pos);
            map.put(key, value);
            skipWhitespace(json, pos);
            if (pos[0] >= json.length()) break;
            char next = json.charAt(pos[0]);
            if (next == '}') {
                pos[0]++;
                return map;
            }
            if (next != ',') {
                throw new RuntimeException("parseJson: expected ',' or '}' in object at position " + pos[0]);
            }
            pos[0]++; // skip ','
        }
        throw new RuntimeException("parseJson: unterminated object");
    }

    private List<Object> parseArray(String json, int[] pos) {
        pos[0]++; // skip '['
        List<Object> list = new ArrayList<>();
        skipWhitespace(json, pos);
        if (pos[0] < json.length() && json.charAt(pos[0]) == ']') {
            pos[0]++;
            return list;
        }
        while (pos[0] < json.length()) {
            Object value = parseValue(json, pos);
            list.add(value);
            skipWhitespace(json, pos);
            if (pos[0] >= json.length()) break;
            char next = json.charAt(pos[0]);
            if (next == ']') {
                pos[0]++;
                return list;
            }
            if (next != ',') {
                throw new RuntimeException("parseJson: expected ',' or ']' in array at position " + pos[0]);
            }
            pos[0]++; // skip ','
        }
        throw new RuntimeException("parseJson: unterminated array");
    }

    private Object parseLiteral(String json, int[] pos, String literal, Object value) {
        if (json.startsWith(literal, pos[0])) {
            pos[0] += literal.length();
            return value;
        }
        throw new RuntimeException("parseJson: invalid literal at position " + pos[0]);
    }

    private Object parseNumber(String json, int[] pos) {
        int start = pos[0];
        if (json.charAt(pos[0]) == '-') pos[0]++;
        while (pos[0] < json.length() && Character.isDigit(json.charAt(pos[0]))) pos[0]++;
        boolean isDecimal = false;
        if (pos[0] < json.length() && json.charAt(pos[0]) == '.') {
            isDecimal = true;
            pos[0]++;
            while (pos[0] < json.length() && Character.isDigit(json.charAt(pos[0]))) pos[0]++;
        }
        if (pos[0] < json.length() && (json.charAt(pos[0]) == 'e' || json.charAt(pos[0]) == 'E')) {
            isDecimal = true;
            pos[0]++;
            if (pos[0] < json.length() && (json.charAt(pos[0]) == '+' || json.charAt(pos[0]) == '-')) pos[0]++;
            while (pos[0] < json.length() && Character.isDigit(json.charAt(pos[0]))) pos[0]++;
        }
        String numStr = json.substring(start, pos[0]);
        if (isDecimal) {
            return new BigDecimal(numStr);
        } else {
            return Long.valueOf(numStr);
        }
    }

    // ========== Registration ==========

    @Override
    public void register(Registry registry) {
        // toJson(Any data) -> string result
        FunctionDeclImpl toJsonFunc = new FunctionDeclImpl("toJson", true);
        toJsonFunc.addParameter("Any", "data");
        toJsonFunc.addReturnVar("string", "result");
        toJsonFunc.setBlock(this::executeToJson);
        registry.registerFunction(new FullyQualifiedName(packageName, "toJson"), toJsonFunc);

        // parseJson(string json, Any target) -> void
        FunctionDeclImpl parseJsonFunc = new FunctionDeclImpl("parseJson", true);
        parseJsonFunc.addParameter("string", "json");
        parseJsonFunc.addParameter("Any", "target");
        parseJsonFunc.setBlock(this::executeParseJson);
        registry.registerFunction(new FullyQualifiedName(packageName, "parseJson"), parseJsonFunc);
    }
}

