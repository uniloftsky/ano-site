package net.anotheria.anosite.api.common;

public interface APIFactory<T extends API> {
	public T createAPI();
}
