package net.anotheria.anosite.access;

import net.anotheria.access.SecurityObject;
import net.anotheria.anoplass.api.API;

/**
 * {@link API} for validate current user access to requested Page, Box, Action, Wizard, etc.
 * 
 * @author Alexandr Bolbat
 */
public interface AnoSiteAccessAPI extends API {

	/**
	 * Validate access for given page. If access operation not configured for given page this validation return <code>true</code>.
	 * 
	 * @param pageId
	 *            - given page it
	 * @return <code>true</code> if have access or <code>false</code>
	 * @throws AnoSiteAccessAPIException
	 */
	boolean isAllowedForPage(String pageId) throws AnoSiteAccessAPIException;

	/**
	 * Validate access for given box. If access operation not configured for given box this validation return <code>true</code>.
	 * 
	 * @param boxId
	 *            - given box it
	 * @return <code>true</code> if have access or <code>false</code>
	 * @throws AnoSiteAccessAPIException
	 */
	boolean isAllowedForBox(String boxId) throws AnoSiteAccessAPIException;

	/**
	 * Validate access for given navigation item. If access operation not configured for given navigation item this validation return <code>true</code>.
	 * 
	 * @param naviItemId
	 *            - given navigation item it
	 * @return <code>true</code> if have access or <code>false</code>
	 * @throws AnoSiteAccessAPIException
	 */
	boolean isAllowedForNaviItem(String naviItemId) throws AnoSiteAccessAPIException;

	/**
	 * Validate access for given action. If access operation not configured for given action this validation return <code>true</code>.
	 * 
	 * @param actionId
	 *            - given action it
	 * @return <code>true</code> if have access or <code>false</code>
	 * @throws AnoSiteAccessAPIException
	 */
	boolean isAllowedForAction(String actionId) throws AnoSiteAccessAPIException;

	/**
	 * Validate access for given wizard. If access operation not configured for given wizard this validation return <code>true</code>.
	 * 
	 * @param wizardId
	 *            - given wizard it
	 * @return <code>true</code> if have access or <code>false</code>
	 * @throws AnoSiteAccessAPIException
	 */
	boolean isAllowedForWizard(String wizardId) throws AnoSiteAccessAPIException;

	/**
	 * Validate access for given action. If access operation not configured for given action this validation return <code>true</code>.
	 *
	 * @param actionName given action name
	 * @param object action executor
	 * @param subject action target
	 * @return <code>true</code> if have access or <code>false</code>
	 * @throws AnoSiteAccessAPIException
	 */
	boolean isAllowed(String actionName, SecurityObject object, SecurityObject subject) throws AnoSiteAccessAPIException;

	/**
	 * Makes hash for password.
	 * @param plainTextPassword pass
	 * @return hash
	 */
	public String hashPassword(String plainTextPassword);

	/**
	 * Get marker for pass hash.
	 * @return hash marker
	 */
	public String getHashMarker();

	/**
	 * Check if user can login.
	 * @param login user's login
	 * @param password user's plain text pass.
	 * @return true is user can login, false otherwise.
	 * @throws RuntimeException if user not found.
	 */
	public boolean canUserLogin(String login, String password);

	/**
	 * Set new password for user.
	 * @param login user's login
	 * @param newPassword new password
	 */
	public void changeUserPassword(String login, String newPassword);

	/**
	 *  Check if user exists.
	 * @param login user's login
	 * @return true if user exists, false otherwise.
	 */
	public boolean isUserExists(String login);

	/**
	 * Get CMS user id by login
	 * @param login user's login
	 * @return id
	 */
	public String getUserIdByLogin(String login);

	/**
	 * Get CMS user login by id
	 * @param id user's id
	 * @return login
	 */
	public String getUserLoginById(String id);
}
