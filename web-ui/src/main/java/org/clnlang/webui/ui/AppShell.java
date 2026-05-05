package org.clnlang.webui.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;

/**
 * Vaadin application shell configuration.
 * {@code @Push} must live here (on an {@link AppShellConfigurator}),
 * not on individual view classes.
 */
@Push
public class AppShell implements AppShellConfigurator {
}
