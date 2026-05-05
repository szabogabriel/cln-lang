package org.clnlang.webui.service;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Loads sample CLN programs into H2 on first startup if the database is empty.
 */
@Component
public class SampleDataInitializer {

    private static final String HELLO_WORLD_SOURCE = """
            package demo.hello;

            import std.console.*;

            int main() {
                writeLine("Hello, World!");
                return 0;
            }
            """;

    private static final String STRUCT_DEMO_SOURCE = """
            package demo.structs;

            import std.console.*;
            import std.str.*;

            struct Point {
                var int x;
                var int y;
            };

            struct Rectangle {
                var int width;
                var int height;
            };

            (var int result = 0) area(Rectangle r) {
                result = r.width * r.height;
                return;
            }

            int main() {
                Point p = Point(x: 10, y: 20);
                writeLine("Point: (" + intToStr(p.x) + ", " + intToStr(p.y) + ")");

                Rectangle rect = Rectangle(width: 5, height: 8);
                int a = area(rect);
                writeLine("Area: " + intToStr(a));

                return 0;
            }
            """;

    private static final String UNION_DEMO_SOURCE = """
            package demo.unions;

            import std.console.*;
            import std.str.*;

            struct Circle {
                var int radius;
            };

            struct Square {
                var int side;
            };

            union Shape {
                Circle;
                Square;
            };

            (var string result = "") describeShape(Shape s) {
                switch (s) {
                    case Circle c: result = "Circle with radius " + intToStr(c.radius);
                    case Square q: result = "Square with side " + intToStr(q.side);
                }
                return;
            }

            int main() {
                Circle c = Circle(radius: 5);
                Square q = Square(side: 7);

                string desc1 = describeShape(c);
                string desc2 = describeShape(q);

                writeLine(desc1);
                writeLine(desc2);

                return 0;
            }
            """;

    private static final String ARRAY_DEMO_SOURCE = """
            package demo.arrays;

            import std.console.*;
            import std.str.*;
            import std.array.*;

            int main() {
                int[] nums = [10, 20, 30, 40, 50];
                var int i = 0;
                while (i < nums.length) {
                    writeLine("nums[" + intToStr(i) + "] = " + intToStr(nums[i]));
                    i++;
                }

                int[] doubled = newIntArray(nums.length);
                var int j = 0;
                while (j < nums.length) {
                    doubled[j] = nums[j] * 2;
                    j++;
                }

                writeLine("Doubled: " + intToStr(doubled[0]) + ", " + intToStr(doubled[1]));

                return 0;
            }
            """;

    private final ClnSourceService clnSourceService;

    public SampleDataInitializer(ClnSourceService clnSourceService) {
        this.clnSourceService = clnSourceService;
    }

    @PostConstruct
    public void init() {
        clnSourceService.save("demo.hello",   HELLO_WORLD_SOURCE);
        clnSourceService.save("demo.structs", STRUCT_DEMO_SOURCE);
        clnSourceService.save("demo.unions",  UNION_DEMO_SOURCE);
        clnSourceService.save("demo.arrays",  ARRAY_DEMO_SOURCE);
    }
}
