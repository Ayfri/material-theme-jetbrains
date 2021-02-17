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

import com.intellij.ui.ColorChooser;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.RelativeFont;
import com.intellij.ui.UIBundle;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.beans.EventHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings({"unused",
  "ThisEscapedInObjectConstruction",
  "MagicNumber",
  "SyntheticAccessorCall",
  "SuspiciousGetterSetter"})
public final class ColorPanelWithOpacity extends JComponent {
  private static final RelativeFont MONOSPACED_FONT = RelativeFont.SMALL.family(Font.MONOSPACED);
  @NonNls
  private static final String ON_PRESSED = "onPressed";
  @NonNls
  private static final String MOUSE_PRESSED = "mousePressed";
  @NonNls
  private static final String KEY_CODE = "keyCode";
  @NonNls
  private static final String KEY_PRESSED = "keyPressed";
  @NonNls
  private static final String COLOR_PANEL_CHANGED = "colorPanelChanged";
  @NonNls
  private static final String HEX_STR = " %s ";

  private final Collection<ActionListener> myListeners = new CopyOnWriteArrayList<>();
  private final JTextField myTextField = new JBTextField(10);
  private boolean myEditable;
  @Nullable
  private ActionEvent myEvent;
  @Nullable
  private Color myColor;

  public ColorPanelWithOpacity() {
    addImpl(myTextField, null, 0);
    myColor = null;
    setEditable();
    setMinimumSize(JBUI.size(10, 10));
    myTextField.addMouseListener(EventHandler.create(MouseListener.class, this, ON_PRESSED, null, MOUSE_PRESSED));
    myTextField.addKeyListener(EventHandler.create(KeyListener.class, this, ON_PRESSED, KEY_CODE, KEY_PRESSED));
    myTextField.setEditable(false);
    MONOSPACED_FONT.install(myTextField);
    ColorPainter.BACKGROUND.install(myTextField, true);
  }

  @SuppressWarnings("unused") // used from event handler
  public void onPressed(final int keyCode) {
    if (keyCode == KeyEvent.VK_SPACE) {
      onPressed();
    }
  }

  @SuppressWarnings("WeakerAccess")
  public void onPressed() {
    if (myEditable && isEnabled()) {
      final Color color = ColorChooser.chooseColor(this, UIBundle.message("color.panel.select.color.dialog.description"), myColor, true);
      if (color != null) {
        setSelectedColor(color);
        if (!myListeners.isEmpty() && (myEvent == null)) {
          try {
            myEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, COLOR_PANEL_CHANGED);
            for (final ActionListener listener : myListeners) {
              listener.actionPerformed(myEvent);
            }
          } finally {
            myEvent = null;
          }
        }
      }
    }
  }

  @Override
  public void doLayout() {
    final Rectangle bounds = new Rectangle(getWidth(), getHeight());
    JBInsets.removeFrom(bounds, getInsets());
    myTextField.setBounds(bounds);
  }

  @Override
  public Dimension getPreferredSize() {
    if (isPreferredSizeSet()) {
      return super.getPreferredSize();
    }
    final Dimension size = myTextField.getPreferredSize();
    JBInsets.addTo(size, getInsets());
    return size;
  }

  @Override
  public String getToolTipText() {
    return myTextField.getToolTipText();
  }

  public void removeActionListener(final ActionListener actionlistener) {
    myListeners.remove(actionlistener);
  }

  public void addActionListener(final ActionListener actionlistener) {
    myListeners.add(actionlistener);
  }

  @Nullable
  public Color getSelectedColor() {
    return myColor;
  }

  public void setSelectedColor(@Nullable final Color color) {
    myColor = color;
    updateSelectedColor();
  }

  @SuppressWarnings({"UseJBColor",
    "ReuseOfLocalVariable",
    "OverlyComplexMethod"})
  private void updateSelectedColor() {
    final boolean enabled = isEnabled();
    if (enabled && myEditable) {
      myTextField.setEnabled(true);
      myTextField.setToolTipText(UIBundle.message("color.panel.select.color.tooltip.text"));
    } else {
      myTextField.setEnabled(false);
      myTextField.setToolTipText(null);
    }

    Color color = enabled ? myColor : null;
    if (color != null) {
      myTextField.setText(String.format(HEX_STR, ColorUtil.toHex(color, true).toUpperCase(Locale.ENGLISH)));
    } else {
      myTextField.setText(null);
      color = getBackground();
    }
    myTextField.setBackground(color);
    myTextField.setSelectedTextColor(color);
    myTextField.setOpaque(false);

    if (color != null) {
      int gray = (int) (0.212656 * color.getRed() + 0.715158 * color.getGreen() + 0.072186 * color.getBlue());
      final int delta = gray < 0x20 ? 0x60 : gray < 0x50 ? 0x40 : gray < 0x80 ? 0x20 : gray < 0xB0 ? -0x20 : gray < 0xE0 ? -0x40 : -0x60;
      gray += delta;
      color = new Color(gray, gray, gray);
      myTextField.setDisabledTextColor(color);
      myTextField.setSelectionColor(color);
      gray += delta;
      color = new Color(gray, gray, gray);
      myTextField.setForeground(color);
    }
  }

  private void setEditable() {
    myEditable = true;
    updateSelectedColor();
  }

  @Override
  public void setEnabled(final boolean enabled) {
    super.setEnabled(enabled);
    updateSelectedColor();
  }

  private static final class ColorPainter implements Highlighter.HighlightPainter, PropertyChangeListener {
    @NonNls
    private static final String PROPERTY = "highlighter";
    static final ColorPainter BACKGROUND = new ColorPainter();

    @SuppressWarnings("StandardVariableNames")
    @Override
    public void paint(final Graphics g, final int p0, final int p1, final Shape bounds, final JTextComponent c) {
      final Color color = c.getBackground();
      if (color != null) {
        g.setColor(color);
        final Rectangle rectangle = bounds instanceof Rectangle ? (Rectangle) bounds : bounds.getBounds();
        g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
      }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
      final Object source = evt.getSource();
      if ((source instanceof JTextComponent) && PROPERTY.equals(evt.getPropertyName())) {
        install((JTextComponent) source, false);
      }
    }

    private void install(final JTextComponent component, final boolean listener) {
      try {
        final Highlighter highlighter = component.getHighlighter();
        if (highlighter != null) {
          highlighter.addHighlight(0, 0, this);
        }
      } catch (final BadLocationException ignored) {
      }
      if (listener) {
        component.addPropertyChangeListener(PROPERTY, this);
      }
    }
  }

}
