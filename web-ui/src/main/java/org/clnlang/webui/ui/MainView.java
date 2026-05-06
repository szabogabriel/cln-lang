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
import org.clnlang.help.ClnCheatSheet;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
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
    private final TreeGrid<TreeNode> treeGrid   = new TreeGrid<>();
    private final Div                editorHost = new Div();   // CodeMirror mount point
    private final TextArea           terminal   = new TextArea();
    private final Div                helpPanel  = new Div();
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

        // ── Editor (centre) – CodeMirror with cln-lang syntax highlighting ─
        editorHost.setSizeFull();
        editorHost.getStyle()
                .set("overflow", "hidden")
                .set("min-height", "0");
        editorHost.addAttachListener(event ->
            editorHost.getElement().executeJs("""
                (function(container) {
                    if (container._cm) return; // already initialised
                    var BASE = 'https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.65.18/';
                    function go() {
                        if (!window.CodeMirror.modes['cln']) {
                            CodeMirror.defineSimpleMode('cln', {
                                start: [
                                    {regex: /\\/\\/.*/, token: 'comment'},
                                    {regex: /\\/\\*/, token: 'comment', next: 'comment'},
                                    {regex: /"[^"]*"/, token: 'string'},
                                    {regex: /\\d+(?:\\.\\d+)?/, token: 'number'},
                                    {regex: /[a-zA-Z_][a-zA-Z0-9_]*/, token: function(m) {
                                        if (/^(?:package|import|expose|struct|union|var|if|else|while|switch|case|default|return)$/.test(m[0])) return 'keyword';
                                        if (/^(?:int|bool|string|dec)$/.test(m[0])) return 'builtin';
                                        if (m[0] === 'true' || m[0] === 'false') return 'atom';
                                        return null;
                                    }},
                                    {regex: /[+\\-*\\/=<>!&|]+/, token: 'operator'}
                                ],
                                comment: [
                                    {regex: /.*?\\*\\//, token: 'comment', next: 'start'},
                                    {regex: /.*/, token: 'comment'}
                                ],
                                meta: {lineComment: '//'}
                            });
                        }
                        if (!document.getElementById('cln-cm-style')) {
                            var st = document.createElement('style');
                            st.id = 'cln-cm-style';
                            st.textContent = '.CodeMirror{height:100%}.CodeMirror-scroll{box-sizing:border-box}';
                            document.head.appendChild(st);
                        }
                        var cm = CodeMirror(container, {
                            mode: 'cln',
                            lineNumbers: true,
                            value: container._pendingValue || '',
                            indentUnit: 4,
                            tabSize: 4,
                            extraKeys: {
                                'Enter': function(cm) {
                                    var cursor = cm.getCursor();
                                    var indent = cm.getLine(cursor.line).match(/^(\\s*)/)[1];
                                    cm.replaceSelection('\\n' + indent);
                                }
                            }
                        });
                        cm.setSize('100%', '100%');
                        container._cm = cm;
                        container._pendingValue = undefined;
                    }
                    if (window.CodeMirror && window.CodeMirror.defineSimpleMode) { go(); return; }
                    var l = document.createElement('link');
                    l.rel = 'stylesheet'; l.href = BASE + 'codemirror.min.css';
                    document.head.appendChild(l);
                    var s = document.createElement('script');
                    s.src = BASE + 'codemirror.min.js';
                    s.onload = function() {
                        var s2 = document.createElement('script');
                        s2.src = BASE + 'addon/mode/simple.min.js';
                        s2.onload = go;
                        document.head.appendChild(s2);
                    };
                    document.head.appendChild(s);
                })(this);
                """)
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
        // Horizontal layout so help slides in on the right of the editor
        HorizontalLayout centreAndHelp = new HorizontalLayout(editorHost, helpPanel);
        centreAndHelp.setSizeFull();
        centreAndHelp.setPadding(false);
        centreAndHelp.setSpacing(false);
        centreAndHelp.expand(editorHost);
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

        for (ClnCheatSheet.Section s : ClnCheatSheet.getSections()) {
            Details details = new Details(s.title(), new Html(s.html()));
            details.setWidthFull();
            helpPanel.add(details);
        }
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
                setEditorValue(entity.getSource() == null ? "" : entity.getSource()));
    }

    private void onNew() {
        selectedNode = null;
        setEditorValue("");
    }

    private void onSave() {
        editorHost.getElement().executeJs("return this._cm ? this._cm.getValue() : ''")
                .then(String.class, src -> {
                    if (src == null || src.isBlank()) {
                        notify("Nothing to save.", NotificationVariant.LUMO_CONTRAST);
                        return;
                    }
                    String pkg = selectedNode != null
                            ? selectedNode.getPackageName()
                            : extractPackageName(src);
                    sourceService.save(pkg, src);
                    refreshTree();
                    notify("'" + pkg + "' saved.", NotificationVariant.LUMO_SUCCESS);
                });
    }

    /** Extracts the package name from a {@code package foo.bar;} declaration, or returns a default. */
    private String extractPackageName(String src) {
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("^\\s*package\\s+([\\w.]+)\\s*;", java.util.regex.Pattern.MULTILINE)
                .matcher(src);
        return m.find() ? m.group(1) : "- default -";
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
            setEditorValue("");
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

        String pkg = selectedNode.getPackageName();
        appendTerminal("▶ Executing package: " + pkg + "\n");

        // Flush editor content, save, then run in background thread
        editorHost.getElement().executeJs("return this._cm ? this._cm.getValue() : ''")
                .then(String.class, src -> {
                    if (src != null && !src.isBlank()) {
                        sourceService.save(pkg, src);
                    }
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
                });
    }

    private void toggleHelp() {
        helpVisible = !helpVisible;
        helpPanel.setVisible(helpVisible);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /** Set the CodeMirror editor content from server-side code. */
    private void setEditorValue(String value) {
        String v = value == null ? "" : value;
        editorHost.getElement().executeJs(
            "if (this._cm) { this._cm.setValue($0); } else { this._pendingValue = $0; }", v);
    }

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
