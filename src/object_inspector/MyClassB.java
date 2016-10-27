package object_inspector;

import java.io.IOException;

public class MyClassB extends MyClassA {
	public MyClassB()
	{
		
	}
	
	public void functionInB(int fun) throws IOException
	{
		throw new IOException("new");
	}
}
