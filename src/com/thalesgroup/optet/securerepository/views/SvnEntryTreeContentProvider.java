package com.thalesgroup.optet.securerepository.views;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.thalesgroup.optet.securerepository.impl.SvnEntry;


public class SvnEntryTreeContentProvider
   extends ArrayContentProvider
   implements ITreeContentProvider {

   public Object[] getChildren(Object parentElement) {
	   SvnEntry component = (SvnEntry) parentElement;
      return component.getChildren().toArray();
   }

   public Object getParent(Object element) {
	   SvnEntry component = (SvnEntry) element;
      return component.getParent();
   }

   public boolean hasChildren(Object element) {
	   int length = 0;
	   if (element instanceof SvnEntry) {
		   SvnEntry component = (SvnEntry) element;
		   length = component.getChildren().size();
	   }
		   
      return length > 0;
   }
}