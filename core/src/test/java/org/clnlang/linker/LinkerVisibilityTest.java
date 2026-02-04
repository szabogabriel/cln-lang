package org.clnlang.linker;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.GlobalVarDeclImpl;
import org.clnlang.compile.declaration.ImportDeclImpl;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;
import org.clnlang.runtime.types.StructDefinition;
import org.clnlang.runtime.types.UnionDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test visibility enforcement in the Linker.
 * Tests that exposed symbols can be imported cross-package,
 * while non-exposed symbols are only accessible within the same package.
 * 
 * Visibility Rules:
 * 1. Same Package: Full access to all symbols (both exposed and non-exposed)
 * 2. Different Package: Only access to symbols marked with 'expose' keyword
 * 
 * Test Coverage:
 * - Functions (exposed and non-exposed)
 * - Global Variables (exposed and non-exposed)
 * - Global Constants (exposed and non-exposed)
 * - Struct Types (exposed and non-exposed)
 * - Union Types (exposed and non-exposed)
 * - Wildcard imports (filters by visibility)
 * - Specific imports (enforces visibility)
 * 
 * All 15 tests verify that:
 * - Same-package imports succeed regardless of expose flag
 * - Cross-package imports succeed only for exposed symbols
 * - Cross-package imports of non-exposed symbols throw exceptions
 * - Wildcard imports only bring in accessible symbols
 * - Wildcard imports fail if no accessible symbols exist
 */
public class LinkerVisibilityTest {

    private Registry registry;
    private Linker linker;

    @BeforeEach
    public void setUp() {
        registry = new Registry();
        linker = new Linker();
    }

    /**
     * Helper method to create a mock function with package and expose flag
     */
    private FunctionDeclImpl createMockFunction(String name, String packageName, boolean isExposed) {
        FunctionDeclImpl func = new FunctionDeclImpl(name, isExposed);
        func.setPackageName(packageName);
        return func;
    }

    /**
     * Helper method to create a mock global variable with package and expose flag
     */
    private GlobalVarDeclImpl createMockVariable(String name, String type, String packageName, boolean isExposed, boolean isMutable) {
        GlobalVarDeclImpl var = new GlobalVarDeclImpl(isMutable, type, name, null, isExposed);
        var.setPackageName(packageName);
        return var;
    }

    /**
     * Helper method to create a mock struct with package and expose flag
     */
    private StructDefinition createMockStruct(String name, String packageName, boolean isExposed) {
        StructDefinition struct = new StructDefinition(name, packageName, isExposed);
        struct.addField("value", "int", false);
        return struct;
    }

    /**
     * Helper method to create a mock union with package and expose flag
     */
    private UnionDefinition createMockUnion(String name, String packageName, boolean isExposed) {
        UnionDefinition union = new UnionDefinition(name, packageName, isExposed);
        union.addMember("int");
        union.addMember("string");
        return union;
    }

    @Test
    public void testSamePackageAccessToNonExposedFunction() throws Exception {
        // Setup: Register a non-exposed function in package "test.lib"
        FunctionDeclImpl privateFunc = createMockFunction("privateFunc", "test.lib", false);
        registry.registerFunction(new FullyQualifiedName("test.lib", "privateFunc"), privateFunc);

        // Setup: Create execution context in the SAME package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.lib");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.privateFunc", false);
        context.registerImport(importDecl);

        // Act: Resolve imports (should succeed - same package)
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Function should be registered
        assertTrue(context.getGlobalContext().hasFunction("privateFunc"));
    }

    @Test
    public void testCrossPackageAccessToNonExposedFunctionFails() {
        // Setup: Register a non-exposed function in package "test.lib"
        FunctionDeclImpl privateFunc = createMockFunction("privateFunc", "test.lib", false);
        registry.registerFunction(new FullyQualifiedName("test.lib", "privateFunc"), privateFunc);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.privateFunc", false);
        context.registerImport(importDecl);

        // Act & Assert: Should throw exception - different package, not exposed
        Exception exception = assertThrows(Exception.class, () -> linker.resolveImports(context, registry));
        assertTrue(exception.getMessage().contains("not exposed"));
    }

    @Test
    public void testCrossPackageAccessToExposedFunction() throws Exception {
        // Setup: Register an EXPOSED function in package "test.lib"
        FunctionDeclImpl publicFunc = createMockFunction("publicFunc", "test.lib", true);
        registry.registerFunction(new FullyQualifiedName("test.lib", "publicFunc"), publicFunc);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.publicFunc", false);
        context.registerImport(importDecl);

        // Act: Resolve imports (should succeed - exposed)
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Function should be registered
        assertTrue(context.getGlobalContext().hasFunction("publicFunc"));
    }

    @Test
    public void testWildcardImportSamePackageGetsAll() throws Exception {
        // Setup: Register both exposed and non-exposed functions in package "test.lib"
        FunctionDeclImpl publicFunc = createMockFunction("publicFunc", "test.lib", true);
        FunctionDeclImpl privateFunc = createMockFunction("privateFunc", "test.lib", false);
        registry.registerFunction(new FullyQualifiedName("test.lib", "publicFunc"), publicFunc);
        registry.registerFunction(new FullyQualifiedName("test.lib", "privateFunc"), privateFunc);

        // Setup: Create execution context in the SAME package with wildcard import
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.lib");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib", true); // wildcard
        context.registerImport(importDecl);

        // Act: Resolve imports
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Both functions should be registered
        assertTrue(context.getGlobalContext().hasFunction("publicFunc"));
        assertTrue(context.getGlobalContext().hasFunction("privateFunc"));
    }

    @Test
    public void testWildcardImportDifferentPackageGetsOnlyExposed() throws Exception {
        // Setup: Register both exposed and non-exposed functions in package "test.lib"
        FunctionDeclImpl publicFunc = createMockFunction("publicFunc", "test.lib", true);
        FunctionDeclImpl privateFunc = createMockFunction("privateFunc", "test.lib", false);
        registry.registerFunction(new FullyQualifiedName("test.lib", "publicFunc"), publicFunc);
        registry.registerFunction(new FullyQualifiedName("test.lib", "privateFunc"), privateFunc);

        // Setup: Create execution context in a DIFFERENT package with wildcard import
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib", true); // wildcard
        context.registerImport(importDecl);

        // Act: Resolve imports
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Only exposed function should be registered
        assertTrue(context.getGlobalContext().hasFunction("publicFunc"));
        assertFalse(context.getGlobalContext().hasFunction("privateFunc"));
    }

    @Test
    public void testCrossPackageAccessToNonExposedVariable() {
        // Setup: Register a non-exposed variable in package "test.lib"
        GlobalVarDeclImpl privateVar = createMockVariable("privateVar", "int", "test.lib", false, true);
        registry.registerGlobalVariable(new FullyQualifiedName("test.lib", "privateVar"), privateVar);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.privateVar", false);
        context.registerImport(importDecl);

        // Act & Assert: Should throw exception - different package, not exposed
        Exception exception = assertThrows(Exception.class, () -> linker.resolveImports(context, registry));
        assertTrue(exception.getMessage().contains("not exposed"));
    }

    @Test
    public void testCrossPackageAccessToExposedVariable() throws Exception {
        // Setup: Register an EXPOSED variable in package "test.lib"
        GlobalVarDeclImpl publicVar = createMockVariable("publicVar", "int", "test.lib", true, true);
        registry.registerGlobalVariable(new FullyQualifiedName("test.lib", "publicVar"), publicVar);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.publicVar", false);
        context.registerImport(importDecl);

        // Act: Resolve imports (should succeed - exposed)
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Variable should be registered
        assertTrue(context.getGlobalContext().hasGlobalVariable("publicVar"));
    }

    @Test
    public void testCrossPackageAccessToNonExposedConstant() {
        // Setup: Register a non-exposed constant in package "test.lib"
        GlobalVarDeclImpl privateConst = createMockVariable("PRIVATE_CONST", "int", "test.lib", false, true);
        registry.registerGlobalConstant(new FullyQualifiedName("test.lib", "PRIVATE_CONST"), privateConst);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.PRIVATE_CONST", false);
        context.registerImport(importDecl);

        // Act & Assert: Should throw exception - different package, not exposed
        Exception exception = assertThrows(Exception.class, () -> linker.resolveImports(context, registry));
        assertTrue(exception.getMessage().contains("not exposed"));
    }

    @Test
    public void testCrossPackageAccessToExposedConstant() throws Exception {
        // Setup: Register an EXPOSED constant in package "test.lib"
        GlobalVarDeclImpl publicConst = createMockVariable("PUBLIC_CONST", "int", "test.lib", true, true);
        registry.registerGlobalConstant(new FullyQualifiedName("test.lib", "PUBLIC_CONST"), publicConst);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.PUBLIC_CONST", false);
        context.registerImport(importDecl);

        // Act: Resolve imports (should succeed - exposed)
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Constant should be registered
        assertTrue(context.getGlobalContext().hasGlobalVariable("PUBLIC_CONST"));
    }

    @Test
    public void testCrossPackageAccessToNonExposedStruct() {
        // Setup: Register a non-exposed struct in package "test.lib"
        StructDefinition privateStruct = createMockStruct("PrivateStruct", "test.lib", false);
        registry.registerStructType(new FullyQualifiedName("test.lib", "PrivateStruct"), privateStruct);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.PrivateStruct", false);
        context.registerImport(importDecl);

        // Act & Assert: Should throw exception - different package, not exposed
        Exception exception = assertThrows(Exception.class, () -> linker.resolveImports(context, registry));
        assertTrue(exception.getMessage().contains("not exposed"));
    }

    @Test
    public void testCrossPackageAccessToExposedStruct() throws Exception {
        // Setup: Register an EXPOSED struct in package "test.lib"
        StructDefinition publicStruct = createMockStruct("PublicStruct", "test.lib", true);
        registry.registerStructType(new FullyQualifiedName("test.lib", "PublicStruct"), publicStruct);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.PublicStruct", false);
        context.registerImport(importDecl);

        // Act: Resolve imports (should succeed - exposed)
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Struct should be registered
        assertTrue(context.getGlobalContext().hasStructType("PublicStruct"));
    }

    @Test
    public void testCrossPackageAccessToNonExposedUnion() {
        // Setup: Register a non-exposed union in package "test.lib"
        UnionDefinition privateUnion = createMockUnion("PrivateUnion", "test.lib", false);
        registry.registerUnionType(new FullyQualifiedName("test.lib", "PrivateUnion"), privateUnion);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.PrivateUnion", false);
        context.registerImport(importDecl);

        // Act & Assert: Should throw exception - different package, not exposed
        Exception exception = assertThrows(Exception.class, () -> linker.resolveImports(context, registry));
        assertTrue(exception.getMessage().contains("not exposed"));
    }

    @Test
    public void testCrossPackageAccessToExposedUnion() throws Exception {
        // Setup: Register an EXPOSED union in package "test.lib"
        UnionDefinition publicUnion = createMockUnion("PublicUnion", "test.lib", true);
        registry.registerUnionType(new FullyQualifiedName("test.lib", "PublicUnion"), publicUnion);

        // Setup: Create execution context in a DIFFERENT package
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib.PublicUnion", false);
        context.registerImport(importDecl);

        // Act: Resolve imports (should succeed - exposed)
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Union should be registered
        assertTrue(context.getGlobalContext().hasUnionType("PublicUnion"));
    }

    @Test
    public void testWildcardImportWithMixedSymbolTypes() throws Exception {
        // Setup: Register various symbol types (some exposed, some not) in package "test.lib"
        FunctionDeclImpl publicFunc = createMockFunction("publicFunc", "test.lib", true);
        FunctionDeclImpl privateFunc = createMockFunction("privateFunc", "test.lib", false);
        GlobalVarDeclImpl publicVar = createMockVariable("publicVar", "int", "test.lib", true, true);
        GlobalVarDeclImpl privateVar = createMockVariable("privateVar", "int", "test.lib", false, true);
        StructDefinition publicStruct = createMockStruct("PublicStruct", "test.lib", true);
        StructDefinition privateStruct = createMockStruct("PrivateStruct", "test.lib", false);

        registry.registerFunction(new FullyQualifiedName("test.lib", "publicFunc"), publicFunc);
        registry.registerFunction(new FullyQualifiedName("test.lib", "privateFunc"), privateFunc);
        registry.registerGlobalVariable(new FullyQualifiedName("test.lib", "publicVar"), publicVar);
        registry.registerGlobalVariable(new FullyQualifiedName("test.lib", "privateVar"), privateVar);
        registry.registerStructType(new FullyQualifiedName("test.lib", "PublicStruct"), publicStruct);
        registry.registerStructType(new FullyQualifiedName("test.lib", "PrivateStruct"), privateStruct);

        // Setup: Create execution context in a DIFFERENT package with wildcard import
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib", true); // wildcard
        context.registerImport(importDecl);

        // Act: Resolve imports
        assertDoesNotThrow(() -> linker.resolveImports(context, registry));

        // Assert: Only exposed symbols should be registered
        assertTrue(context.getGlobalContext().hasFunction("publicFunc"));
        assertFalse(context.getGlobalContext().hasFunction("privateFunc"));
        assertTrue(context.getGlobalContext().hasGlobalVariable("publicVar"));
        assertFalse(context.getGlobalContext().hasGlobalVariable("privateVar"));
        assertTrue(context.getGlobalContext().hasStructType("PublicStruct"));
        assertFalse(context.getGlobalContext().hasStructType("PrivateStruct"));
    }

    @Test
    public void testWildcardImportFailsWhenNoAccessibleSymbols() {
        // Setup: Register only non-exposed symbols in package "test.lib"
        FunctionDeclImpl privateFunc = createMockFunction("privateFunc", "test.lib", false);
        GlobalVarDeclImpl privateVar = createMockVariable("privateVar", "int", "test.lib", false, true);
        
        registry.registerFunction(new FullyQualifiedName("test.lib", "privateFunc"), privateFunc);
        registry.registerGlobalVariable(new FullyQualifiedName("test.lib", "privateVar"), privateVar);

        // Setup: Create execution context in a DIFFERENT package with wildcard import
        ExecutionContext context = new ExecutionContext();
        context.getGlobalContext().setPackageName("test.client");
        ImportDeclImpl importDecl = new ImportDeclImpl("test.lib", true); // wildcard
        context.registerImport(importDecl);

        // Act & Assert: Should throw exception - no accessible symbols
        Exception exception = assertThrows(Exception.class, () -> linker.resolveImports(context, registry));
        assertTrue(exception.getMessage().contains("No accessible symbols found"));
    }
}
