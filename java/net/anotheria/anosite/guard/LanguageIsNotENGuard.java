package net.anotheria.anosite.guard;

/**
 * Current language shouldn't be 'EN'.
 * 
 * @author Alexandr Bolbat
 */
public abstract class LanguageIsNotENGuard extends LanguageIsENGuard {

	@Override
	protected boolean shouldMatch() {
		return false;
	}

}
