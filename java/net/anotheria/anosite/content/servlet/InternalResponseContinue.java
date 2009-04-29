package net.anotheria.anosite.content.servlet;

import net.anotheria.anosite.shared.InternalResponseCode;

public class InternalResponseContinue extends InternalResponse{
	public InternalResponseContinue(){
		super(InternalResponseCode.CONTINUE);
	}
}
