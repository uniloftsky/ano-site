package test.api;

import java.util.Locale;

import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.common.AbstractAPIImpl;

public class CreateThread implements Runnable{
	
	public static void main(String[] a) throws Exception{
		APICallContext.getCallContext().setCurrentUserId("123");
		APICallContext.getCallContext().setCurrentLocale(new Locale("us","en", "xyz"));
		APICallContext.getCallContext().setAttribute("test", "test");
		System.out.println("TEST: "+APICallContext.getCallContext().getAttribute("test"));
		new Thread(new CreateThread()).start();
		System.out.println("---------");
		Thread.sleep(500);
		new Thread(new CreateThread()).start();
		Thread.sleep(500);
		System.out.println("TEST: "+APICallContext.getCallContext().getAttribute("test"));
		
	}
	
	public void run(){
		APICallContext.getCallContext().setAttribute("test","childish");
		System.out.println("started thread ... ");
		System.out.println("Locale: "+APICallContext.getCallContext().getCurrentLocale());
		System.out.println("UserId: "+APICallContext.getCallContext().getCurrentUserId());
	}
}

