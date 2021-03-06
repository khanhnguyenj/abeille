/*
 * Copyright (C) 2005 Jeff Tassin
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.jeta.swingbuilder.gui.editor;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.UIManager;

import com.jeta.forms.gui.common.FormSpecAdapter;
import com.jeta.forms.gui.common.FormUtils;
import com.jeta.forms.gui.components.ComponentSource;
import com.jeta.forms.gui.form.FormComponent;
import com.jeta.forms.gui.form.GridComponent;
import com.jeta.forms.gui.form.GridView;
import com.jeta.jgoodies.forms.layout.RowSpec;
import com.jeta.open.i18n.I18N;
import com.jeta.open.registry.JETARegistry;
import com.jeta.open.resources.ResourceLoader;
import com.jeta.swingbuilder.gui.commands.CommandUtils;
import com.jeta.swingbuilder.gui.commands.EditRowSpecCommand;
import com.jeta.swingbuilder.gui.dnd.DesignerDragSource;
import com.jeta.swingbuilder.gui.project.UserPreferencesNames;
import com.jeta.swingbuilder.gui.utils.FormDesignerUtils;
import com.jeta.swingbuilder.interfaces.userprops.TSUserPropertiesUtils;

public class RowMargin extends Margin {
	/**
	 * The x-position of the middle of the drag thumb
	 */
	private int m_ypos;

	private int m_drag_y_diff;

	/**
	 * Icon for resize thumb
	 */
	private static ImageIcon m_row_thumb;

	static {
		try {
			ResourceLoader loader = (ResourceLoader) JETARegistry.lookup(ResourceLoader.COMPONENT_ID);
			m_row_thumb = loader.loadImage("images/forms/thumb_horizontal.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ctor
	 *
	 * @param view
	 *            the top level view that will render the resize indicator
	 * @param viewport
	 *            the viewport that contains the top level view. Needed when the
	 *            view is scrolled right or down
	 */
	public RowMargin(FormComponent form, GridView topview, ComponentSource compSrc, JViewport viewport, boolean show) {
		super(Orientation.VERTICAL, form, topview, compSrc, viewport, show);
		if(!FormDesignerUtils.isFixed()){
			addMouseListener(new MouseHandler());
			addMouseMotionListener(new MouseMotionHandler());
		}
	}

	@Override
    protected void paintComponent(Graphics g) {
		if (isPaintMargin()) {
			Rectangle clip = g.getClipBounds();
			g.setColor(UIManager.getColor("control"));
			g.fillRect(clip.x, clip.y, clip.width, clip.height);

			if (m_gc != null) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				g2.drawImage(m_row_thumb.getImage(), 0, m_ypos - THUMB_HEIGHT / 2, this);
			}
		}
	}

	/**
	 * Changes the thumb position to the specified grid component
	 */
	@Override
    void update(GridComponent gc) {
		DesignerDragSource dds = (DesignerDragSource) JETARegistry.lookup(DesignerDragSource.COMPONENT_ID);
		if (dds != null && dds != this && dds.isDragging()) {
			return;
		}

		if (isPaintMargin() && !m_dragging && m_compsrc.isSelectionTool()) {
			m_gc = gc;
			if (gc != null) {
				GridView parentview = gc.getParentView();
				if (parentview == m_view) {
					m_gc = null;
				}
				else {
					if (parentview != null) {
						Insets insets = parentview.getInsets();
						int row_offset = gc.getRow() + gc.getRowSpan() - 1;
						Point pt = javax.swing.SwingUtilities.convertPoint(parentview, 0, parentview.getRowOrgY(row_offset)
								+ parentview.getRowHeight(row_offset) + insets.top, this);
						m_ypos = pt.y;
					}
				}
			}
			repaint();
		}
	}

	private class MouseHandler extends MouseAdapter {
		@Override
        public void mousePressed(MouseEvent e) {
			int y = e.getY();
			if ((y >= (m_ypos - THUMB_WIDTH / 2)) && (y <= (m_ypos + THUMB_WIDTH)) && isPaintMargin()) {
				if (m_gc != null) {
					startDrag();
					m_drag_y_diff = e.getY() - m_ypos;
					m_resize_indicator.setPosition(m_ypos);
					int row = m_gc.getRow() + m_gc.getRowSpan() - 1;
					GridView view = m_gc.getParentView();
					FormSpecAdapter adapter = new FormSpecAdapter(view.getRowSpec(row));
					if (adapter.isComponentSize()) {
						m_units = TSUserPropertiesUtils.getString(UserPreferencesNames.ID_DEFAULT_RESIZE_UNITS, "PX");
						if (!FormUtils.isValidUnits(m_units))
							m_units = "PX";
					}
					else {
						m_units = adapter.getConstantUnits();
						if (m_units == null)
							m_units = "PX";
					}
				}
			}
			else {
				m_view.deselectAll();
				m_form.setSelected(true);
				m_gc = null;
			}
		}

		@Override
        public void mouseReleased(MouseEvent e) {
			if (m_dragging && m_gc != null) {
				if (m_comp_size > 0) {
					int row = m_gc.getRow() + m_gc.getRowSpan() - 1;
					GridView view = m_gc.getParentView();
					RowSpec oldspec = view.getRowSpec(row);
					FormEditor editor = FormEditor.getEditor(view);

					if (editor != null) {
						NewSizeAdapter adapter = new NewSizeAdapter(oldspec, m_comp_size, m_units);
						if (!adapter.isResizeGrow() && !adapter.isBoundedSize()) {
							String newspec = FormUtils.toEncodedString(adapter);
							EditRowSpecCommand cmd = new EditRowSpecCommand(view.getParentForm(), row, new RowSpec(newspec), oldspec);
							CommandUtils.invoke(cmd, editor);
						}
						else {
							String msg = null;
							if (adapter.isBoundedSize()) {
								msg = I18N
										.getLocalizedMessage("The row size is set to bounded.\nYou must manually set the size in the row specification window.");
							}
							else if (adapter.isResizeGrow()) {
								msg = I18N
										.getLocalizedMessage("The row resize behavior is set to grow.\nYou must manually set the size in the row specification window.");
							}
							else {
								msg = I18N.getLocalizedMessage("You must manually set the size in the row specification window.");
							}

							String title = I18N.getLocalizedMessage("Error");
							JOptionPane.showMessageDialog(m_view, msg, title, JOptionPane.ERROR_MESSAGE);
						}

					}
					else {
						assert (false);
					}
				}

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					@Override
                    public void run() {
						m_overlay.setResizeIndicator(null);
						m_overlay.repaint();
						m_dragging = false;
						update(m_gc);
					}
				});
			}
		}
	}

	private class MouseMotionHandler extends MouseMotionAdapter {
		@Override
        public void mouseDragged(MouseEvent e) {
			if (m_gc != null && m_dragging) {
				int y = e.getY() - m_drag_y_diff;

				GridView parentview = m_gc.getParentView();
				Insets insets = parentview.getInsets();

				Point pt = javax.swing.SwingUtilities.convertPoint(RowMargin.this, 0, y, parentview);
				Point offsetpt = m_viewport.getViewPosition();

				// pt.y is the new height of the grid component
				int pixels = pt.y - parentview.getRowOrgY(m_gc.getRow() + m_gc.getRowSpan() - 1) - insets.top;
				m_comp_size = convertPoint(pixels, m_units);
				m_ypos = y;
				repaint();
				m_resize_indicator.setPosition(m_ypos + offsetpt.y);
				m_resize_indicator.setSize(m_comp_size, m_units, offsetpt.x);
				m_overlay.repaint();
			}
		}
	}

}
