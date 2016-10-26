package object_inspector;

import java.util.Vector;

@SuppressWarnings("rawtypes") 
public class Inspector {
	public Inspector()
	{
		
	}
	
	public void inspect(Object obj, boolean recursive)
	{
		Vector<Class> interfaces = new Vector<Class>();
		Class c = obj.getClass();
		String[] classNameDetails = getClassNameDetails(c);
		System.out.println("Inspecting " + (classNameDetails[0].compareTo("0") == 0 ? "Non-array" : classNameDetails[0] + "D") + " object: " + classNameDetails[1] + "");
		inspectSuperclass(c);
		inspectInterfaces(c);
	}
	
	/**
	 * getClassNameDetails
	 * 
	 * takes in a class and returns the details of the classes name in the form of a String[2].
	 * 
	 * @param c the class for which the name is to be derived
	 * @return String[], String[0] = Dimensions of array (0 if not an array), String[1] contains the name of the class (or char code if a primitive non-array).
	 */
	public String[] getClassNameDetails(Class c)
	{
		String[] fullClassName = new String[]{"",""};
		String className = c.getName();
		
		int arrayDimensions = 0;
		
		while(className.charAt(arrayDimensions) == '[')
		{
			arrayDimensions++;
		}
		
		fullClassName[0] = String.valueOf(arrayDimensions);
		
		if (arrayDimensions != 0)
		{
			//has dimensions >= 1, remove ['s from class name and drop ';' in case it is an array of objects
			fullClassName[1] = className.substring(arrayDimensions, className.length()).replace(";", "");
		}else
			fullClassName[1] = className;
		
		return fullClassName;
	}
	
	/**
	 * 
	 * @param c
	 */
	public void inspectSuperclass(Class c)
	{
		Class superclassObject = c.getSuperclass();
		String superclassName;
		
		//null indicates that c was of type Object
		if (superclassObject != null)
		{
			superclassName = superclassObject.getName();
			System.out.println("Superclass: " + superclassName);
		}
	}
	
	/**
	 * 
	 * @param c
	 */
	public void inspectInterfaces(Class c)
	{
		Class[] curInterfaces = c.getInterfaces();
		if (curInterfaces.length > 0)
		{
			for(int i = 0; i < curInterfaces.length; i ++)
			{
				System.out.println(curInterfaces[i]);
			}
		}
	}
	
	/**
	 * 
	 * @param c
	 * @param interfaces
	 */
	public void getAllSuperClasses(Class c, Vector<Class> superclasses)
	{
		Class superclass = c.getSuperclass();
		if (superclass != null)
		{
			superclasses.addElement(superclass);
			getAllSuperClasses(superclass, superclasses);
		}
	}
}
