package object_inspector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings("rawtypes") 
public class Inspector {
	private final String DELIMITER = "   ";
	private boolean _Recursive;
	
	public Inspector()
	{
		_Recursive = false;
	}
	
	/**
	 * 
	 * @param obj
	 * @param recursive
	 */
	public void inspect(Object obj, boolean recursive)
	{
		Class c = obj.getClass();
		String[] classNameDetails = getClassNameDetails(c);
		_Recursive = recursive;
		
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
		
		System.out.println("Fields:");
		inspectFields(obj, obj.getClass(), DELIMITER);
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
				System.out.print(delimiter);
				writeMethod(methods[i].getModifiers(),
						methods[i].getReturnType().getName(),
						methods[i].getName(),
						methods[i].getParameterTypes(),
						methods[i].getExceptionTypes());
			}
		}
		
		System.out.println();
	}
	
	
	/**
	 * writeMethod takes the different elements of a Method and/or Constructor and writes the components in human readable format
	 * 
	 * @param modifiers int representation of the modifiers of the method
	 * @param returnType String representation of the returnType
	 * @param methodName String representation of the methodName
	 * @param parameterTypes a Class[] of the parameter types
	 * @param exceptionTypes a Class[] of the exceptions thrown
	 */
	public void writeMethod(int modifiers, String returnType, String methodName, Class[] parameterTypes, Class[] exceptionTypes)
	{
		String readableModifiers = Modifier.toString(modifiers);
		int j, k;

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
	 * inspectConstructors takes in a class and a delimiter and writes all the constructors of the Class to System.out
	 * prefixed by the specified delimiter.
	 * 
	 * @param c the Class to inspect
	 * @param delimiter prefixes each constructor (can be empty)
	 */
	public void inspectConstructors(Class c, String delimiter)
	{
		Constructor[] constructors = c.getDeclaredConstructors();
		int i;
		
		for (i = 0; i < constructors.length; i++)
		{
			System.out.print(delimiter);
			writeMethod(constructors[i].getModifiers(),
					"",
					constructors[i].getName(),
					constructors[i].getParameterTypes(),
					constructors[i].getExceptionTypes());
		}
		
		System.out.println();
	}
	
	/**
	 * inspectFields takes in a class and a delimiter and writes all the fields of Class to system.out along with their variables.<p>
	 * If inspect() was called with recursive set to true, it will also explore any objects found by traversing those objects as well.
	 * 
	 * @param obj The base level object being inspected
	 * @param c The current class being inspected
	 * @param delimiter prefixes each field (can be empty)
	 */
	public void inspectFields(Object obj, Class c, String delimiter)
	{
		Field[] fields = c.getDeclaredFields();
		String[] fieldDetails;
		int modifiers;
		Object oValue;
		int i;
		
		for(i = 0; i < fields.length; i++)
		{
			System.out.print(delimiter);
			
			//get modifiers before setting accessible to true
			
			fieldDetails = getClassNameDetails(fields[i].getType());
			
			//fieldDetails[0] indicates the dimensions of the array. If it is a 0D array, it is just an element.
			if(fieldDetails[0].compareTo("0") != 0)
			{
				//handle array
				System.out.println("Array encountered, skipping for now.");
			}else
			{
				if(fields[i].getType().isPrimitive() || !_Recursive)
				{
					//handle primitive or just list object contents
					listRawFieldContents(obj, fields[i]);
				}else
				{
					try{
						modifiers = fields[i].getModifiers();
						fields[i].setAccessible(true);
						oValue = fields[i].get(obj);
						System.out.println(Modifier.toString(modifiers) + " " + fields[i].getType().getName() + " = " + (oValue == null ? "null" : ""));
						if(oValue != null)
							inspectFields(oValue, fields[i].getType(), delimiter + DELIMITER);

					}catch(IllegalAccessException ex)
					{
						System.out.println("Cannot access field. Illegal Access. " + ex.getMessage());
					}
				}
			}
		}
	}
	
	//TODO implement
	public void listRawFieldContents(Object obj, Field field)
	{
		String value = "";
		Object oValue;
		//get modifiers before setting accessible to true
		int modifiers = field.getModifiers();
		
		try
		{
			field.setAccessible(true);
			oValue = field.get(obj);
			if (oValue != null)
			{
				if (field.getType().isPrimitive())
					value = getPrimitiveObjectValue(obj, field);
				else
					value = String.valueOf(field.get(obj).hashCode());
			}else
				value = "null";

			System.out.println(Modifier.toString(modifiers) + " " + field.getType().getName() + " = " + value.toString());
		}catch(IllegalAccessException ex)
		{
			System.out.println("Cannot access field. Illegal Access. " + ex.getMessage());
		}
	}
	
	public String getPrimitiveObjectValue(Object obj, Field field) throws IllegalAccessException
	{
		String value = "";
		try{
			switch(field.getType().getName())
			{
				case "byte":
					value = String.valueOf(field.getByte(obj));
					break;
				case "short":
					value = String.valueOf(field.getShort(obj));
					break;
				case "int":
					value = String.valueOf(field.getInt(obj));
					break;
				case "long":
					value = String.valueOf(field.getLong(obj));
					break;
				case "float":
					value = String.valueOf(field.getFloat(obj));
					break;
				case "double":
					value = String.valueOf(field.getDouble(obj));
					break;
				case "boolean":
					value = String.valueOf(field.getBoolean(obj));
					break;
				case "char":
					value = String.valueOf(field.getChar(obj));
					break;
			}
		}catch(IllegalAccessException ex)
		{
			throw ex;
		}
		
		return value;
	}
	/**
	 * getMethod returns a method from "this" class, referenced by name and a Class[] of the parameter types<p>
	 * Example: public void myMethod(String str, Integer i) equates to:<p>
	 *  methodName="str"<p>
	 *  params = new Class[]{String.class, Integer.class};
	 * 
	 * @param methodName the name of the method to be retrieved
	 * @param params a list of the parameters matching the methods signature, in order
	 * @return the Method object representation of the requested Method or null if not found.
	 */
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
	
	/**
	 * inspectInheritedElements inspects all inheritedElements for a particular object by traversing the superclass hierarchy<p>
	 * It takes in a methodName which it will call for every superclass of the supplied object<p>
	 * the methodName that is passed into the function is expected to have the form methodName(Class, String)
	 * 
	 * @param obj The instantiation of the Class that is intended to inspect the hierarchy of
	 * @param methodName The name of the method that will be called on each superclass Class.
	 */
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
