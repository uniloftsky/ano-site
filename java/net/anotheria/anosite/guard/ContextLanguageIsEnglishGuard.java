package net.anotheria.anosite.guard;

public class ContextLanguageIsEnglishGuard extends ContextLanguageIsGuard{

	@Override
	protected String getDesiredContextLanguage() {
		return "EN";
	}
	
}
