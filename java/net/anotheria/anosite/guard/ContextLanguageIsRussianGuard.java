package net.anotheria.anosite.guard;

public class ContextLanguageIsRussianGuard extends ContextLanguageIsGuard{

	@Override
	protected String getDesiredContextLanguage() {
		return "RU";
	}
	
}
