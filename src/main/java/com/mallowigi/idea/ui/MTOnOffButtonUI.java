/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.mallowigi.idea.ui;

import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.ui.components.OnOffButton;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI;
import com.mallowigi.idea.utils.MTUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

public final class MTOnOffButtonUI extends BasicToggleButtonUI {
  private static final Dimension TOGGLE_SIZE = new JBDimension(18, 18);
  private static final Dimension BUTTON_SIZE = new JBDimension(32, 14);
  private static final Border BUTTON_BORDER = JBUI.Borders.empty(1, 10);
  private static final int ARC = 16;

  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass",
    "unused"})
  public static ComponentUI createUI(final JComponent component) {
    component.setBorder(BUTTON_BORDER);
    return new MTOnOffButtonUI();
  }

  @Override
  public Dimension getPreferredSize(final JComponent c) {
    final Dimension size = new Dimension(BUTTON_SIZE);
    JBInsets.addTo(size, BUTTON_BORDER.getBorderInsets(c));
    return size;
  }

  @Override
  public Dimension getMaximumSize(final JComponent c) {
    return getPreferredSize(c);
  }

  @Override
  public Dimension getMinimumSize(final JComponent c) {
    return getPreferredSize(c);
  }

  @SuppressWarnings("FeatureEnvy")
  @Override
  public void paint(final Graphics g, final JComponent c) {
    if (!(c instanceof OnOffButton)) {
      return;
    }

    final OnOffButton button = (OnOffButton) c;
    final Graphics2D g2 = (Graphics2D) g.create();
    final GraphicsConfig config = GraphicsUtil.setupAAPainting(g2);

    try {
      final Insets insets = c.getInsets();
      final Point origin = new Point((c.getWidth() - BUTTON_SIZE.width) / 2 + insets.left,
        (c.getHeight() - BUTTON_SIZE.height) / 2 + insets.top);

      // Background
      g2.setColor(button.isSelected() ? MTUI.Switch.getOnSwitchColor() : MTUI.Switch.getOffSwitchColor());
      g2.fillRoundRect(origin.x, origin.y, BUTTON_SIZE.width, BUTTON_SIZE.height, ARC, ARC);

      // Fill
      g2.setColor(button.isSelected() ? MTUI.Switch.getOnThumbColor() : MTUI.Switch.getOffThumbColor());

      final Point location = new Point(
        (button.isSelected() ? JBUI.scale(20) : JBUI.scale(-2)) + origin.x,
        JBUI.scale(-2) + origin.y);
      g2.fillOval(location.x, location.y, TOGGLE_SIZE.width, TOGGLE_SIZE.height);

      config.restore();
    } finally {
      g2.dispose();
    }
  }

}
