package net.anotheria.anosite.guard;

public class ContextLanguageIsGermanGuard extends ContextLanguageIsGuard{

	@Override
	protected String getDesiredContextLanguage() {
		return "DE";
	}
	
}
