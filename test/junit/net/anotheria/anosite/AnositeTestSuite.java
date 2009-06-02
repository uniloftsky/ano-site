package net.anotheria.anosite;


import net.anotheria.anosite.content.variables.CalendarProcessorTestCase;
import net.anotheria.anosite.content.variables.TextResourceProcessorTestCase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={CalendarProcessorTestCase.class,TextResourceProcessorTestCase.class} )
public class AnositeTestSuite {

}
