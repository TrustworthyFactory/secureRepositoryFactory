package com.thalesgroup.optet.securerepository.views;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.thalesgroup.optet.securerepository.Activator;
import com.thalesgroup.optet.securerepository.impl.SvnEntry;




public class SvnEntryListLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		System.out.println("---------> getimage");
		   ImageDescriptor descriptor = null;
		   if (element instanceof SvnEntry) {
			   
			   if (((SvnEntry)element).getType().equals("FOLDER")){
		       descriptor = Activator.getImageDescriptor("icons/folder.png");
			   } else if (((SvnEntry)element).getType().equals("FILE")) {
			       descriptor = Activator.getImageDescriptor("icons/file.png");
				   
			   }
		   }

		   return descriptor.createImage();
	}

	public String getText(Object element) {
		
		SvnEntry entry = (SvnEntry) element;
		System.out.println("--------->  getText" + entry.getAuthor());
		return entry.getAuthor() ;
	}
}