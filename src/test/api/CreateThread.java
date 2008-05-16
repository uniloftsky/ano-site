package test.api;

import java.util.Locale;

import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.common.AbstractAPIImpl;

public class CreateThread implements Runnable{
	
	public static void main(String[] a) throws Exception{
		APICallContext.getCallContext().setCurrentUserId("123");
		APICallContext.getCallContext().setCurrentLocale(new Locale("us","en", "xyz"));
		new Thread(new CreateThread()).start();
		System.out.println("---------");
		Thread.sleep(500);
		AbstractAPIImpl.spawnThread(new CreateThread()).start();
	}
	
	public void run(){
		System.out.println("started thread ... ");
		System.out.println("Locale: "+APICallContext.getCallContext().getCurrentLocale());
		System.out.println("UserId: "+APICallContext.getCallContext().getCurrentUserId());
	}
}

