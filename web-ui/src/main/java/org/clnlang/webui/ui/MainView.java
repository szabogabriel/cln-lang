package org.clnlang.webui.ui;

import java.util.List;

import org.clnlang.webui.model.ClnSourceEntity;
import org.clnlang.webui.model.TreeNode;
import org.clnlang.webui.service.ClnExecutionService;
import org.clnlang.webui.service.ClnParserService;
import org.clnlang.webui.service.ClnSourceService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * The single-page main view of the cln-lang DB demo web application.
 *
 * <pre>
 * ┌──────────────────────────────────────────────────────────────┐
 * │  Toolbar: [New] [Save] [Delete] [Execute]         [Help ❓]  │
 * ├──────────────┬───────────────────────────┬───────────────────┤
 * │  Package     │  Source code editor       │  Help panel       │
 * │  tree view   │  (centre pane)            │  (toggle right)   │
 * │              │                           │                   │
 * ├──────────────┴───────────────────────────┴───────────────────┤
 * │  Terminal output (read-only, bottom strip)                   │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 */
@Route("")
@PageTitle("cln-lang DB Demo")
public class MainView extends AppLayout {

    // ── Services ──────────────────────────────────────────────────────────────
    private final ClnSourceService    sourceService;
    private final ClnParserService    parserService;
    private final ClnExecutionService executionService;

    // ── UI components ──────────────────────────────────────────────────────
    private final TreeGrid<TreeNode> treeGrid    = new TreeGrid<>();
    private final TextArea           editor      = new TextArea();
    private final TextArea           terminal    = new TextArea();
    private final Div                helpPanel   = new Div();
    private final Div                lineNumbers = new Div();
    private boolean                  helpVisible = false;

    // ── State ──────────────────────────────────────────────────────────────
    private TreeNode selectedNode;  // currently selected tree item

    // ── Constructor ───────────────────────────────────────────────────────
    public MainView(ClnSourceService sourceService,
                    ClnParserService parserService,
                    ClnExecutionService executionService) {
        this.sourceService    = sourceService;
        this.parserService    = parserService;
        this.executionService = executionService;

        buildNavbar();
        buildContent();
        refreshTree();
    }

    // ── Navbar / toolbar ──────────────────────────────────────────────────

    private void buildNavbar() {
        H1 title = new H1("cln-lang DB Demo");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0")
                .set("color", "var(--lumo-base-color)");

        // Toolbar buttons
        Button btnNew     = new Button("New",    new Icon(VaadinIcon.PLUS));
        Button btnSave    = new Button("Save",   new Icon(VaadinIcon.CHECK));
        Button btnDelete  = new Button("Delete", new Icon(VaadinIcon.TRASH));
        Button btnExecute = new Button("Execute ▶", new Icon(VaadinIcon.PLAY));
        Button btnHelp    = new Button("Help",   new Icon(VaadinIcon.QUESTION_CIRCLE));

        btnNew.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        btnExecute.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnHelp.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);

        btnNew.addClickListener(e -> onNew());
        btnSave.addClickListener(e -> onSave());
        btnDelete.addClickListener(e -> onDelete());
        btnExecute.addClickListener(e -> onExecute());
        btnHelp.addClickListener(e -> toggleHelp());

        HorizontalLayout toolbar = new HorizontalLayout(
                title, btnNew, btnSave, btnDelete, btnExecute, btnHelp);
        toolbar.setWidthFull();
        toolbar.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        toolbar.getStyle()
                .set("padding", "0 var(--lumo-space-m)")
                .set("gap", "var(--lumo-space-s)");
        toolbar.setFlexGrow(1, title);

        addToNavbar(toolbar);
    }

    // ── Main content area ─────────────────────────────────────────────────

    private void buildContent() {
        // ── Tree (left) ────────────────────────────────────────────────────
        treeGrid.addHierarchyColumn(TreeNode::getLabel).setHeader("Packages & Symbols");
        treeGrid.setWidthFull();
        treeGrid.setHeightFull();
        treeGrid.addSelectionListener(e -> e.getFirstSelectedItem().ifPresent(this::onNodeSelected));

        Div treeWrapper = new Div(treeGrid);
        treeWrapper.getStyle()
                .set("overflow", "auto")
                .set("height", "100%");

        // ── Editor (centre) ────────────────────────────────────────────────
        editor.setPlaceholder("Select a package from the tree, or create a new one…");
        editor.setWidthFull();
        editor.setHeightFull();
        editor.getStyle()
                .set("font-family", "monospace")
                .set("font-size", "var(--lumo-font-size-s)");
        editor.getElement().setAttribute("spellcheck", "false");

        // ── Line numbers gutter ────────────────────────────────────────────
        lineNumbers.getStyle()
                .set("font-family", "monospace")
                .set("font-size", "var(--lumo-font-size-s)")
                .set("text-align", "right")
                .set("padding", "0 0.4em")
                .set("min-width", "2.8em")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("border-right", "1px solid var(--lumo-contrast-10pct)")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("overflow", "hidden")
                .set("user-select", "none")
                .set("cursor", "default");
        editor.addAttachListener(event ->
            event.getUI().getPage().executeJs("""
                (function(va, gutter) {
                    function updateGutter(text) {
                        var count = (text || '').split('\\n').length;
                        var html = '';
                        for (var i = 1; i <= count; i++) {
                            html += '<div>' + i + '</div>';
                        }
                        gutter.innerHTML = html;
                    }
                    function setup() {
                        var ta = va.shadowRoot && va.shadowRoot.querySelector('textarea');
                        if (!ta) { requestAnimationFrame(setup); return; }
                        var cs = window.getComputedStyle(ta);
                        gutter.style.lineHeight = cs.lineHeight;
                        gutter.style.paddingTop = cs.paddingTop;
                        gutter.style.fontSize = cs.fontSize;
                        ta.addEventListener('input', function() { updateGutter(ta.value); });
                        ta.addEventListener('scroll', function() { gutter.scrollTop = ta.scrollTop; });
                        va.addEventListener('value-changed', function() {
                            updateGutter(va.value || '');
                            requestAnimationFrame(function() { gutter.scrollTop = ta.scrollTop; });
                        });
                        updateGutter(ta.value || '');
                    }
                    setup();
                })($0, $1);
                """, editor.getElement(), lineNumbers.getElement())
        );

        // ── Help panel (right, hidden initially) ───────────────────────────
        buildHelpPanel();
        helpPanel.setVisible(false);

        // ── Top-area split: tree | editor | help ──────────────────────────
        SplitLayout topSplit = new SplitLayout(treeWrapper, buildCentreAndHelp());
        topSplit.setSplitterPosition(20);   // tree takes 20 %
        topSplit.setWidthFull();
        topSplit.getStyle().set("flex", "1");

        // ── Terminal (bottom) ──────────────────────────────────────────────
        terminal.setReadOnly(true);
        terminal.setValue("─── Terminal ───\n");
        terminal.setWidthFull();
        terminal.setHeight("180px");
        terminal.getStyle()
                .set("font-family", "monospace")
                .set("font-size", "var(--lumo-font-size-s)")
                .set("--vaadin-input-field-value-color", "#d4d4d4")
                .set("--vaadin-input-field-background", "#1e1e1e")
                .set("background", "#1e1e1e")
                .set("color", "#d4d4d4");
        terminal.getElement().setAttribute("spellcheck", "false");
        // Force text colour on the inner textarea inside the shadow DOM
        terminal.getElement().executeJs(
            "var ta = this.inputElement || (this.shadowRoot && this.shadowRoot.querySelector('textarea'));" +
            "if (ta) { ta.style.color = '#d4d4d4'; ta.style.caretColor = '#d4d4d4'; }");

        Button btnClearConsole = new Button("Clear", new Icon(VaadinIcon.CLOSE_SMALL));
        btnClearConsole.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        btnClearConsole.getStyle().set("margin-left", "auto");
        btnClearConsole.addClickListener(e -> terminal.setValue("─── Terminal ───\n"));

        HorizontalLayout terminalBar = new HorizontalLayout(btnClearConsole);
        terminalBar.setWidthFull();
        terminalBar.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        terminalBar.getStyle()
                .set("background", "#1e1e1e")
                .set("padding", "2px var(--lumo-space-s)")
                .set("border-top", "1px solid #444");

        VerticalLayout terminalWrapper = new VerticalLayout(terminalBar, terminal);
        terminalWrapper.setPadding(false);
        terminalWrapper.setSpacing(false);
        terminalWrapper.setWidthFull();

        VerticalLayout content = new VerticalLayout(topSplit, terminalWrapper);
        content.setSizeFull();
        content.setPadding(false);
        content.setSpacing(false);
        content.expand(topSplit);

        setContent(content);
    }

    private Component buildCentreAndHelp() {
        // Wrap editor with line numbers gutter
        HorizontalLayout editorWithGutter = new HorizontalLayout(lineNumbers, editor);
        editorWithGutter.setSizeFull();
        editorWithGutter.setPadding(false);
        editorWithGutter.setSpacing(false);
        editorWithGutter.expand(editor);

        // Horizontal layout so help slides in on the right of the editor
        HorizontalLayout centreAndHelp = new HorizontalLayout(editorWithGutter, helpPanel);
        centreAndHelp.setSizeFull();
        centreAndHelp.setPadding(false);
        centreAndHelp.setSpacing(false);
        centreAndHelp.expand(editorWithGutter);
        helpPanel.getStyle().set("width", "300px").set("min-width", "300px");
        return centreAndHelp;
    }

    private void buildHelpPanel() {
        helpPanel.getStyle()
                .set("overflow", "auto")
                .set("padding", "var(--lumo-space-m)")
                .set("border-left", "1px solid var(--lumo-contrast-10pct)")
                .set("background", "var(--lumo-contrast-5pct)");

        H3 title = new H3("cln-lang Quick Reference");
        title.getStyle().set("margin-top", "0");

        helpPanel.add(title);
        helpPanel.add(section("Package declaration",
                "package com.example.myapp;"));
        helpPanel.add(section("Imports",
                "import std.console.*;\nimport std.str.*;\nimport std.math.*;\nimport std.array.*;"));
        helpPanel.add(section("Entry point",
                "int main() {\n    // …\n    return 0;\n}"));
        helpPanel.add(section("Variables",
                "var int x = 42;\nvar string s = \"hello\";\nvar bool flag = true;\nvar dec d = 3.14;"));
        helpPanel.add(section("Structs",
                "struct Point {\n    var int x;\n    var int y;\n};\nPoint p = Point(x: 1, y: 2);"));
        helpPanel.add(section("Unions",
                "union Shape = Circle | Square;\n// switch on union:\nswitch s {\n    case Circle c: …\n    case Square q: …\n}"));
        helpPanel.add(section("Arrays",
                "int[] nums = [1, 2, 3];\nint len = nums.length;\nnums[0] = 99;"));
        helpPanel.add(section("Functions",
                "int add(int a, int b) {\n    return a + b;\n}\n// named return:\n(var int sum = 0) add(int a, int b) {\n    sum = a + b;\n    return;\n}"));
        helpPanel.add(section("Console I/O",
                "writeLine(\"text\");\nwrite(\"no newline\");\nstring s = readLine();"));
        helpPanel.add(section("While loop",
                "int i = 0;\nwhile i < 10 {\n    i++;\n}"));
        helpPanel.add(section("Operators",
                "+ - * /  == != < <= > >=\n&& ||  !  ++ --"));
    }

    private Component section(String header, String code) {
        Div div = new Div();
        H5 h = new H5(header);
        h.getStyle().set("margin", "var(--lumo-space-s) 0 2px 0");
        Pre pre = new Pre(code);
        pre.getStyle()
                .set("background", "var(--lumo-contrast-10pct)")
                .set("padding", "var(--lumo-space-s)")
                .set("border-radius", "4px")
                .set("font-size", "var(--lumo-font-size-xs)")
                .set("overflow-x", "auto")
                .set("margin", "0");
        div.add(h, pre);
        return div;
    }

    // ── Tree management ───────────────────────────────────────────────────

    private void refreshTree() {
        List<ClnSourceEntity> sources = sourceService.findAll();
        List<TreeNode> nodes = parserService.buildTree(sources);

        TreeData<TreeNode> data = new TreeData<>();
        for (TreeNode pkg : nodes) {
            data.addItem(null, pkg);
            for (TreeNode child : pkg.getChildren()) {
                data.addItem(pkg, child);
            }
        }

        treeGrid.setDataProvider(new TreeDataProvider<>(data));
        treeGrid.expandRecursively(nodes, 0);
    }

    // ── Event handlers ────────────────────────────────────────────────────

    private void onNodeSelected(TreeNode node) {
        this.selectedNode = node;

        if (node.getSourceId() == null) {
            return;
        }
        sourceService.findById(node.getSourceId()).ifPresent(entity ->
                editor.setValue(entity.getSource() == null ? "" : entity.getSource()));
    }

    private void onNew() {
        Dialog dlg = new Dialog();
        dlg.setHeaderTitle("New CLN Source");

        TextField pkgField = new TextField("Package name");
        pkgField.setPlaceholder("e.g. com.example.myapp");
        pkgField.setWidthFull();

        TextArea srcField = new TextArea("Source code");
        srcField.setWidthFull();
        srcField.setHeight("300px");
        srcField.getStyle().set("font-family", "monospace");
        srcField.setValue("""
                package com.example.newpackage;

                import std.console.*;

                int main() {
                    writeLine("Hello from new package!");
                    return 0;
                }
                """);

        Button btnCreate = new Button("Create", e -> {
            String pkg = pkgField.getValue().trim();
            String src = srcField.getValue();
            if (pkg.isEmpty()) {
                Notification.show("Package name is required", 3000, Notification.Position.MIDDLE);
                return;
            }
            sourceService.save(pkg, src);
            refreshTree();
            dlg.close();
            notify("Package '" + pkg + "' created.", NotificationVariant.LUMO_SUCCESS);
        });
        btnCreate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnCancel = new Button("Cancel", e -> dlg.close());

        dlg.add(new VerticalLayout(pkgField, srcField));
        dlg.getFooter().add(btnCancel, btnCreate);
        dlg.setWidth("600px");
        dlg.open();
    }

    private void onSave() {
        if (selectedNode == null) {
            notify("Select a package first.", NotificationVariant.LUMO_CONTRAST);
            return;
        }
        String pkg = selectedNode.getPackageName();
        String src = editor.getValue();
        sourceService.save(pkg, src);
        refreshTree();
        notify("'" + pkg + "' saved.", NotificationVariant.LUMO_SUCCESS);
    }

    private void onDelete() {
        if (selectedNode == null) {
            notify("Select a package first.", NotificationVariant.LUMO_CONTRAST);
            return;
        }
        String pkg = selectedNode.getPackageName();
        ConfirmDialog confirm = new ConfirmDialog();
        confirm.setHeader("Delete '" + pkg + "'?");
        confirm.setText("This will permanently remove the source from the database.");
        confirm.setCancelable(true);
        confirm.setConfirmButtonTheme("error primary");
        confirm.addConfirmListener(e -> {
            sourceService.deleteByPackageName(pkg);
            editor.clear();
            selectedNode = null;
            refreshTree();
            notify("'" + pkg + "' deleted.", NotificationVariant.LUMO_ERROR);
        });
        confirm.open();
    }

    private void onExecute() {
        if (selectedNode == null) {
            notify("Select a package in the tree first.", NotificationVariant.LUMO_CONTRAST);
            return;
        }

        // Save current editor content before running
        String pkg = selectedNode.getPackageName();
        String src = editor.getValue();
        if (src != null && !src.isBlank()) {
            sourceService.save(pkg, src);
        }

        appendTerminal("▶ Executing package: " + pkg + "\n");

        // Run in background thread so the UI stays responsive
        UI ui = UI.getCurrent();
        new Thread(() -> {
            ClnExecutionService.ExecutionResult result = executionService.execute(pkg, false);
            ui.access(() -> {
                String out = result.output();
                if (out == null || out.isBlank()) {
                    appendTerminal("(no output)\n");
                } else {
                    appendTerminal(out);
                }
                appendTerminal("─── Exit code: " + result.exitCode() + " ───\n");
                refreshTree();
            });
        }).start();
    }

    private void toggleHelp() {
        helpVisible = !helpVisible;
        helpPanel.setVisible(helpVisible);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void appendTerminal(String text) {
        String current = terminal.getValue();
        terminal.setValue(current + text);
        // Scroll textarea to bottom – use inputElement property (Vaadin 24) with a
        // shadow-DOM fallback; guard against null in case the element is not yet rendered.
        terminal.getElement().executeJs(
            "var ta = this.inputElement || (this.shadowRoot && this.shadowRoot.querySelector('textarea'));" +
            "if (ta) ta.scrollTop = ta.scrollHeight;");
    }

    private void notify(String message, NotificationVariant variant) {
        Notification n = Notification.show(message, 3000, Notification.Position.BOTTOM_END);
        n.addThemeVariants(variant);
    }
}
