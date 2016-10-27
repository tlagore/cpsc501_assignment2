package object_inspector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings("rawtypes") 
public class Inspector {
	private final String DELIMITER = "   ";
	
	public Inspector()
	{
		
	}
	
	public void inspect(Object obj, boolean recursive)
	{
		Vector<Class> interfaces = new Vector<Class>();
		Class c = obj.getClass();
		String[] classNameDetails = getClassNameDetails(c);
		
		//TODO check if object received is an array and handle accordingly
		System.out.println("Inspecting " + (classNameDetails[0].compareTo("0") == 0 ? "non-array" : classNameDetails[0] + "D") + " object: ");
		System.out.println(DELIMITER + classNameDetails[1]);
		System.out.println();
		
		inspectSuperclass(c);
		inspectInterfaces(c);
		
		System.out.println("Declared Class Methods:");
		inspectMethods(c, DELIMITER);
		
		System.out.println("Inherited Methods:");
		inspectInheritedElements(obj, "inspectMethods");

		System.out.println("Declared Constructors:");
		inspectConstructors(c, DELIMITER);
		
		System.out.println("Inherited Constructors:");
		inspectInheritedElements(obj, "inspectConstructors");
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
	 * inspectSuperclass takes in an object of type Class and prints the Class' immediate superclass to system.out.
	 * 
	 * If the Class has no superclass, a message indicating as such is written to system.out.
	 * 
	 * @param c the class being inspected
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
			System.out.println(DELIMITER + superclassName);
		}else
			System.out.println(DELIMITER + "Class does not have a superclass.");
		
		System.out.println();
	}
	
	/**
	 *  inspectInterfaces takes in an object of type Class and prints the Class' interfaces to system.out
	 *  
	 *  If the class does not implement any interfaces, a message indicating as such is writtent o system.out
	 * 
	 * @param c the class being inspected
	 */
	public void inspectInterfaces(Class c)
	{
		Class[] curInterfaces = c.getInterfaces();
		System.out.println("Interfaces Implemented: ");
		if (curInterfaces.length > 0)
		{
			for(int i = 0; i < curInterfaces.length; i ++)
			{
				System.out.println(DELIMITER + curInterfaces[i]);
			}
		}else
			System.out.println(DELIMITER + "None implemented.");
		
		System.out.println();
	}
	
	/**
	 * getAllSuperclasses takes in a Class and a Class Vector and recursively fills the Vector with all superclasses
	 * of the given Class object.
	 *  
	 * @param c the Class being inspected
	 * @param superclasses the Superclasses of the given Class
	 */
	public void getAllSuperclasses(Class c, Vector<Class> superclasses)
	{
		Class superclass = c.getSuperclass();
		if (superclass != null)
		{
			superclasses.addElement(superclass);
			getAllSuperclasses(superclass, superclasses);
		}
	}
	
	/**
	 * inspectMethods prints all declared methods of a given Class to system.out in the form:<p>
	 * [modifier1] [modifier2] ... [returnType] [methodName] ([parameter1], [parameter2]...) throws Exception1, Exception2 ... \r\n
	 * 
	 * @param c
	 */
	public void inspectMethods(Class c, String delimiter)
	{
		Method[] methods = c.getDeclaredMethods();
		int i;
		
		if(methods.length == 0)
			System.out.println(delimiter + "No implemented methods");
		else
		{
			for(i = 0; i < methods.length; i++)
			{			
				//System.out.print(delimiter);
				writeMethod(methods[i].getModifiers(),
						methods[i].getReturnType().getName(),
						methods[i].getName(),
						methods[i].getParameterTypes(),
						methods[i].getExceptionTypes(),
						delimiter);
			}
		}
		
		System.out.println();
//				modifiers = methods[i].getModifiers();
//				readableModifiers = Modifier.toString(modifiers);
//				methodName = methods[i].getName();
//				parameterTypes = methods[i].getParameterTypes();
//				exceptionTypes = methods[i].getExceptionTypes();
//				returnType = methods[i].getReturnType();
//				
//				System.out.print(delimiter + readableModifiers + " " + returnType.getName() + " " + methodName + "(");
//				
//				if (parameterTypes.length > 0)
//				{
//					for(j = 0; j < parameterTypes.length - 1; j++)
//						System.out.print(parameterTypes[j].getName() +", ");
//					
//					System.out.print(parameterTypes[j].getName() + ")");
//				}else
//					System.out.print(")");
//				
//				if(exceptionTypes.length > 0)
//				{
//					System.out.print(" throws ");
//					for(k = 0; k < exceptionTypes.length - 1; k++)
//						System.out.print(exceptionTypes[k].getName() + ", ");
//					
//					System.out.println(exceptionTypes[k].getName());
//				}else
//					System.out.println();
//			}
	}
	
	
	/**
	 * 
	 * @param modifiers
	 * @param returnType
	 * @param methodName
	 * @param parameterTypes
	 * @param exceptionTypes
	 * @param delimiter
	 */
	public void writeMethod(int modifiers, String returnType, String methodName, Class[] parameterTypes, Class[] exceptionTypes, String delimiter)
	{
		String readableModifiers = Modifier.toString(modifiers);
		int j, k;
		
		System.out.print(delimiter);
		//write method modifiers, return type, and method name
		System.out.print(readableModifiers + (returnType == "" ? " " : (" " + returnType + " ")) + methodName + "(");
		
		//write parameters
		if (parameterTypes.length > 0)
		{
			for(j = 0; j < parameterTypes.length - 1; j++)
				System.out.print(parameterTypes[j].getName() +", ");
			
			System.out.print(parameterTypes[j].getName() + ")");
		}else
			System.out.print(")");
		
		//write exceptions
		if(exceptionTypes.length > 0)
		{
			System.out.print(" throws ");
			for(k = 0; k < exceptionTypes.length - 1; k++)
				System.out.print(exceptionTypes[k].getName() + ", ");
			
			System.out.println(exceptionTypes[k].getName());
		}else
			System.out.println();
	}
	
	/**
	 * 
	 * @param c
	 * @param delimiter
	 */
	public void inspectConstructors(Class c, String delimiter)
	{
		Constructor[] constructors = c.getDeclaredConstructors();
		int i;
		
		for (i = 0; i < constructors.length; i++)
		{
			writeMethod(constructors[i].getModifiers(),
					"",
					constructors[i].getName(),
					constructors[i].getParameterTypes(),
					constructors[i].getExceptionTypes(),
					delimiter);
		}
		
		System.out.println();
	}
	
	
	public Method getMethod(String methodName, Class[] params)
	{
		Method method = null;
		try{
			method = this.getClass().getMethod(methodName, params);
		}catch(NoSuchMethodException ex)
		{
			System.out.println(ex.getMessage());
		}
		
		return method;
	}
	
	public void inspectInheritedElements(Object obj, String methodName)
	{
		Class c = obj.getClass();
		Vector<Class> superclasses = new Vector<Class>();		
		Class curClass;
		String delimiter = "";
		Method method = getMethod(methodName, new Class[]{Class.class, String.class});
		
		getAllSuperclasses(c, superclasses);
		
		if (method == null)
			return;
		
		if (superclasses.size() > 0)
		{
			for(Iterator i = superclasses.iterator(); i.hasNext();)
			{
				curClass = (Class)i.next();
				System.out.println(delimiter + curClass.getName());
				
				delimiter += DELIMITER;
				try{
					method.invoke(this, new Object[] {curClass, new String(delimiter) });
				}catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
			}
		}else
			System.out.println("Class does not have superclass. No inherited methods.");
	}
}
