package object_inspector_tests;

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
		inspector.inspectSuperclass((new String[1][1]).getClass());
		assertEquals("Superclass: java.lang.Object\r\n", outContent.toString());
		
		outContent.reset();
		inspector.inspectSuperclass((new String("Hello")).getClass());
		String out = outContent.toString();
		assertEquals("Superclass: java.lang.Object\r\n", outContent.toString());
	}

}
