package comp2402a3;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
// import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*
 * This class generates and displays a Random Binary Search Tree
 */
public class TreeTester implements ActionListener, ItemListener {
	A3Tree t;
	JPanel output;
	JScrollPane scrollPane;
	String newline = "\n";

	// Size of the random binary search tree
	// You can change this to generate a larger or smaller tree.
	int size = 15;

	public TreeTester() {
		randomBST();
		System.out.println("smallestLeaf(): "+t.smallestLeaf());
		System.out.println("maxWidth(): "+t.maxWidth());
		System.out.println("maxSum(): "+t.maxSum());
	}

	public void randomBST(){
		t = new A3Tree();
        List<Integer> list = new ArrayList<>();
		
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		  
        Collections.shuffle(list);     
        t.addAll(list);
       
        System.out.println(t);

		// Make sure you call this method before drawing the tree.
		t.inorderDraw();
	}
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the first menu.
		menu = new JMenu("Actions");
		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("new random binary search tree");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("set size");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuBar.add(menu);

		return menuBar;
	}

	public Container createContentPane() {
		// Create the content-pane-to-be.
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);


		// Create a scrolled text area.
		output = new JPanel() {
			private static final long serialVersionUID = 2196729896823372494L;
			public void paint(Graphics g) {
				if (t != null) recursivePaint(g, t.r);
			}
			protected void recursivePaint(Graphics g, A3Tree.Node u) {
				final int r = 26,  m = 40;
				setBackground(Color.WHITE);
				if (u == null) return;
				
				if (u.left != null) {
					g.drawLine(u.position.x * m + r/2, u.position.y * m  + r/2, 
							u.left.position.x * m + r/2, u.left.position.y *m + r/2);
					recursivePaint(g, u.left);
				}
				if (u.right != null) {
					g.drawLine(u.position.x * m + r/2, u.position.y * m + r/2, 
							u.right.position.x * m + r/2, u.right.position.y *m + r/2);
					recursivePaint(g, u.right);
				}
				Color c = g.getColor();
				g.fillOval(u.position.x * m, u.position.y * m, r, r);
				g.setColor(Color.WHITE);
				int offset = 8;
				if (u.x < 10) offset = 4;
				g.drawString(Integer.toString(u.x), u.position.x *m+r/2 - offset, u.position.y * m+r/2+4);
				g.setColor(c);
			}
		};

		// Add the text area to the content pane.
		output.setBackground(Color.WHITE);
		contentPane.add(output, BorderLayout.CENTER);

		return contentPane;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) (e.getSource());
		String s = "Action event detected." + newline + "    Event source: "
				+ source.getText();
		System.out.println(s);
		if (source.getText().equals("new random binary search tree")) {
			System.out.println("new random tree");
			randomBST();
			System.out.println("smallestLeaf(): "+t.smallestLeaf());
			System.out.println("maxWidth(): "+t.maxWidth());
			System.out.println("maxSum(): "+t.maxSum());
			output.repaint();
		} else 	if (source.getText().equals("set size")) {
			// get an integer from the user with a JDialog
			String s1 = JOptionPane.showInputDialog("Enter the size of the tree");
			size = Integer.parseInt(s1);
			randomBST();
			System.out.println("smallestLeaf(): "+t.smallestLeaf());
			System.out.println("maxWidth(): "+t.maxWidth());
			System.out.println("maxSum(): "+t.maxSum());
			output.repaint();
		
		}else if (source.getText().equals("in-order layout")) {
			t.inorderDraw();
			output.repaint();
		}
		

	}

	public void itemStateChanged(ItemEvent e) {
//		JMenuItem source = (JMenuItem) (e.getSource());
//		String s = "Item event detected."
//				+ newline
//				+ "    Event source: "
//				+ source.getText()
//				+ newline
//				+ "    New state: "
//				+ ((e.getStateChange() == ItemEvent.SELECTED) ? "selected"
//						: "unselected");
//		System.out.println(s);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("MenuDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		TreeTester demo = new TreeTester();
		frame.setJMenuBar(demo.createMenuBar());
		frame.setContentPane(demo.createContentPane());

		// Display the window.
		frame.setSize(800, 400);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}