<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><div
	class="contactform">
<form method="post" action="Register.html">
<fieldset><legend>&nbsp;�ber mich/uns&nbsp;</legend> <!--<p><label for="___" class="left">___</label> <input name="___"-->
<!--	id="___" class="field" value="" tabindex="____" type="text" /></p>-->

<p><label for="sex" class="left">Geschlecht:</label> <select
	name="sex" id="sex" class="combo">
	<option value="woman">Frau</option>
	<option value="man">Mann</option>
	<option value="pairMW">Paar(M/W)</option>
	<option value="pairWW">Paar(W/W)</option>
	<option value="pairMM">Paar(M/M)</option>
	<option value="group">Gruppe</option>
	<option value="transgender">Transgender</option>
</select></p>

<p><label for="day" class="left">Geburtsdatum</label> <select
	size="1" name="day" style="width: 45px;" class="combo" tabindex="3">
	<option value="xx">xx</option>
	<option value="01">01</option>
	<option value="02">02</option>
	<option value="03">03</option>
	<option value="04">04</option>

	<option value="05">05</option>
	<option value="06">06</option>
	<option value="07">07</option>
	<option value="08">08</option>
	<option value="09">09</option>
	<option value="10">10</option>

	<option value="11">11</option>
	<option value="12">12</option>
	<option value="13">13</option>
	<option value="14">14</option>
	<option value="15">15</option>
	<option value="16">16</option>

	<option value="17">17</option>
	<option value="18">18</option>
	<option value="19">19</option>
	<option value="20">20</option>
	<option value="21">21</option>
	<option value="22">22</option>

	<option value="23">23</option>
	<option value="24">24</option>
	<option value="25">25</option>
	<option value="26">26</option>
	<option value="27">27</option>
	<option value="28">28</option>

	<option value="29">29</option>
	<option value="30">30</option>
	<option value="31">31</option>
</select> <select size="1" name="monat" style="width: 45px;" class="combo"
	tabindex="4">
	<option value="xx">xx</option>
	<option value="01">01</option>
	<option value="02">02</option>
	<option value="03">03</option>
	<option value="04">04</option>

	<option value="05">05</option>
	<option value="06">06</option>
	<option value="07">07</option>
	<option value="08">08</option>
	<option value="09">09</option>
	<option value="10">10</option>

	<option value="11">11</option>
	<option value="12">12</option>
</select> <select size="1" name="year" style="width: 60px;" class="combo"
	tabindex="5">
	<option value="xxxx">xxxx</option>
	<option value="1940">1940</option>
	<option value="1941">1941</option>

	<option value="1942">1942</option>
	<option value="1943">1943</option>
	<option value="1944">1944</option>
	<option value="1945">1945</option>
	<option value="1946">1946</option>
	<option value="1947">1947</option>

	<option value="1948">1948</option>
	<option value="1949">1949</option>
	<option value="1950">1950</option>
	<option value="1951">1951</option>
	<option value="1952">1952</option>
	<option value="1953">1953</option>

	<option value="1954">1954</option>
	<option value="1955">1955</option>
	<option value="1956">1956</option>
	<option value="1957">1957</option>
	<option value="1958">1958</option>
	<option value="1959">1959</option>

	<option value="1960">1960</option>
	<option value="1961">1961</option>
	<option value="1962">1962</option>
	<option value="1963">1963</option>
	<option value="1964">1964</option>
	<option value="1965">1965</option>

	<option value="1966">1966</option>
	<option value="1967">1967</option>
	<option value="1968">1968</option>
	<option value="1969">1969</option>
	<option value="1970">1970</option>
	<option value="1971">1971</option>

	<option value="1972">1972</option>
	<option value="1973">1973</option>
	<option value="1974">1974</option>
	<option value="1975">1975</option>
	<option value="1976">1976</option>
	<option value="1977">1977</option>

	<option value="1978">1978</option>
	<option value="1979">1979</option>
	<option value="1980">1980</option>
	<option value="1981">1981</option>
	<option value="1982">1982</option>
	<option value="1983">1983</option>

	<option value="1984">1984</option>
	<option value="1985">1985</option>
	<option value="1986">1986</option>
	<option value="1987">1987</option>
	<option value="1988">1988</option>
	<option value="1989">1989</option>

	<option value="1990">1990</option>
</select></p>

<p><label for="habitat" class="left">Ich wohnen in Land</label> <input
	name="habitat" id="habitat" class="field" value="" tabindex="6"
	type="text" /></p>

<p><label for="nationality" class="left">Maine Nationalit�t</label>
<input name="nationality" id="nationality" class="field" value=""
	tabindex="7" type="text" /></p>

<p><input name="prev" id="prev" class="button" value="Prev"
	tabindex="4" type="submit"> <input name="next" id="next"
	class="button" value="Next" tabindex="4" type="submit"></p>
</fieldset>
</form>
</div>