package menuitems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.RandomGeneratorGUI;


@SuppressWarnings("serial")
public class OpenMenuItem extends JMenuItem implements ActionListener
{


	public OpenMenuItem()
	{
		super("Open...");
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		System.out.println("open button pressed!");
		JFileChooser fc = new JFileChooser();
		
		//Set working directory
		File here = new File(".");
		fc.setCurrentDirectory(here);
		here.delete();
				
				
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT Files", "txt");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            RandomGeneratorGUI.open(file);
        } else {
        	System.out.println("Cancelled by user");
        }
	}
}
