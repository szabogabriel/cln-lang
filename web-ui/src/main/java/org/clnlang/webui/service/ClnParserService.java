package org.clnlang.webui.service;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.visitor.CompilerVisitor;
import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.declaration.*;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.clnlang.webui.model.ClnSourceEntity;
import org.clnlang.webui.model.TreeNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses CLN source code to extract structural information
 * (package name, functions, structs, unions) used to build the tree view.
 */
@Service
public class ClnParserService {

    /**
     * Build the full tree from all stored source entities.
     */
    public List<TreeNode> buildTree(List<ClnSourceEntity> sources) {
        List<TreeNode> roots = new ArrayList<>();
        for (ClnSourceEntity entity : sources) {
            TreeNode node = parseToTreeNode(entity);
            if (node != null) {
                roots.add(node);
            }
        }
        return roots;
    }

    /**
     * Parse a single CLN source entity and return a package-level tree node
     * with function/struct/union children.
     */
    public TreeNode parseToTreeNode(ClnSourceEntity entity) {
        try {
            String source = entity.getSource();
            if (source == null || source.isBlank()) {
                return new TreeNode(entity.getPackageName(), TreeNode.Type.PACKAGE,
                        entity.getPackageName(), entity.getId());
            }

            CharStream stream = CharStreams.fromString(source);
            clnLexer lexer = new clnLexer(stream);
            lexer.removeErrorListeners();
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            clnParser parser = new clnParser(tokens);
            parser.removeErrorListeners();

            CompilerVisitor visitor = new CompilerVisitor();
            ProgramImpl program = (ProgramImpl) visitor.visit(parser.program());

            String packageName = entity.getPackageName();
            if (program != null && program.getPackageDecl() != null) {
                packageName = program.getPackageDecl().getPackageName();
            }

            TreeNode packageNode = new TreeNode(packageName, TreeNode.Type.PACKAGE,
                    packageName, entity.getId());

            if (program != null) {
                for (CompiledAction decl : program.getDeclarations()) {
                    if (decl instanceof FunctionDeclImpl fn) {
                        packageNode.addChild(new TreeNode(fn.getName(), TreeNode.Type.FUNCTION,
                                packageName, entity.getId()));
                    } else if (decl instanceof StructDeclImpl st) {
                        packageNode.addChild(new TreeNode(st.getName(), TreeNode.Type.STRUCT,
                                packageName, entity.getId()));
                    } else if (decl instanceof UnionDeclImpl un) {
                        packageNode.addChild(new TreeNode(un.getName(), TreeNode.Type.UNION,
                                packageName, entity.getId()));
                    }
                }
            }

            return packageNode;

        } catch (Exception e) {
            // If parsing fails return a plain package node so the UI still shows the entry
            TreeNode fallback = new TreeNode(entity.getPackageName(), TreeNode.Type.PACKAGE,
                    entity.getPackageName(), entity.getId());
            return fallback;
        }
    }
}
