package object_inspector_tests;


import driver.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import object_inspector.Inspector;

public class Inspector_Tests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testGetClassNameDetails() {
		Inspector inspector = new Inspector();
		
		String[] name = inspector.getClassNameDetails(((Object)new String()).getClass());
		assertEquals(name[0],"0");
		assertEquals(name[1],"java.lang.String");
		
		name = inspector.getClassNameDetails(((Object)true).getClass());
		assertEquals(name[0],"0");
		assertEquals(name[1],"java.lang.Boolean");
		
		name = inspector.getClassNameDetails(((Object)new String[2]).getClass());
		assertEquals(name[0],"1");
		assertEquals(name[1],"Ljava.lang.String");
		
		name = inspector.getClassNameDetails(((Object)(long)4).getClass());
		assertEquals(name[0],"0");
		assertEquals(name[1],"java.lang.Long");

		name = inspector.getClassNameDetails(((Object)new Float[1][1]).getClass());
		assertEquals(name[0],"2");
		assertEquals(name[1],"Ljava.lang.Float");
		
		name = inspector.getClassNameDetails(((Object)new byte[1][1][1][1]).getClass());
		assertEquals(name[0],"4");
		assertEquals(name[1],"B");
		
		name = inspector.getClassNameDetails(((Object)new char[1][1][1][1][1][1]).getClass());
		assertEquals(name[0],"6");
		assertEquals(name[1],"C");
	}
	
	@Test
	public void testInspectSuperclass()
	{
		Inspector inspector = new Inspector();
		
		//test1
		inspector.inspectSuperclass((new String[1][1]).getClass());
		assertEquals("Superclass:\r\n\tjava.lang.Object\r\n\r\n", outContent.toString());		
		outContent.reset();
		
		//test2
		inspector.inspectSuperclass((new String("Hello")).getClass());
		assertEquals("Superclass:\r\n\tjava.lang.Object\r\n\r\n", outContent.toString());
		outContent.reset();
	}
	
	@Test
	public void testInspectMethods()
	{
		Inspector inspector = new Inspector();
		inspector.inspectMethods(((Object)new ClassA()).getClass());
		
		/*getDeclaredMethods() appears to sometimes return functions in different orders
		so instead of checking exact order of methods, we just ensure that each method exists */
		assertEquals(outContent.toString().contains("Class Methods:\r\n"), true);
		assertEquals(outContent.toString().contains("\tpublic void run()\r\n"), true);
		assertEquals(outContent.toString().contains("\tpublic java.lang.String toString()\r\n"), true);
		assertEquals(outContent.toString().contains("\tpublic void setVal(int) throws java.lang.Exception\r\n"), true);
		assertEquals(outContent.toString().contains("\tpublic int getVal()\r\n"), true);
		assertEquals(outContent.toString().contains("\tprivate void printSomething()\r\n"), true);
		outContent.reset();
		
		inspector.inspectMethods(((Object)new ClassD()).getClass());
		assertEquals(outContent.toString().contains("Class Methods:\r\n"), true);
		assertEquals(outContent.toString().contains("\tpublic java.lang.String toString()\r\n"), true);
		assertEquals(outContent.toString().contains("\tpublic int getVal3()\r\n"), true);
	}

}
