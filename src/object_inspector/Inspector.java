package object_inspector;

public class Inspector {
	public Inspector()
	{
		
	}
	
	public void inspect(Object obj, boolean recursive)
	{
		Class c = obj.getClass();
		System.out.println(c.getName());
	}
}
