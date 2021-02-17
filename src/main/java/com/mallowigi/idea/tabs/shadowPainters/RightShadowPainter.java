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

package com.mallowigi.idea.tabs.shadowPainters;

import com.intellij.ui.ColorUtil;

import java.awt.*;

@SuppressWarnings("MagicNumber")
public final class RightShadowPainter extends ShadowPainter {

  @Override
  public void drawShadow(final Graphics2D g2d, final Point from, final Point to) {
    final int w = (int) to.getX();
    final int h = (int) to.getY();
    if (h == 0) {
      return;
    }

    final Color bg = getShadowColor();
    g2d.setColor(ColorUtil.toAlpha(bg, 50));
    g2d.drawLine(w - 1, 0, w - 1, h);

    // draw the drop-shadow
    final Color mid = ColorUtil.toAlpha(bg, 40);
    g2d.setColor(mid);
    g2d.drawLine(w - 2, 0, w - 2, h);

    // draw the drop-shadow
    final Color mid2 = ColorUtil.toAlpha(bg, 30);
    g2d.setColor(mid2);
    g2d.drawLine(w - 3, 0, w - 3, h);
    g2d.drawLine(w - 4, 0, w - 4, h);

    final Color edge = ColorUtil.toAlpha(bg, 25);
    g2d.setColor(edge);
    g2d.drawLine(w - 5, 0, w - 5, h);
  }

}
