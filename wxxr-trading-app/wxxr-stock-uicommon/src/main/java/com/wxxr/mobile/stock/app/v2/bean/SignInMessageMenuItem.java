/**
 * 
 */
package com.wxxr.mobile.stock.app.v2.bean;

/**
 * @author wangxuyang
 *
 */
public class SignInMessageMenuItem extends BaseMenuItem {
	private boolean hasSignIn;
	private int signDays;
	private int score;
	private String message;
	public boolean isHasSignIn() {
		return hasSignIn;
	}
	public void setHasSignIn(boolean hasSignIn) {
		this.hasSignIn = hasSignIn;
	}
	public int getSignDays() {
		return signDays;
	}
	public void setSignDays(int signDays) {
		this.signDays = signDays;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "SignInMessageMenuItem [hasSignIn=" + hasSignIn + ", signDays="
				+ signDays + ", score=" + score + ", message=" + message + "]";
	}
	
}
