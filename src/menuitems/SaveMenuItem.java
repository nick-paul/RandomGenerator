package menuitems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ui.RandomGeneratorGUI;


@SuppressWarnings("serial")
public class SaveMenuItem extends JMenuItem implements ActionListener
{


	public SaveMenuItem()
	{
		super("Save");
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		RandomGeneratorGUI.save();
	}
}
