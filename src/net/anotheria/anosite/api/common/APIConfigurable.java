package net.anotheria.anosite.api.common;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

@ConfigureMe(name = "apiconfig")
public class APIConfigurable {
	@Configure	private boolean verboseMethodCalls;
	@Configure	private boolean testMode;
	
	public APIConfigurable(){
		System.out.println("%%%%% ======= %%%%%");
	}

	public boolean isVerboseMethodCalls() {
		return verboseMethodCalls;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setVerboseMethodCalls(boolean value) {
		verboseMethodCalls = value;
	}

	public void setTestMode(boolean value) {
		testMode = value;
	}
}
