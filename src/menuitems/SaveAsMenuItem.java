package menuitems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.RandomGeneratorGUI;


@SuppressWarnings("serial")
public class SaveAsMenuItem extends JMenuItem implements ActionListener
{


	public SaveAsMenuItem()
	{
		super("Save As...");
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser();
		//Set working directory
		File here = new File(".");
		fc.setCurrentDirectory(here);
		here.delete();
				
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT Files", "txt");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would save the file.
            System.out.println("Saving: " + file.getName());
            RandomGeneratorGUI.saveAs(file);
        } else {
        	System.out.println("Cancelled by user");
        }
        //log.setCaretPosition(log.getDocument().getLength());
	}
}
