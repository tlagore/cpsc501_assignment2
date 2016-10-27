package object_inspector;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
		
		//TODO check if object received is an array and handle accordingly
		System.out.println("Inspecting " + (classNameDetails[0].compareTo("0") == 0 ? "Non-array" : classNameDetails[0] + "D") + " object: ");
		System.out.println("\t" + classNameDetails[1]);
		System.out.println();
		
		inspectSuperclass(c);
		inspectInterfaces(c);
		inspectMethods(c);
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
			System.out.println("Superclass:");
			System.out.println("\t" + superclassName);
		}else
			System.out.println("\tClass does not have a superclass.");
		
		System.out.println();
	}
	
	/**
	 * 
	 * @param c
	 */
	public void inspectInterfaces(Class c)
	{
		Class[] curInterfaces = c.getInterfaces();
		System.out.println("Interfaces Implemented: ");
		if (curInterfaces.length > 0)
		{
			for(int i = 0; i < curInterfaces.length; i ++)
			{
				System.out.println("\t" + curInterfaces[i]);
			}
		}else
			System.out.println("\tNone implemented.");
		
		System.out.println();
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
	
	public void inspectMethods(Class c)
	{
		Method[] methods = c.getDeclaredMethods();
		int i, j, k,
			modifiers;
		String methodName,
			readableModifiers;
		Class[] parameterTypes,
			exceptionTypes;
		Class returnType;
		
		System.out.println("Class Methods:");
		if(methods.length == 0)
			System.out.println("\tNo directly implemented methods");
		else
		{
			for(i = 0; i < methods.length; i++)
			{
				modifiers = methods[i].getModifiers();
				readableModifiers = Modifier.toString(modifiers);
				methodName = methods[i].getName();
				parameterTypes = methods[i].getParameterTypes();
				exceptionTypes = methods[i].getExceptionTypes();
				returnType = methods[i].getReturnType();
				
				System.out.print("\t" + readableModifiers + " " + returnType.getName() + " " + methodName + "(");
				
				if (parameterTypes.length > 0)
				{
					for(j = 0; j < parameterTypes.length - 1; j++)
						System.out.print(parameterTypes[j].getName() +", ");
					
					System.out.print(parameterTypes[j].getName() + ")");
				}else
					System.out.print(")");
				
				if(exceptionTypes.length > 0)
				{
					System.out.print(" throws ");
					for(k = 0; k < exceptionTypes.length - 1; k++)
						System.out.print(exceptionTypes[k].getName() + ", ");
					
					System.out.println(exceptionTypes[k].getName());
				}else
					System.out.println();
			}
		}
		
		System.out.println();
	}
}
